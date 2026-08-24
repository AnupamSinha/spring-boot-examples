package com.anupam.performance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Configures asynchronous task execution with a tuned thread pool.
 *
 * Enables @Async support so service methods can return CompletableFuture
 * and execute on a dedicated thread pool, keeping the request-handling
 * threads free for new incoming requests.
 *
 * @author Anupam
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    /**
     * Creates a thread pool executor for async operations.
     *
     * Pool sizing rationale:
     * - corePoolSize(10): baseline threads always available
     * - maxPoolSize(50): allows burst handling under load
     * - queueCapacity(100): buffers tasks before rejecting
     * - graceful shutdown: waits up to 30s for in-flight tasks to complete
     */
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("async-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        return executor;
    }
}
