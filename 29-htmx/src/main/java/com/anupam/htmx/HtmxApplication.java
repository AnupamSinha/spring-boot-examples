package com.anupam.htmx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot application entry point for the HTMX demo.
 * <p>
 * Demonstrates server-side rendering with Thymeleaf combined with
 * HTMX for partial page updates without a full JavaScript framework.
 * </p>
 *
 * @author Anupam
 */
@SpringBootApplication
public class HtmxApplication {

    /**
     * Application entry point that bootstraps the Spring context.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(HtmxApplication.class, args);
    }
}
