package com.anupam.scheduling.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * Configuration class for the task scheduler thread pool.
 * Customizes the default scheduler with a dedicated thread pool, error handling,
 * and graceful shutdown behavior for scheduled tasks.
 *
 * @author Anupam
 */
@Configuration
public class SchedulerConfig {

    /**
     * Creates a customized ThreadPoolTaskScheduler bean with:
     * <ul>
     *   <li>Pool size of 5 threads for concurrent task execution</li>
     *   <li>Named thread prefix for easier debugging and monitoring</li>
     *   <li>Custom error handler to log scheduling failures</li>
     *   <li>Graceful shutdown with 30-second termination timeout</li>
     * </ul>
     *
     * @return the configured thread pool task scheduler
     */
    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        // Allow up to 5 concurrent scheduled tasks
        scheduler.setPoolSize(5);
        // Prefix thread names for easy identification in logs and thread dumps
        scheduler.setThreadNamePrefix("scheduled-task-");
        // Custom error handler to prevent silent task failures
        scheduler.setErrorHandler(throwable ->
                System.err.println("Scheduled task error: " + throwable.getMessage()));
        // Wait for running tasks to complete during application shutdown
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        // Allow up to 30 seconds for tasks to finish before forced termination
        scheduler.setAwaitTerminationSeconds(30);
        return scheduler;
    }
}
