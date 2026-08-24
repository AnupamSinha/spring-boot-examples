package com.anupam.multitenant.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Configures multiple DataSources — one per tenant schema — and wires them
 * into the {@link TenantRoutingDataSource}.
 */
@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String baseUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        TenantRoutingDataSource routingDataSource = new TenantRoutingDataSource();

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("tenant_a", buildDataSource("tenant_a"));
        targetDataSources.put("tenant_b", buildDataSource("tenant_b"));

        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(buildDataSource("tenant_a"));
        routingDataSource.afterPropertiesSet();

        return routingDataSource;
    }

    private DataSource buildDataSource(String schema) {
        String url = baseUrl + "?currentSchema=" + schema;
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}
