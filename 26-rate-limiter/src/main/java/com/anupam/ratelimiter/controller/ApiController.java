package com.anupam.ratelimiter.controller;

import com.anupam.ratelimiter.annotation.RateLimit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/public")
    public Map<String, Object> publicEndpoint() {
        return Map.of(
                "message", "This endpoint has no rate limit",
                "timestamp", Instant.now().toString()
        );
    }

    @GetMapping("/limited")
    @RateLimit(requests = 10, seconds = 60)
    public Map<String, Object> limitedEndpoint() {
        return Map.of(
                "message", "This endpoint allows 10 requests per 60 seconds",
                "timestamp", Instant.now().toString()
        );
    }

    @GetMapping("/strict")
    @RateLimit(requests = 3, seconds = 10)
    public Map<String, Object> strictEndpoint() {
        return Map.of(
                "message", "This endpoint allows 3 requests per 10 seconds",
                "timestamp", Instant.now().toString()
        );
    }
}
