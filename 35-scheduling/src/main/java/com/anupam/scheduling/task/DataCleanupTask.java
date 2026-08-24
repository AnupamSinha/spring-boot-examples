package com.anupam.scheduling.task;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Scheduled task that performs periodic cleanup of stale data records.
 * Runs every hour using fixedRate scheduling and is protected by ShedLock
 * to ensure only one instance executes in a clustered environment.
 *
 * <p>The lock is held for at least 5 minutes and at most 55 minutes to prevent
 * concurrent execution while allowing recovery from failed instances.</p>
 *
 * @author Anupam
 */
@Component
public class DataCleanupTask {

    private static final Logger log = LoggerFactory.getLogger(DataCleanupTask.class);

    /** Thread-safe reference tracking the last successful execution time. */
    private final AtomicReference<LocalDateTime> lastRunTime = new AtomicReference<>();

    /**
     * Runs every hour (3600000 ms). Cleans up records older than 30 days.
     * fixedRate means the next execution starts 1 hour after the PREVIOUS START.
     */
    @Scheduled(fixedRate = 3600000)
    @SchedulerLock(
            name = "dataCleanup",
            lockAtLeastFor = "5m",
            lockAtMostFor = "55m"
    )
    public void cleanupOldRecords() {
        log.info("Starting data cleanup task...");
        lastRunTime.set(LocalDateTime.now());

        // Simulate cleanup of old records (older than 30 days)
        int deletedCount = performCleanup();
        log.info("Data cleanup completed. Deleted {} old records.", deletedCount);
    }

    /**
     * Performs the actual cleanup logic. In a production application, this would
     * execute a database DELETE for records older than 30 days.
     *
     * @return the number of records deleted
     */
    private int performCleanup() {
        // In a real app, this would delete from a database:
        // DELETE FROM audit_log WHERE created_at < NOW() - INTERVAL '30 days'
        try {
            Thread.sleep(2000); // simulate work
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return 42; // simulated count
    }

    /**
     * Returns the timestamp of the last successful task execution.
     *
     * @return the last run time, or null if the task has never executed
     */
    public LocalDateTime getLastRunTime() {
        return lastRunTime.get();
    }
}
