package com.anupam.mongo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the MongoDB demo application.
 * <p>
 * This application demonstrates Spring Data MongoDB features including CRUD operations,
 * custom queries with {@code @Query} annotations, and MongoDB aggregation pipelines.
 * </p>
 *
 * @author Anupam
 */
@SpringBootApplication
public class MongoApplication {

    /**
     * Bootstraps the Spring Boot application.
     *
     * @param args command-line arguments passed at startup
     */
    public static void main(String[] args) {
        SpringApplication.run(MongoApplication.class, args);
    }
}
