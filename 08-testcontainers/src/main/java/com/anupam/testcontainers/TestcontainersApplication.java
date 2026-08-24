package com.anupam.testcontainers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Testcontainers Demo - Main Application Entry Point.
 *
 * Demonstrates integration testing with real infrastructure using Testcontainers.
 * Tests run against actual PostgreSQL and Kafka containers spun up on-demand,
 * eliminating the need for in-memory fakes like H2 or embedded Kafka.
 *
 * @author Anupam
 */
@SpringBootApplication
public class TestcontainersApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestcontainersApplication.class, args);
    }
}
