package com.anupam.notification.service;

import com.anupam.events.OrderCreatedEvent;
import com.anupam.events.PaymentCompletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Kafka consumer that handles notification events.
 *
 * Subscribes to:
 * - "orders" topic: sends order confirmation notifications
 * - "payments" topic: sends payment receipt/failure notifications
 *
 * In production, this would integrate with an email service (SendGrid, SES, etc.).
 *
 * @author Anupam
 */
@Service
public class NotificationConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);

    /** Sends an order confirmation notification when a new order is created. */
    @KafkaListener(topics = "orders", groupId = "notification-service")
    public void onOrderCreated(OrderCreatedEvent event) {
        log.info("[EMAIL] Order {} confirmed for customer {}. Total: {}",
                event.orderId(), event.customerId(), event.totalAmount());
    }

    /** Sends a payment receipt/failure notification after payment processing. */
    @KafkaListener(topics = "payments", groupId = "notification-service")
    public void onPaymentCompleted(PaymentCompletedEvent event) {
        log.info("[EMAIL] Payment {} for order {} — Status: {}. Amount: {}",
                event.paymentId(), event.orderId(), event.status(), event.amount());
    }
}
