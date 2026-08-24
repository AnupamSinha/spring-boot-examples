package com.anupam.fullstack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot application entry point for the React Fullstack demo backend.
 * <p>
 * Provides a secured REST API with JWT authentication for a React frontend,
 * demonstrating a typical fullstack architecture with Spring Boot + React.
 * </p>
 *
 * @author Anupam
 */
@SpringBootApplication
public class FullstackApplication {

    /**
     * Application entry point that bootstraps the Spring context.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(FullstackApplication.class, args);
    }
}
