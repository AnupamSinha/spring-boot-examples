package com.anupam.performance.controller;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.search.Search;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Exposes a custom metrics summary endpoint for monitoring application performance.
 *
 * Aggregates Micrometer metrics including:
 * - Service-layer timers (cached, uncached, async)
 * - Operation counters (getById, create)
 * - JVM metrics (memory, threads)
 * - HikariCP connection pool stats
 *
 * @author Anupam
 */
@RestController
@RequestMapping("/api/metrics")
public class MetricsController {

    private final MeterRegistry meterRegistry;

    public MetricsController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    /**
     * GET /api/metrics/summary - Returns a structured overview of key performance metrics.
     * Useful for dashboards and quick health checks during load testing.
     */
    @GetMapping("/summary")
    public Map<String, Object> getMetricsSummary() {
        Map<String, Object> summary = new HashMap<>();

        // Timer metrics - measure latency of service operations
        Map<String, Object> timers = new HashMap<>();
        addTimerMetric(timers, "product.service.getAll", "Product GetAll (Cached)");
        addTimerMetric(timers, "product.service.getAll.uncached", "Product GetAll (Uncached)");
        addTimerMetric(timers, "product.service.getAll.async", "Product GetAll (Async)");
        summary.put("timers", timers);

        // Counter metrics - track call frequency
        Map<String, Object> counters = new HashMap<>();
        addCounterMetric(counters, "product.service.getById.calls", "GetById Calls");
        addCounterMetric(counters, "product.service.create.calls", "Create Calls");
        summary.put("counters", counters);

        // JVM metrics - memory and thread usage
        Map<String, Object> jvm = new HashMap<>();
        addGaugeMetric(jvm, "jvm.memory.used", "Memory Used (bytes)");
        addGaugeMetric(jvm, "jvm.threads.live", "Live Threads");
        summary.put("jvm", jvm);

        // HikariCP metrics - connection pool health
        Map<String, Object> hikari = new HashMap<>();
        addGaugeMetric(hikari, "hikaricp.connections.active", "Active Connections");
        addGaugeMetric(hikari, "hikaricp.connections.idle", "Idle Connections");
        addGaugeMetric(hikari, "hikaricp.connections.pending", "Pending Connections");
        summary.put("hikaricp", hikari);

        return summary;
    }

    /** Extracts timer statistics (count, total, mean, max) for a named meter. */
    private void addTimerMetric(Map<String, Object> map, String meterName, String displayName) {
        Timer timer = meterRegistry.find(meterName).timer();
        if (timer != null) {
            map.put(displayName, Map.of(
                    "count", timer.count(),
                    "totalTimeMs", timer.totalTime(TimeUnit.MILLISECONDS),
                    "meanMs", timer.mean(TimeUnit.MILLISECONDS),
                    "maxMs", timer.max(TimeUnit.MILLISECONDS)
            ));
        }
    }

    /** Extracts the current count for a named counter. */
    private void addCounterMetric(Map<String, Object> map, String meterName, String displayName) {
        Counter counter = meterRegistry.find(meterName).counter();
        if (counter != null) {
            map.put(displayName, counter.count());
        }
    }

    /** Extracts the current value for a named gauge. */
    private void addGaugeMetric(Map<String, Object> map, String meterName, String displayName) {
        var gauges = Search.in(meterRegistry).name(meterName).gauges();
        gauges.forEach(gauge -> map.put(displayName, gauge.value()));
    }
}
