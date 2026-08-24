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

@Component
public class PaymentStreamTopology {

    private static final Logger log = LoggerFactory.getLogger(PaymentStreamTopology.class);
    private static final String INPUT_TOPIC = "raw-payments";
    private static final String OUTPUT_TOPIC = "payment-counts";
    public static final String STATE_STORE_NAME = "payment-counts-store";

    private final ObjectMapper objectMapper;

    public PaymentStreamTopology() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Autowired
    public void buildTopology(StreamsBuilder streamsBuilder) {
        KStream<String, String> rawStream = streamsBuilder.stream(INPUT_TOPIC,
                Consumed.with(Serdes.String(), Serdes.String()));

        // 1. Deserialize raw payments
        KStream<String, RawPayment> payments = rawStream
                .mapValues(this::deserializeRawPayment)
                .filter((key, payment) -> payment != null);

        // 2. Filter: only process payments with positive amount
        KStream<String, RawPayment> validPayments = payments
                .filter((key, payment) -> payment.amount() > 0)
                .peek((key, payment) -> log.debug("Processing payment: {} - {} {}",
                        payment.id(), payment.amount(), payment.currency()));

        // 3. Transform: enrich with timestamp and status
        KStream<String, EnrichedPayment> enrichedPayments = validPayments
                .mapValues(raw -> new EnrichedPayment(
                        raw.id(),
                        raw.currency(),
                        raw.amount(),
                        raw.amount() > 10000 ? "HIGH_VALUE" : "NORMAL",
                        Instant.now()
                ));

        // 4. Group by currency and count per tumbling window (1 minute)
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

        // 5. Write counts to output topic
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

    private RawPayment deserializeRawPayment(String json) {
        try {
            return objectMapper.readValue(json, RawPayment.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize RawPayment: {}", json, e);
            return null;
        }
    }

    private EnrichedPayment deserializeEnrichedPayment(String json) {
        try {
            return objectMapper.readValue(json, EnrichedPayment.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize EnrichedPayment: {}", json, e);
            return null;
        }
    }

    private byte[] serialize(Object obj) {
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize object", e);
            return new byte[0];
        }
    }
}
