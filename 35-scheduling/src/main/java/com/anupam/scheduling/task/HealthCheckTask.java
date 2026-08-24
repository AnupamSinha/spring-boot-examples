package com.anupam.scheduling.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Scheduled task that periodically checks the health of external services.
 * Uses fixedDelay scheduling (30 seconds after previous completion) and does not
 * use ShedLock, allowing each instance to independently monitor service health.
 *
 * <p>Maintains a live status map of monitored services (UP/DOWN) accessible
 * via the controller for status reporting.</p>
 *
 * @author Anupam
 */
@Component
public class HealthCheckTask {

    private static final Logger log = LoggerFactory.getLogger(HealthCheckTask.class);

    private final HttpClient httpClient;

    /** Thread-safe map tracking the current health status of each monitored service. */
    private final Map<String, String> serviceStatus = new ConcurrentHashMap<>();
    private LocalDateTime lastRunTime;

    /** Map of service names to their health check URLs. */
    private static final Map<String, String> SERVICES = Map.of(
            "google", "https://www.google.com",
            "github", "https://api.github.com"
    );

    /**
     * Constructs the HealthCheckTask with an HTTP client configured
     * for a 5-second connection timeout.
     */
    public HealthCheckTask() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    /**
     * Runs every 30 seconds (fixedDelay = 30000).
     * fixedDelay means the next execution starts 30s after the PREVIOUS COMPLETION.
     * No ShedLock — each instance checks independently.
     */
    @Scheduled(fixedDelay = 30000)
    public void checkExternalServices() {
        log.debug("Running health checks...");
        lastRunTime = LocalDateTime.now();

        // Check each configured service and update its status
        SERVICES.forEach((name, url) -> {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .timeout(Duration.ofSeconds(5))
                        .GET()
                        .build();

                HttpResponse<Void> response = httpClient.send(request,
                        HttpResponse.BodyHandlers.discarding());

                // Consider any status < 400 as healthy
                String status = response.statusCode() < 400 ? "UP" : "DOWN";
                serviceStatus.put(name, status);
                log.debug("Service {} is {}", name, status);
            } catch (Exception e) {
                // Any exception (timeout, connection refused, etc.) marks service as DOWN
                serviceStatus.put(name, "DOWN");
                log.warn("Service {} is DOWN: {}", name, e.getMessage());
            }
        });
    }

    /**
     * Returns an immutable copy of the current service health status map.
     *
     * @return map of service names to their status (UP or DOWN)
     */
    public Map<String, String> getServiceStatus() {
        return Map.copyOf(serviceStatus);
    }

    /**
     * Returns the timestamp of the last health check execution.
     *
     * @return the last run time, or null if never executed
     */
    public LocalDateTime getLastRunTime() {
        return lastRunTime;
    }
}
