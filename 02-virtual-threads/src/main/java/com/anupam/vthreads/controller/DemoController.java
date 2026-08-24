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

@RestController
@RequestMapping("/api")
public class DemoController {

    private final IoSimulatorService ioSimulatorService;

    public DemoController(IoSimulatorService ioSimulatorService) {
        this.ioSimulatorService = ioSimulatorService;
    }

    /**
     * Simulates a blocking IO operation (500ms delay).
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
     * Returns information about the current thread handling this request.
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
     * Runs 10 blocking operations concurrently using virtual threads via ExecutorService.
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
