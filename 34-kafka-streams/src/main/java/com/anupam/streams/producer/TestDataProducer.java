package com.anupam.streams.producer;

import com.anupam.streams.model.RawPayment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Scheduled producer that generates synthetic payment data at a fixed interval
 * and publishes it to the "raw-payments" Kafka topic. Used for testing and
 * demonstrating the Kafka Streams processing topology.
 *
 * @author Anupam
 */
@Component
public class TestDataProducer {

    private static final Logger log = LoggerFactory.getLogger(TestDataProducer.class);
    private static final String TOPIC = "raw-payments";
    private static final List<String> CURRENCIES = List.of("USD", "EUR", "GBP", "JPY", "INR");

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final Random random = new Random();

    /**
     * Constructs the TestDataProducer with Kafka template and JSON mapper.
     *
     * @param kafkaTemplate the Kafka template for sending messages
     * @param objectMapper  the Jackson ObjectMapper for JSON serialization
     */
    public TestDataProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Produces a random test payment every 2 seconds.
     * Generates a payment with a random UUID, random currency from the supported list,
     * and a random amount between 0 and 50,000 (rounded to 2 decimal places).
     * The payment is serialized to JSON and sent to the raw-payments topic.
     */
    @Scheduled(fixedRate = 2000)
    public void produceTestPayment() {
        // Generate a random payment with UUID, random currency, and random amount
        RawPayment payment = new RawPayment(
                UUID.randomUUID().toString(),
                CURRENCIES.get(random.nextInt(CURRENCIES.size())),
                Math.round(random.nextDouble() * 50000 * 100.0) / 100.0
        );

        try {
            // Serialize to JSON and send to Kafka topic with payment ID as key
            String json = objectMapper.writeValueAsString(payment);
            kafkaTemplate.send(TOPIC, payment.id(), json);
            log.info("Produced payment: {} {} {}", payment.id(), payment.currency(), payment.amount());
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize payment", e);
        }
    }
}
