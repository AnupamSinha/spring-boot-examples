package com.anupam.multitenant.config;

/**
 * Thread-local holder for the current tenant identifier.
 * <p>
 * Set by {@link TenantFilter} at the start of each HTTP request and cleared
 * after the request completes. Used by {@link TenantRoutingDataSource} to
 * determine which data source to route to.
 * </p>
 *
 * @author Anupam
 */
public final class TenantContext {

    /** Thread-local storage for the current tenant ID. */
    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    /** Private constructor to prevent instantiation of this utility class. */
    private TenantContext() {
    }

    /**
     * Retrieves the tenant identifier for the current thread.
     *
     * @return the current tenant ID, or {@code null} if not set
     */
    public static String getCurrentTenant() {
        return CURRENT_TENANT.get();
    }

    /**
     * Sets the tenant identifier for the current thread.
     *
     * @param tenantId the tenant ID to associate with the current thread
     */
    public static void setCurrentTenant(String tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    /**
     * Clears the tenant identifier from the current thread.
     * <p>
     * Must be called after request processing to prevent thread-local leaks in pooled threads.
     * </p>
     */
    public static void clear() {
        CURRENT_TENANT.remove();
    }
}
