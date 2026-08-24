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

@Component
public class HealthCheckTask {

    private static final Logger log = LoggerFactory.getLogger(HealthCheckTask.class);

    private final HttpClient httpClient;
    private final Map<String, String> serviceStatus = new ConcurrentHashMap<>();
    private LocalDateTime lastRunTime;

    private static final Map<String, String> SERVICES = Map.of(
            "google", "https://www.google.com",
            "github", "https://api.github.com"
    );

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

        SERVICES.forEach((name, url) -> {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .timeout(Duration.ofSeconds(5))
                        .GET()
                        .build();

                HttpResponse<Void> response = httpClient.send(request,
                        HttpResponse.BodyHandlers.discarding());

                String status = response.statusCode() < 400 ? "UP" : "DOWN";
                serviceStatus.put(name, status);
                log.debug("Service {} is {}", name, status);
            } catch (Exception e) {
                serviceStatus.put(name, "DOWN");
                log.warn("Service {} is DOWN: {}", name, e.getMessage());
            }
        });
    }

    public Map<String, String> getServiceStatus() {
        return Map.copyOf(serviceStatus);
    }

    public LocalDateTime getLastRunTime() {
        return lastRunTime;
    }
}
