package com.anupam.multitenant.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Servlet filter that extracts the tenant identifier from the X-Tenant-ID header
 * and stores it in {@link TenantContext} for the duration of the request.
 * <p>
 * Runs as the first filter in the chain (Order 1). If the header is missing or blank,
 * the request is rejected with HTTP 400 Bad Request.
 * </p>
 *
 * @author Anupam
 */
@Component
@Order(1)
public class TenantFilter implements Filter {

    /** The HTTP header name used to pass the tenant identifier. */
    private static final String TENANT_HEADER = "X-Tenant-ID";

    /**
     * Extracts the tenant ID from the request header, sets it in the thread-local context,
     * and ensures cleanup after the request completes.
     *
     * @param request  the incoming servlet request
     * @param response the outgoing servlet response
     * @param chain    the filter chain to continue processing
     * @throws IOException      if an I/O error occurs during filtering
     * @throws ServletException if a servlet error occurs during filtering
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String tenantId = httpRequest.getHeader(TENANT_HEADER);

        // Reject requests without a valid tenant header
        if (tenantId == null || tenantId.isBlank()) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            httpResponse.getWriter().write("Missing required header: " + TENANT_HEADER);
            return;
        }

        try {
            // Store tenant in thread-local for downstream data source routing
            TenantContext.setCurrentTenant(tenantId);
            chain.doFilter(request, response);
        } finally {
            // Always clear to prevent thread-local leaks
            TenantContext.clear();
        }
    }
}
