package com.anupam.performance.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Configures Caffeine as the application-level cache.
 *
 * Caffeine is a high-performance, near-optimal caching library for Java.
 * This configuration provides two caches: "products" (for list queries)
 * and "productById" (for single-entity lookups).
 *
 * @author Anupam
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Creates a CaffeineCacheManager with tuned eviction policies.
     * - Entries expire 10 minutes after write (time-based freshness)
     * - Entries expire 5 minutes after last access (LRU behavior)
     * - Stats recording enabled for monitoring cache hit/miss ratios
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("products", "productById");
        cacheManager.setCaffeine(caffeineCacheBuilder());
        cacheManager.setAllowNullValues(false);
        return cacheManager;
    }

    /**
     * Builds the Caffeine cache configuration.
     * Capacity starts at 100 entries and grows up to 500 before eviction kicks in.
     */
    private Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(500)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .recordStats();
    }
}
