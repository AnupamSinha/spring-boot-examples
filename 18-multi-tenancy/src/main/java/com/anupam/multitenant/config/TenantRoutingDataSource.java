package com.anupam.multitenant.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Routes database connections to the appropriate tenant-specific DataSource
 * based on the current value in {@link TenantContext}.
 * <p>
 * Extends Spring's {@link AbstractRoutingDataSource} to dynamically resolve
 * the lookup key from the thread-local tenant identifier.
 * </p>
 *
 * @author Anupam
 */
public class TenantRoutingDataSource extends AbstractRoutingDataSource {

    /**
     * Determines the current tenant lookup key used to select the target DataSource.
     *
     * @return the tenant identifier for the current thread, or {@code null} if not set
     */
    @Override
    protected Object determineCurrentLookupKey() {
        // Delegate to TenantContext which holds the tenant ID for the current request
        return TenantContext.getCurrentTenant();
    }
}
