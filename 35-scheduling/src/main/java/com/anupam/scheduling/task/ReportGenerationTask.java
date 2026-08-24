package com.anupam.scheduling.task;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class ReportGenerationTask {

    private static final Logger log = LoggerFactory.getLogger(ReportGenerationTask.class);
    private final AtomicReference<LocalDateTime> lastRunTime = new AtomicReference<>();

    /**
     * Runs daily at 2:00 AM. ShedLock ensures only one instance executes
     * in a clustered environment.
     */
    @Scheduled(cron = "0 0 2 * * *")
    @SchedulerLock(
            name = "reportGeneration",
            lockAtLeastFor = "5m",
            lockAtMostFor = "30m"
    )
    public void generateDailyReport() {
        log.info("Starting daily report generation...");
        lastRunTime.set(LocalDateTime.now());

        try {
            // Simulate report generation work
            Thread.sleep(5000);
            log.info("Daily report generated successfully at {}", lastRunTime.get());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Report generation interrupted", e);
        }
    }

    public LocalDateTime getLastRunTime() {
        return lastRunTime.get();
    }
}
