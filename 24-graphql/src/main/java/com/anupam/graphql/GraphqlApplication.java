package com.anupam.graphql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the GraphQL demo application.
 * <p>
 * This application demonstrates Spring for GraphQL features including query/mutation
 * mappings, batch data loading to prevent the N+1 problem, and JPA-backed resolvers.
 * </p>
 *
 * @author Anupam
 */
@SpringBootApplication
public class GraphqlApplication {

    /**
     * Bootstraps the Spring Boot application.
     *
     * @param args command-line arguments passed at startup
     */
    public static void main(String[] args) {
        SpringApplication.run(GraphqlApplication.class, args);
    }
}
