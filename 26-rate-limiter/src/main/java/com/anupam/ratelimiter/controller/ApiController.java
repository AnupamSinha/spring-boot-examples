package com.anupam.ratelimiter.controller;

import com.anupam.ratelimiter.annotation.RateLimit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

/**
 * REST controller exposing API endpoints with varying rate limit configurations.
 * <p>
 * Demonstrates how the custom {@link RateLimit} annotation can be applied
 * to individual endpoints with different thresholds.
 * </p>
 *
 * @author Anupam
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    /**
     * Public endpoint with no rate limiting applied.
     *
     * @return a response map containing a message and timestamp
     */
    @GetMapping("/public")
    public Map<String, Object> publicEndpoint() {
        return Map.of(
                "message", "This endpoint has no rate limit",
                "timestamp", Instant.now().toString()
        );
    }

    /**
     * Rate-limited endpoint allowing 10 requests per 60 seconds per client.
     *
     * @return a response map containing a message and timestamp
     */
    @GetMapping("/limited")
    @RateLimit(requests = 10, seconds = 60)
    public Map<String, Object> limitedEndpoint() {
        return Map.of(
                "message", "This endpoint allows 10 requests per 60 seconds",
                "timestamp", Instant.now().toString()
        );
    }

    /**
     * Strictly rate-limited endpoint allowing only 3 requests per 10 seconds per client.
     *
     * @return a response map containing a message and timestamp
     */
    @GetMapping("/strict")
    @RateLimit(requests = 3, seconds = 10)
    public Map<String, Object> strictEndpoint() {
        return Map.of(
                "message", "This endpoint allows 3 requests per 10 seconds",
                "timestamp", Instant.now().toString()
        );
    }
}
