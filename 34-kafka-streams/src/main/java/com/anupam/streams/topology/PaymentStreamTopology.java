package com.anupam.streams.topology;

import com.anupam.streams.model.EnrichedPayment;
import com.anupam.streams.model.RawPayment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.WindowStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

/**
 * Defines the Kafka Streams topology for processing raw payment events.
 * The topology performs the following pipeline steps:
 * <ol>
 *   <li>Deserialize raw payment JSON from the input topic</li>
 *   <li>Filter out payments with non-positive amounts</li>
 *   <li>Enrich payments with a processing timestamp and value-based status</li>
 *   <li>Group by currency and count per 1-minute tumbling window</li>
 *   <li>Write windowed counts to the output topic</li>
 * </ol>
 *
 * @author Anupam
 */
@Component
public class PaymentStreamTopology {

    private static final Logger log = LoggerFactory.getLogger(PaymentStreamTopology.class);
    private static final String INPUT_TOPIC = "raw-payments";
    private static final String OUTPUT_TOPIC = "payment-counts";

    /** Name of the materialized windowed state store for interactive queries. */
    public static final String STATE_STORE_NAME = "payment-counts-store";

    private final ObjectMapper objectMapper;

    /**
     * Constructs the topology component with a Jackson ObjectMapper configured
     * for Java Time module support.
     */
    public PaymentStreamTopology() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Builds the Kafka Streams topology and wires it into the provided StreamsBuilder.
     * This method is auto-invoked by Spring Kafka during application startup.
     *
     * @param streamsBuilder the Spring-managed StreamsBuilder for topology construction
     */
    @Autowired
    public void buildTopology(StreamsBuilder streamsBuilder) {
        KStream<String, String> rawStream = streamsBuilder.stream(INPUT_TOPIC,
                Consumed.with(Serdes.String(), Serdes.String()));

        // Step 1: Deserialize raw payment JSON, filtering out deserialization failures
        KStream<String, RawPayment> payments = rawStream
                .mapValues(this::deserializeRawPayment)
                .filter((key, payment) -> payment != null);

        // Step 2: Filter payments - only process those with positive amounts
        KStream<String, RawPayment> validPayments = payments
                .filter((key, payment) -> payment.amount() > 0)
                .peek((key, payment) -> log.debug("Processing payment: {} - {} {}",
                        payment.id(), payment.amount(), payment.currency()));

        // Step 3: Enrich with processing timestamp and derive status based on amount threshold
        KStream<String, EnrichedPayment> enrichedPayments = validPayments
                .mapValues(raw -> new EnrichedPayment(
                        raw.id(),
                        raw.currency(),
                        raw.amount(),
                        raw.amount() > 10000 ? "HIGH_VALUE" : "NORMAL",
                        Instant.now()
                ));

        // Step 4: Group by currency and count within 1-minute tumbling windows
        KTable<Windowed<String>, Long> paymentCounts = enrichedPayments
                .groupBy((key, payment) -> payment.currency(),
                        Grouped.with(Serdes.String(), Serdes.String())
                                .withValueSerde(Serdes.serdeFrom(
                                        (topic, data) -> serialize(data),
                                        (topic, data) -> deserializeEnrichedPayment(new String(data))
                                )))
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(1)))
                .count(Materialized.<String, Long, WindowStore<Bytes, byte[]>>as(STATE_STORE_NAME)
                        .withKeySerde(Serdes.String())
                        .withValueSerde(Serdes.Long()));

        // Step 5: Write windowed counts as JSON to the output topic
        paymentCounts
                .toStream()
                .map((windowedKey, count) -> KeyValue.pair(
                        windowedKey.key(),
                        String.format("{\"currency\":\"%s\",\"count\":%d,\"windowStart\":\"%s\",\"windowEnd\":\"%s\"}",
                                windowedKey.key(), count,
                                windowedKey.window().startTime(),
                                windowedKey.window().endTime())))
                .to(OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.String()));

        log.info("Payment stream topology built successfully");
    }

    /**
     * Deserializes a JSON string into a RawPayment record.
     *
     * @param json the JSON representation of a raw payment
     * @return the deserialized RawPayment, or null if deserialization fails
     */
    private RawPayment deserializeRawPayment(String json) {
        try {
            return objectMapper.readValue(json, RawPayment.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize RawPayment: {}", json, e);
            return null;
        }
    }

    /**
     * Deserializes a JSON string into an EnrichedPayment record.
     *
     * @param json the JSON representation of an enriched payment
     * @return the deserialized EnrichedPayment, or null if deserialization fails
     */
    private EnrichedPayment deserializeEnrichedPayment(String json) {
        try {
            return objectMapper.readValue(json, EnrichedPayment.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize EnrichedPayment: {}", json, e);
            return null;
        }
    }

    /**
     * Serializes an object to a byte array using Jackson.
     *
     * @param obj the object to serialize
     * @return the serialized byte array, or an empty array if serialization fails
     */
    private byte[] serialize(Object obj) {
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize object", e);
            return new byte[0];
        }
    }
}
