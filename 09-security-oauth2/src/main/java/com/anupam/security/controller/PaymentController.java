package com.anupam.security.controller;

import com.anupam.security.model.PaymentInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * REST controller demonstrating role-based endpoint access with JWT authentication.
 *
 * Endpoints:
 * - /api/public/health : open to all (no auth required)
 * - /api/payments      : requires USER or ADMIN role
 * - /api/admin/users   : requires ADMIN role (method-level @PreAuthorize)
 * - /api/me            : returns current user's JWT claims
 *
 * @author Anupam
 */
@RestController
@RequestMapping("/api")
public class PaymentController {

    /** Public health check endpoint - no authentication needed. */
    @GetMapping("/public/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "service", "spring-security-oauth2-demo");
    }

    /**
     * Returns payments for the authenticated user.
     * Extracts the username from the JWT "preferred_username" claim.
     */
    @GetMapping("/payments")
    public ResponseEntity<List<PaymentInfo>> getPayments(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getClaimAsString("preferred_username");
        List<PaymentInfo> payments = List.of(
                new PaymentInfo("TXN-001", new BigDecimal("250.00"), "COMPLETED", username, LocalDateTime.now()),
                new PaymentInfo("TXN-002", new BigDecimal("89.99"), "PENDING", username, LocalDateTime.now())
        );
        return ResponseEntity.ok(payments);
    }

    /**
     * Admin-only endpoint. Protected with @PreAuthorize for method-level security.
     * Returns a list of all system users.
     */
    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, String>>> getAllUsers() {
        return ResponseEntity.ok(List.of(
                Map.of("username", "alice", "role", "ADMIN"),
                Map.of("username", "bob", "role", "USER")
        ));
    }

    /**
     * Returns the authenticated user's profile information extracted from the JWT.
     * Includes username, email, roles, and token expiry.
     */
    @GetMapping("/me")
    public Map<String, Object> currentUser(@AuthenticationPrincipal Jwt jwt) {
        return Map.of(
                "username", jwt.getClaimAsString("preferred_username"),
                "email", jwt.getClaimAsString("email") != null ? jwt.getClaimAsString("email") : "N/A",
                "roles", jwt.getClaimAsMap("realm_access"),
                "tokenExpiry", jwt.getExpiresAt()
        );
    }
}
