package com.anupam.performance.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

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
