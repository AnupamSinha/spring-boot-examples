package com.anupam.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Security OAuth2 / JWT Demo - Main Application Entry Point.
 *
 * Demonstrates securing REST APIs with OAuth2 Resource Server and JWT validation.
 * Features role-based access control using Keycloak-style realm_access claims,
 * method-level security with @PreAuthorize, and JWT user info extraction.
 *
 * @author Anupam
 */
@SpringBootApplication
public class SecurityOAuth2Application {
    public static void main(String[] args) {
        SpringApplication.run(SecurityOAuth2Application.class, args);
    }
}
