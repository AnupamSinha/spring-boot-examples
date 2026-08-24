package com.anupam.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * JPA Advanced Demo - Main Application Entry Point.
 *
 * Demonstrates advanced Spring Data JPA features including:
 * - Specification-based dynamic queries (composable predicates)
 * - Interface-based projections (fetch only needed columns)
 * - JPQL and native SQL queries
 * - JPA Auditing (automatic createdAt/updatedAt timestamps)
 *
 * @author Anupam
 */
@SpringBootApplication
public class JpaAdvancedApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaAdvancedApplication.class, args);
    }
}
