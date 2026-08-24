package com.anupam.scheduling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Spring Boot application entry point for the Task Scheduling Service.
 * Enables Spring's scheduling support for cron-based and fixed-rate task execution.
 * Demonstrates scheduled tasks with ShedLock for distributed lock coordination.
 *
 * @author Anupam
 */
@SpringBootApplication
@EnableScheduling
public class SchedulingApplication {

    /**
     * Application entry point that bootstraps the Spring Boot scheduling application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(SchedulingApplication.class, args);
    }
}
