package com.anupam.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Payment Service - Kafka Microservices Demo.
 *
 * Listens to the "orders" Kafka topic for OrderCreatedEvents,
 * processes payments, and publishes PaymentCompletedEvents to
 * the "payments" topic for downstream notification handling.
 *
 * @author Anupam
 */
@SpringBootApplication
public class PaymentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
    }
}
