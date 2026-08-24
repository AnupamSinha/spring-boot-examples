package com.anupam.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Notification Service - Kafka Microservices Demo.
 *
 * Listens to both "orders" and "payments" Kafka topics and simulates
 * sending email notifications for order confirmations and payment receipts.
 *
 * @author Anupam
 */
@SpringBootApplication
public class NotificationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
}
