package com.anupam.vthreads.controller;

import com.anupam.vthreads.service.IoSimulatorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Demonstration controller showcasing virtual thread behavior.
 *
 * Provides endpoints to observe virtual threads in action:
 * - Single blocking call
 * - Thread info inspection
 * - Parallel task execution with speedup measurement
 *
 * @author Anupam
 */
@RestController
@RequestMapping("/api")
public class DemoController {

    private final IoSimulatorService ioSimulatorService;

    public DemoController(IoSimulatorService ioSimulatorService) {
        this.ioSimulatorService = ioSimulatorService;
    }

    /**
     * Simulates a single 500ms blocking I/O call.
     * Response includes the thread name and whether it was virtual.
     */
    @GetMapping("/blocking")
    public Map<String, Object> blocking() {
        long start = System.currentTimeMillis();
        String result = ioSimulatorService.simulateBlockingCall(500);
        long duration = System.currentTimeMillis() - start;

        return Map.of(
                "result", result,
                "durationMs", duration,
                "thread", Thread.currentThread().getName(),
                "virtual", Thread.currentThread().isVirtual()
        );
    }

    /**
     * Returns metadata about the current request-handling thread.
     * Useful for verifying that Tomcat is indeed using virtual threads.
     */
    @GetMapping("/thread-info")
    public Map<String, Object> threadInfo() {
        Thread currentThread = Thread.currentThread();
        return Map.of(
                "threadName", currentThread.getName(),
                "isVirtual", currentThread.isVirtual(),
                "threadId", currentThread.threadId(),
                "threadClass", currentThread.getClass().getName()
        );
    }

    /**
     * Runs 10 blocking operations (500ms each) concurrently using virtual threads.
     * Demonstrates near-linear speedup: ~500ms total instead of ~5000ms sequential.
     */
    @GetMapping("/parallel")
    public Map<String, Object> parallel() {
        long start = System.currentTimeMillis();
        List<String> results = new ArrayList<>();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<String>> futures = new ArrayList<>();

            for (int i = 1; i <= 10; i++) {
                final int taskId = i;
                futures.add(executor.submit(() -> ioSimulatorService.simulateExternalApiCall(taskId)));
            }

            for (Future<String> future : futures) {
                results.add(future.get());
            }
        } catch (Exception e) {
            throw new RuntimeException("Parallel execution failed", e);
        }

        long duration = System.currentTimeMillis() - start;

        return Map.of(
                "totalTasks", 10,
                "totalDurationMs", duration,
                "expectedSequentialMs", 5000,
                "speedup", String.format("%.2fx", 5000.0 / duration),
                "results", results,
                "handlerThread", Thread.currentThread().getName(),
                "handlerIsVirtual", Thread.currentThread().isVirtual()
        );
    }
}
