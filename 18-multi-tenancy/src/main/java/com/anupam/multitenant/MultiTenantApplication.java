package com.anupam.multitenant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the multi-tenancy demonstration application.
 * <p>
 * This application showcases schema-based multi-tenancy where each tenant's data
 * is isolated in a separate database schema, routed dynamically based on
 * the X-Tenant-ID request header.
 * </p>
 *
 * @author Anupam
 */
@SpringBootApplication
public class MultiTenantApplication {

    /**
     * Bootstraps the Spring Boot application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(MultiTenantApplication.class, args);
    }
}
