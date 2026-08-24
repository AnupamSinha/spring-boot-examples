package com.anupam.hexagonal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Hexagonal Architecture demo application.
 * <p>
 * This application demonstrates the Ports and Adapters (Hexagonal) architectural pattern,
 * where the domain logic is isolated from infrastructure concerns through well-defined
 * port interfaces. Adapters connect external systems (HTTP, persistence, payment) to the domain.
 * </p>
 *
 * @author Anupam
 */
@SpringBootApplication
public class HexagonalApplication {

    /**
     * Bootstraps the Spring Boot application.
     *
     * @param args command-line arguments passed at startup
     */
    public static void main(String[] args) {
        SpringApplication.run(HexagonalApplication.class, args);
    }
}
