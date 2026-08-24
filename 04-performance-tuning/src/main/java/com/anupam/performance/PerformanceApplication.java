package com.anupam.performance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Performance Tuning Demo - Main Application Entry Point.
 *
 * Demonstrates key Spring Boot performance optimization techniques including:
 * - Caffeine caching for hot data paths
 * - HikariCP connection pool tuning
 * - Async processing with ThreadPoolTaskExecutor
 * - Micrometer metrics for performance visibility
 * - Hibernate query hints for read-only optimization
 *
 * @author Anupam
 */
@SpringBootApplication
public class PerformanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PerformanceApplication.class, args);
    }
}
