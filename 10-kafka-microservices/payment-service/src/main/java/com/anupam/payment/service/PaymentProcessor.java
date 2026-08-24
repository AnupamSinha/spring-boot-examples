package com.anupam.payment.service;

import com.anupam.events.OrderCreatedEvent;
import com.anupam.events.PaymentCompletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

/**
 * Kafka consumer that processes payments for incoming orders.
 *
 * Business rule: orders over $10,000 are automatically declined (FAILED),
 * all others are approved (COMPLETED). After processing, publishes a
 * PaymentCompletedEvent to the "payments" topic.
 *
 * @author Anupam
 */
@Service
public class PaymentProcessor {

    private static final Logger log = LoggerFactory.getLogger(PaymentProcessor.class);
    private final KafkaTemplate<String, PaymentCompletedEvent> kafkaTemplate;

    public PaymentProcessor(KafkaTemplate<String, PaymentCompletedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Listens to the "orders" topic and processes each order's payment.
     * Publishes the result to the "payments" topic for notification handling.
     */
    @KafkaListener(topics = "orders", groupId = "payment-service")
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Processing payment for order: {} amount: {}", event.orderId(), event.totalAmount());

        // Simple business rule: reject orders over $10,000
        String status = event.totalAmount().doubleValue() > 10000 ? "FAILED" : "COMPLETED";
        String paymentId = "PAY-" + UUID.randomUUID().toString().substring(0, 8);

        PaymentCompletedEvent paymentEvent = new PaymentCompletedEvent(
                paymentId, event.orderId(), status, event.totalAmount(), Instant.now());

        kafkaTemplate.send("payments", event.orderId(), paymentEvent);
        log.info("Payment {} for order {} — status: {}", paymentId, event.orderId(), status);
    }
}
