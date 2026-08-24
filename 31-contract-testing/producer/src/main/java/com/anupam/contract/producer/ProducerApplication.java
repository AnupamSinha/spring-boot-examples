package com.anupam.contract.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot application entry point for the Contract Testing Producer.
 * This service acts as the producer side in the contract testing setup,
 * exposing APIs whose contracts are verified against consumer expectations.
 *
 * @author Anupam
 */
@SpringBootApplication
public class ProducerApplication {

    /**
     * Application entry point that bootstraps the Spring Boot producer application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }
}
