package com.anupam.k8s;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Kubernetes Deployment Demo - Main Application Entry Point.
 *
 * A minimal Spring Boot application designed for Kubernetes deployment.
 * Includes health/info endpoints, externalized configuration via ConfigMaps,
 * and environment variable injection (e.g., POD_NAME from the Downward API).
 *
 * @author Anupam
 */
@SpringBootApplication
public class K8sDeployApplication {

    public static void main(String[] args) {
        SpringApplication.run(K8sDeployApplication.class, args);
    }
}
