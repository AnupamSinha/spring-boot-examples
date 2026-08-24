package com.anupam.cqrs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the CQRS and Event Sourcing demo application.
 * <p>
 * This application demonstrates the Command Query Responsibility Segregation (CQRS)
 * pattern combined with Event Sourcing, where commands and queries are handled by
 * separate models and all state changes are persisted as an immutable sequence of events.
 * </p>
 *
 * @author Anupam
 */
@SpringBootApplication
public class CqrsApplication {

    /**
     * Bootstraps the Spring Boot application.
     *
     * @param args command-line arguments passed at startup
     */
    public static void main(String[] args) {
        SpringApplication.run(CqrsApplication.class, args);
    }
}
