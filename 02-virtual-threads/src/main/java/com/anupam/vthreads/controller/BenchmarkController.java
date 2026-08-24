package com.anupam.vthreads.controller;

import com.anupam.vthreads.service.IoSimulatorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Benchmark controller that compares platform threads vs. virtual threads
 * under concurrent blocking I/O load.
 *
 * Platform threads are limited by the fixed pool size (2x available processors),
 * while virtual threads scale to the number of tasks with minimal overhead.
 *
 * @author Anupam
 */
@RestController
@RequestMapping("/api")
public class BenchmarkController {

    private final IoSimulatorService ioSimulatorService;

    public BenchmarkController(IoSimulatorService ioSimulatorService) {
        this.ioSimulatorService = ioSimulatorService;
    }

    /**
     * Benchmarks N concurrent requests comparing platform threads vs virtual threads.
     * Each request simulates a 500ms blocking I/O call.
     *
     * @param requests number of concurrent tasks to run (default: 100)
     * @return comparison results including execution times and speedup factor
     */
    @GetMapping("/benchmark")
    public Map<String, Object> benchmark(@RequestParam(defaultValue = "100") int requests) {
        long platformTime = runWithPlatformThreads(requests);
        long virtualTime = runWithVirtualThreads(requests);

        double speedup = (double) platformTime / virtualTime;

        return Map.of(
                "requests", requests,
                "blockingDurationPerRequestMs", 500,
                "platformThreadsTimeMs", platformTime,
                "virtualThreadsTimeMs", virtualTime,
                "speedup", String.format("%.2fx", speedup),
                "winner", virtualTime < platformTime ? "Virtual Threads" : "Platform Threads"
        );
    }

    /**
     * Runs tasks using a fixed platform thread pool.
     * Pool size is capped at 2x available processors, causing queuing under high concurrency.
     */
    private long runWithPlatformThreads(int requests) {
        long start = System.currentTimeMillis();

        try (ExecutorService executor = Executors.newFixedThreadPool(
                Math.min(requests, Runtime.getRuntime().availableProcessors() * 2))) {
            List<Future<String>> futures = new ArrayList<>();

            for (int i = 0; i < requests; i++) {
                futures.add(executor.submit(() -> ioSimulatorService.simulateBlockingCall(500)));
            }

            for (Future<String> future : futures) {
                future.get();
            }
        } catch (Exception e) {
            throw new RuntimeException("Platform thread benchmark failed", e);
        }

        return System.currentTimeMillis() - start;
    }

    /**
     * Runs tasks using virtual threads (one per task).
     * All tasks execute truly concurrently regardless of count.
     */
    private long runWithVirtualThreads(int requests) {
        long start = System.currentTimeMillis();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<String>> futures = new ArrayList<>();

            for (int i = 0; i < requests; i++) {
                futures.add(executor.submit(() -> ioSimulatorService.simulateBlockingCall(500)));
            }

            for (Future<String> future : futures) {
                future.get();
            }
        } catch (Exception e) {
            throw new RuntimeException("Virtual thread benchmark failed", e);
        }

        return System.currentTimeMillis() - start;
    }
}
