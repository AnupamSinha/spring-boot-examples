package com.anupam.streams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Spring Boot application entry point for the Kafka Streams payment processing service.
 * Enables scheduling for periodic test data production and configures the Kafka Streams
 * topology for real-time payment event processing.
 *
 * @author Anupam
 */
@SpringBootApplication
@EnableScheduling
public class KafkaStreamsApplication {

    /**
     * Application entry point that bootstraps the Spring Boot Kafka Streams application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(KafkaStreamsApplication.class, args);
    }
}
