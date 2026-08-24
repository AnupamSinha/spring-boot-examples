package com.anupam.vthreads.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class IoSimulatorService {

    private static final Logger log = LoggerFactory.getLogger(IoSimulatorService.class);

    /**
     * Simulates an external API call with a blocking sleep.
     * Logs the thread name to demonstrate virtual thread usage.
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
     * Simulates a shorter external service call.
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
