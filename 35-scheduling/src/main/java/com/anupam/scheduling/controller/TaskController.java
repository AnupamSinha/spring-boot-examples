package com.anupam.scheduling.controller;

import com.anupam.scheduling.task.DataCleanupTask;
import com.anupam.scheduling.task.HealthCheckTask;
import com.anupam.scheduling.task.ReportGenerationTask;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final ReportGenerationTask reportTask;
    private final DataCleanupTask cleanupTask;
    private final HealthCheckTask healthCheckTask;

    public TaskController(ReportGenerationTask reportTask,
                          DataCleanupTask cleanupTask,
                          HealthCheckTask healthCheckTask) {
        this.reportTask = reportTask;
        this.cleanupTask = cleanupTask;
        this.healthCheckTask = healthCheckTask;
    }

    @PostMapping("/trigger/{name}")
    public ResponseEntity<Map<String, String>> triggerTask(@PathVariable String name) {
        Map<String, String> response = new HashMap<>();

        switch (name) {
            case "report" -> {
                reportTask.generateDailyReport();
                response.put("task", "reportGeneration");
                response.put("status", "triggered");
            }
            case "cleanup" -> {
                cleanupTask.cleanupOldRecords();
                response.put("task", "dataCleanup");
                response.put("status", "triggered");
            }
            case "healthcheck" -> {
                healthCheckTask.checkExternalServices();
                response.put("task", "healthCheck");
                response.put("status", "triggered");
            }
            default -> {
                response.put("error", "Unknown task: " + name);
                return ResponseEntity.badRequest().body(response);
            }
        }

        response.put("triggeredAt", LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();

        status.put("reportGeneration", Map.of(
                "lastRun", reportTask.getLastRunTime() != null
                        ? reportTask.getLastRunTime().toString() : "never",
                "schedule", "cron: 0 0 2 * * * (daily at 2 AM)"
        ));

        status.put("dataCleanup", Map.of(
                "lastRun", cleanupTask.getLastRunTime() != null
                        ? cleanupTask.getLastRunTime().toString() : "never",
                "schedule", "fixedRate: 3600000 (every hour)"
        ));

        status.put("healthCheck", Map.of(
                "lastRun", healthCheckTask.getLastRunTime() != null
                        ? healthCheckTask.getLastRunTime().toString() : "never",
                "schedule", "fixedDelay: 30000 (every 30s after completion)",
                "serviceStatus", healthCheckTask.getServiceStatus()
        ));

        return ResponseEntity.ok(status);
    }
}
