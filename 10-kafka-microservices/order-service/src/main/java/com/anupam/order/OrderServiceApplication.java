package com.anupam.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Order Service - Kafka Microservices Demo.
 *
 * Accepts order creation requests via REST, computes the total,
 * and publishes an OrderCreatedEvent to the "orders" Kafka topic.
 * Downstream services (payment, notification) react to this event.
 *
 * @author Anupam
 */
@SpringBootApplication
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
