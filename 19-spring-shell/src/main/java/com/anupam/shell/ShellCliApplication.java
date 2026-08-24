package com.anupam.shell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Spring Shell CLI application.
 * <p>
 * This application demonstrates Spring Shell capabilities including file operations,
 * HTTP commands, greeting utilities, and interactive terminal-based workflows.
 * </p>
 *
 * @author Anupam
 */
@SpringBootApplication
public class ShellCliApplication {

    /**
     * Bootstraps the Spring Boot application with Spring Shell support.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(ShellCliApplication.class, args);
    }
}
