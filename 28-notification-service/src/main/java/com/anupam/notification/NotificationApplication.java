package com.anupam.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot application entry point for the Notification Service.
 * <p>
 * Demonstrates an event-driven notification system using Kafka for
 * asynchronous delivery across multiple channels (Email, SMS, Push)
 * with retry support and dead letter topic handling.
 * </p>
 *
 * @author Anupam
 */
@SpringBootApplication
public class NotificationApplication {

    /**
     * Application entry point that bootstraps the Spring context.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }
}
