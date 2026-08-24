package com.anupam.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Spring Batch application.
 * <p>
 * This application demonstrates Spring Batch processing capabilities including
 * reading transactions from a CSV file, processing them for suspicious activity,
 * and writing results to a database.
 * </p>
 *
 * @author Anupam
 */
@SpringBootApplication
public class BatchApplication {

    /**
     * Bootstraps the Spring Boot application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(BatchApplication.class, args);
    }
}
