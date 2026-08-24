package com.anupam.loadtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot application entry point for the Gatling Load Test target service.
 * This application provides a simple REST API that serves as the system under test
 * for Gatling performance and load testing scenarios.
 *
 * @author Anupam
 */
@SpringBootApplication
public class LoadTestApp {

    /**
     * Application entry point that bootstraps the Spring Boot load test application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(LoadTestApp.class, args);
    }
}
