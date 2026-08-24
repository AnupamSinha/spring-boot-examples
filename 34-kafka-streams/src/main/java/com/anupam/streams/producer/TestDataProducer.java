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

@Component
public class TestDataProducer {

    private static final Logger log = LoggerFactory.getLogger(TestDataProducer.class);
    private static final String TOPIC = "raw-payments";
    private static final List<String> CURRENCIES = List.of("USD", "EUR", "GBP", "JPY", "INR");

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final Random random = new Random();

    public TestDataProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedRate = 2000)
    public void produceTestPayment() {
        RawPayment payment = new RawPayment(
                UUID.randomUUID().toString(),
                CURRENCIES.get(random.nextInt(CURRENCIES.size())),
                Math.round(random.nextDouble() * 50000 * 100.0) / 100.0
        );

        try {
            String json = objectMapper.writeValueAsString(payment);
            kafkaTemplate.send(TOPIC, payment.id(), json);
            log.info("Produced payment: {} {} {}", payment.id(), payment.currency(), payment.amount());
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize payment", e);
        }
    }
}
