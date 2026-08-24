package com.anupam.vthreads.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Simulates I/O-bound operations (external API calls, database queries, etc.)
 * using Thread.sleep to demonstrate how virtual threads handle blocking work.
 *
 * Logs the thread name and type (virtual vs. platform) for each operation
 * so the difference in concurrency behavior is visible in the output.
 *
 * @author Anupam
 */
@Service
public class IoSimulatorService {

    private static final Logger log = LoggerFactory.getLogger(IoSimulatorService.class);

    /**
     * Simulates an external API call with a configurable blocking delay.
     *
     * @param durationMs the duration to block in milliseconds
     * @return a string describing which thread handled the call
     */
    public String simulateBlockingCall(long durationMs) {
        String threadName = Thread.currentThread().getName();
        boolean isVirtual = Thread.currentThread().isVirtual();
        log.info("Executing blocking call on thread: {} (virtual: {})", threadName, isVirtual);

        try {
            Thread.sleep(durationMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Blocking call interrupted", e);
        }

        return "Completed on thread: " + threadName + " (virtual: " + isVirtual + ")";
    }

    /**
     * Simulates a shorter external service call (500ms) identified by a task ID.
     * Useful for parallel execution benchmarks.
     *
     * @param taskId a numeric identifier for the task
     * @return a string with the task result and thread info
     */
    public String simulateExternalApiCall(int taskId) {
        String threadName = Thread.currentThread().getName();
        boolean isVirtual = Thread.currentThread().isVirtual();
        log.info("Task {} running on thread: {} (virtual: {})", taskId, threadName, isVirtual);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Task " + taskId + " interrupted", e);
        }

        return "Task " + taskId + " completed on " + threadName + " (virtual: " + isVirtual + ")";
    }
}
