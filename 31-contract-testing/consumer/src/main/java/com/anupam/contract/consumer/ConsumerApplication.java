package com.anupam.contract.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot application entry point for the Contract Testing Consumer.
 * This service acts as the consumer side in the contract testing setup,
 * consuming APIs provided by the producer service.
 *
 * @author Anupam
 */
@SpringBootApplication
public class ConsumerApplication {

    /**
     * Application entry point that bootstraps the Spring Boot consumer application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }
}
