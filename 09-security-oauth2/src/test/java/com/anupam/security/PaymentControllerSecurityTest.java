package com.anupam.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Security integration tests verifying JWT-based access control.
 *
 * Uses Spring Security's jwt() RequestPostProcessor to simulate JWT tokens
 * with different claims and roles without needing a real OAuth2 provider.
 *
 * @author Anupam
 */
@SpringBootTest
@AutoConfigureMockMvc
class PaymentControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    /** Public endpoint should be accessible without any authentication. */
    @Test
    void shouldAllowPublicEndpointWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/public/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    /** Protected endpoints should reject requests without a valid JWT. */
    @Test
    void shouldRejectUnauthenticatedRequest() throws Exception {
        mockMvc.perform(get("/api/payments"))
                .andExpect(status().isUnauthorized());
    }

    /** A USER-role JWT should grant access to the payments endpoint. */
    @Test
    void shouldAllowUserWithJwt() throws Exception {
        mockMvc.perform(get("/api/payments")
                .with(SecurityMockMvcRequestPostProcessors.jwt()
                        .jwt(jwt -> jwt
                                .claim("preferred_username", "alice")
                                .claim("realm_access", Map.of("roles", List.of("USER"))))))
                .andExpect(status().isOk());
    }

    /** A USER-role JWT should be denied access to admin endpoints. */
    @Test
    void shouldDenyUserFromAdminEndpoint() throws Exception {
        mockMvc.perform(get("/api/admin/users")
                .with(SecurityMockMvcRequestPostProcessors.jwt()
                        .jwt(jwt -> jwt
                                .claim("preferred_username", "bob")
                                .claim("realm_access", Map.of("roles", List.of("USER"))))))
                .andExpect(status().isForbidden());
    }

    /** An ADMIN-role JWT should grant access to admin endpoints. */
    @Test
    void shouldAllowAdminToAccessAdminEndpoint() throws Exception {
        mockMvc.perform(get("/api/admin/users")
                .with(SecurityMockMvcRequestPostProcessors.jwt()
                        .jwt(jwt -> jwt
                                .claim("preferred_username", "alice")
                                .claim("realm_access", Map.of("roles", List.of("ADMIN"))))))
                .andExpect(status().isOk());
    }

    /** The /me endpoint should return claims from the authenticated JWT. */
    @Test
    void shouldReturnCurrentUserInfo() throws Exception {
        mockMvc.perform(get("/api/me")
                .with(SecurityMockMvcRequestPostProcessors.jwt()
                        .jwt(jwt -> jwt
                                .claim("preferred_username", "alice")
                                .claim("email", "alice@example.com")
                                .claim("realm_access", Map.of("roles", List.of("ADMIN", "USER"))))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }
}
