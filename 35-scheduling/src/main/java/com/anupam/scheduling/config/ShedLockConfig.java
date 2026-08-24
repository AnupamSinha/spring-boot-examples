package com.anupam.scheduling.config;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Configuration class for ShedLock distributed lock provider.
 * Ensures that scheduled tasks execute at most once across multiple application instances
 * in a clustered environment by using a JDBC-based lock stored in the database.
 *
 * <p>Default lock duration is 10 minutes (lockAtMostFor) to prevent deadlocks
 * in case of unexpected instance failures.</p>
 *
 * @author Anupam
 */
@Configuration
@EnableSchedulerLock(defaultLockAtMostFor = "10m")
public class ShedLockConfig {

    /**
     * Creates a JDBC-based lock provider that stores lock state in the database.
     * Uses database time for lock expiration to avoid clock skew issues across instances.
     *
     * @param dataSource the application's data source for lock table access
     * @return the configured ShedLock lock provider
     */
    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        return new JdbcTemplateLockProvider(
                JdbcTemplateLockProvider.Configuration.builder()
                        .withJdbcTemplate(new org.springframework.jdbc.core.JdbcTemplate(dataSource))
                        // Use database server time instead of application time
                        .usingDbTime()
                        .build()
        );
    }
}
