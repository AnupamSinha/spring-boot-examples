package com.anupam.ratelimiter.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

/**
 * Global exception handler that translates application exceptions
 * into structured HTTP error responses.
 * <p>
 * Handles {@link RateLimitExceededException} by returning a 429 response
 * with standard rate limit headers (X-RateLimit-Limit, Retry-After, etc.).
 * </p>
 *
 * @author Anupam
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles rate limit exceeded exceptions by building a 429 response
     * with informative headers and a JSON error body.
     *
     * @param ex the rate limit exceeded exception
     * @return a ResponseEntity with HTTP 429 status, rate limit headers, and error details
     */
    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<Map<String, Object>> handleRateLimitExceeded(RateLimitExceededException ex) {
        // Set standard rate limit response headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RateLimit-Limit", String.valueOf(ex.getMaxRequests()));
        headers.set("X-RateLimit-Remaining", "0");
        headers.set("X-RateLimit-Window", ex.getWindowSeconds() + "s");
        headers.set("Retry-After", String.valueOf(ex.getRetryAfterSeconds()));

        // Build the JSON error response body
        Map<String, Object> body = Map.of(
                "status", 429,
                "error", "Too Many Requests",
                "message", ex.getMessage(),
                "retryAfter", ex.getRetryAfterSeconds(),
                "timestamp", Instant.now().toString()
        );

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .headers(headers)
                .body(body);
    }
}
