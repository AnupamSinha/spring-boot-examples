package com.anupam.shortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot application entry point for the URL Shortener service.
 * <p>
 * Provides a RESTful API for shortening URLs, redirecting short codes
 * to their original destinations, and tracking click analytics.
 * </p>
 *
 * @author Anupam
 */
@SpringBootApplication
public class UrlShortenerApplication {

    /**
     * Application entry point that bootstraps the Spring context.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(UrlShortenerApplication.class, args);
    }
}
