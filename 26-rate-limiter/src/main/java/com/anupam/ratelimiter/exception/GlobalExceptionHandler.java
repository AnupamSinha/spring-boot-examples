package com.anupam.ratelimiter.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<Map<String, Object>> handleRateLimitExceeded(RateLimitExceededException ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RateLimit-Limit", String.valueOf(ex.getMaxRequests()));
        headers.set("X-RateLimit-Remaining", "0");
        headers.set("X-RateLimit-Window", ex.getWindowSeconds() + "s");
        headers.set("Retry-After", String.valueOf(ex.getRetryAfterSeconds()));

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
