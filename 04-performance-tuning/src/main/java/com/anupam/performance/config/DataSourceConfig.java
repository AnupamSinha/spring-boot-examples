package com.anupam.performance.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * Configures HikariCP connection pool with performance-tuned settings.
 *
 * HikariCP is the default connection pool in Spring Boot. This configuration
 * overrides defaults to optimize for a high-throughput application with
 * proper connection lifecycle management and leak detection.
 *
 * @author Anupam
 */
@Configuration
public class DataSourceConfig {

    /**
     * Creates a HikariCP DataSource with tuned pool parameters.
     *
     * Pool settings explained:
     * - maxPoolSize(20): max concurrent connections to the database
     * - minIdle(10): keeps 10 connections warm to avoid cold-start latency
     * - connectionTimeout(20s): how long to wait for a connection from the pool
     * - idleTimeout(5min): idle connections are closed after 5 minutes
     * - maxLifetime(20min): connections are recycled after 20 minutes
     * - leakDetectionThreshold(30s): logs a warning if a connection is held >30s
     */
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.hikari")
    public DataSource dataSource(DataSourceProperties properties) {
        HikariDataSource dataSource = properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();

        // HikariCP Performance Tuning
        dataSource.setMaximumPoolSize(20);
        dataSource.setMinimumIdle(10);
        dataSource.setConnectionTimeout(20000);     // 20 seconds
        dataSource.setIdleTimeout(300000);           // 5 minutes
        dataSource.setMaxLifetime(1200000);          // 20 minutes
        dataSource.setLeakDetectionThreshold(30000); // 30 seconds
        dataSource.setPoolName("HikariPool-Perf");

        // Connection validation
        dataSource.setValidationTimeout(5000);

        return dataSource;
    }
}
