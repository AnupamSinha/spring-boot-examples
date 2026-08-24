package com.anupam.fullstack.controller;

import com.anupam.fullstack.config.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * REST controller handling user authentication and JWT token issuance.
 * <p>
 * Provides a login endpoint that validates credentials and returns
 * a signed JWT token for subsequent authenticated API calls.
 * </p>
 *
 * @author Anupam
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    /**
     * Constructs the auth controller with authentication dependencies.
     *
     * @param authenticationManager the Spring Security authentication manager
     * @param jwtUtil               the JWT utility for token generation
     */
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Authenticates the user with username/password and returns a JWT token.
     * <p>
     * On success, returns a JSON response containing the token, username, and
     * token type. On failure, returns a 401 Unauthorized response.
     * </p>
     *
     * @param request the login request containing username and password
     * @return a response with JWT token on success, or 401 on failure
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Authenticate using Spring Security's authentication manager
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );
            // Generate JWT token for the authenticated user
            String token = jwtUtil.generateToken(authentication.getName());
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "username", authentication.getName(),
                    "type", "Bearer"
            ));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }

    /**
     * Request DTO for the login endpoint.
     *
     * @param username the user's username
     * @param password the user's password
     */
    public record LoginRequest(String username, String password) {}
}
