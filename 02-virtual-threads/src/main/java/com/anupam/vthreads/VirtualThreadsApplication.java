package com.anupam.vthreads;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Virtual Threads Demo - Main Application Entry Point.
 *
 * Demonstrates Java 21 Virtual Threads integrated with Spring Boot,
 * showing how blocking I/O operations scale significantly better
 * when handled by virtual threads vs. platform threads.
 *
 * @author Anupam
 */
@SpringBootApplication
public class VirtualThreadsApplication {

    public static void main(String[] args) {
        SpringApplication.run(VirtualThreadsApplication.class, args);
    }
}
