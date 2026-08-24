package com.anupam.observability;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Observability Demo - Main Application Entry Point.
 *
 * Demonstrates the three pillars of observability in Spring Boot:
 * - Metrics: Micrometer counters and timers for payment processing
 * - Tracing: Micrometer Observation API for distributed trace context
 * - Logging: Structured logs with trace/span IDs for correlation
 *
 * @author Anupam
 */
@SpringBootApplication
public class ObservabilityApplication {
    public static void main(String[] args) {
        SpringApplication.run(ObservabilityApplication.class, args);
    }
}
