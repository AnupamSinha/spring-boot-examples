package com.anupam.ratelimiter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot application entry point for the Rate Limiter service.
 * <p>
 * Demonstrates a custom annotation-driven rate limiting mechanism
 * using Redis sorted sets and Spring AOP.
 * </p>
 *
 * @author Anupam
 */
@SpringBootApplication
public class RateLimiterApplication {

    /**
     * Application entry point that bootstraps the Spring context.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(RateLimiterApplication.class, args);
    }
}
