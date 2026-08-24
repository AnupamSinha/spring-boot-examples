package com.anupam.outbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main entry point for the Transactional Outbox Pattern demo application.
 * <p>
 * This application demonstrates the Outbox Pattern for reliable event publishing.
 * Instead of directly sending events to a message broker (dual-write problem), events
 * are written to an outbox table in the same transaction as the business data. A
 * scheduled relay then publishes unpublished events to Kafka.
 * </p>
 * <p>
 * {@code @EnableScheduling} activates the scheduled outbox relay polling.
 * </p>
 *
 * @author Anupam
 */
@SpringBootApplication
@EnableScheduling
public class OutboxApplication {

    /**
     * Bootstraps the Spring Boot application.
     *
     * @param args command-line arguments passed at startup
     */
    public static void main(String[] args) {
        SpringApplication.run(OutboxApplication.class, args);
    }
}
