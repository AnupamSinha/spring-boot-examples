package com.anupam.caching;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Multi-Level Caching Demo - Main Application Entry Point.
 *
 * Demonstrates a two-tier caching strategy:
 * - L1: Caffeine (in-process, fast, limited size, short TTL)
 * - L2: Redis (distributed, shared across instances, longer TTL)
 *
 * Uses Spring's @Cacheable, @CachePut, and @CacheEvict annotations
 * for declarative cache management.
 *
 * @author Anupam
 */
@SpringBootApplication
@EnableCaching
public class CachingApplication {

    public static void main(String[] args) {
        SpringApplication.run(CachingApplication.class, args);
    }
}
