package com.anupam.flags;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Feature Flags demonstration application.
 * <p>
 * This application showcases runtime feature toggling using the Togglz library,
 * allowing features to be enabled or disabled without redeployment.
 * </p>
 *
 * @author Anupam
 */
@SpringBootApplication
public class FeatureFlagsApplication {

    /**
     * Bootstraps the Spring Boot application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(FeatureFlagsApplication.class, args);
    }
}
