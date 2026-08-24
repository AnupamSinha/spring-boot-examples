package com.anupam.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot application entry point for the Elasticsearch Search Service.
 * Demonstrates full-text search, fuzzy matching, highlighting, and aggregation
 * capabilities using Spring Data Elasticsearch.
 *
 * @author Anupam
 */
@SpringBootApplication
public class SearchApplication {

    /**
     * Application entry point that bootstraps the Spring Boot search application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class, args);
    }
}
