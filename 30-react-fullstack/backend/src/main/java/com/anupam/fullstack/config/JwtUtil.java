package com.anupam.fullstack.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Utility component for generating and validating JSON Web Tokens (JWT).
 * <p>
 * Uses HMAC-SHA signing with a configurable secret and expiration period.
 * Tokens carry the username as the subject claim.
 * </p>
 *
 * @author Anupam
 */
@Component
public class JwtUtil {

    private final SecretKey key;
    private final long expirationHours;

    /**
     * Constructs the JWT utility with signing secret and expiration configuration.
     *
     * @param secret          the HMAC secret key string (must be at least 256 bits)
     * @param expirationHours the token validity duration in hours (default: 24)
     */
    public JwtUtil(@Value("${app.jwt.secret}") String secret,
                   @Value("${app.jwt.expiration-hours:24}") long expirationHours) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationHours = expirationHours;
    }

    /**
     * Generates a signed JWT token for the given username.
     *
     * @param username the authenticated user's username to embed as subject
     * @return the compact JWT string
     */
    public String generateToken(String username) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expirationHours, ChronoUnit.HOURS)))
                .signWith(key)
                .compact();
    }

    /**
     * Extracts the username (subject) from a JWT token.
     *
     * @param token the JWT token string
     * @return the username embedded in the token
     */
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    /**
     * Validates whether the given token is properly signed and not expired.
     *
     * @param token the JWT token string to validate
     * @return true if the token is valid and not expired, false otherwise
     */
    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractClaims(token);
            // Check that the token has not expired
            return claims.getExpiration().after(Date.from(Instant.now()));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Parses and verifies the JWT token, returning its claims.
     *
     * @param token the JWT token string
     * @return the parsed claims payload
     */
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
