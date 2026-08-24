package com.anupam.native_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * GraalVM Native Image Demo - Main Application Entry Point.
 *
 * Demonstrates compiling a Spring Boot application into a native executable
 * using GraalVM, showcasing sub-second startup times, reduced memory footprint,
 * and reflection handling via runtime hints.
 *
 * @author Anupam
 */
@SpringBootApplication
public class NativeApplication {

    public static void main(String[] args) {
        SpringApplication.run(NativeApplication.class, args);
    }
}
