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
 * <p>
 * Each tenant is mapped to an isolated PostgreSQL schema. The routing data source
 * dynamically selects the appropriate schema based on the current tenant context.
 * </p>
 *
 * @author Anupam
 */
@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String baseUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    /**
     * Creates the routing data source bean that delegates to tenant-specific data sources.
     * <p>
     * Registers data sources for each known tenant and sets a default fallback.
     * </p>
     *
     * @return a {@link TenantRoutingDataSource} configured with per-tenant data sources
     */
    @Bean
    public DataSource dataSource() {
        TenantRoutingDataSource routingDataSource = new TenantRoutingDataSource();

        // Register a data source for each tenant schema
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("tenant_a", buildDataSource("tenant_a"));
        targetDataSources.put("tenant_b", buildDataSource("tenant_b"));

        routingDataSource.setTargetDataSources(targetDataSources);
        // Default to tenant_a if no tenant is specified
        routingDataSource.setDefaultTargetDataSource(buildDataSource("tenant_a"));
        routingDataSource.afterPropertiesSet();

        return routingDataSource;
    }

    /**
     * Builds a data source configured for a specific tenant schema.
     *
     * @param schema the database schema name to connect to
     * @return a configured {@link DataSource} targeting the given schema
     */
    private DataSource buildDataSource(String schema) {
        // Append the schema parameter to the base JDBC URL
        String url = baseUrl + "?currentSchema=" + schema;
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}
