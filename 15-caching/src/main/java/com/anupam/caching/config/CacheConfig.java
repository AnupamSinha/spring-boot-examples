package com.anupam.caching.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Configures a two-tier caching architecture (L1 + L2).
 *
 * L1 (Caffeine): In-process, zero-latency reads, ideal for hot data.
 *   - Short TTL (60s) to limit staleness.
 *   - 500 entry max to bound memory usage.
 *
 * L2 (Redis): Distributed, consistent across all app instances.
 *   - Longer TTL (10 min) for broader cache coverage.
 *   - Survives application restarts.
 *   - JSON serialization for debuggability.
 *
 * @author Anupam
 */
@Configuration
public class CacheConfig implements CachingConfigurer {

    /**
     * L1 Cache - Caffeine (primary, in-process).
     * Fastest possible reads with no network hop.
     */
    @Bean
    @Primary
    public CacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("products", "productList");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(60, TimeUnit.SECONDS)
                .recordStats());
        return cacheManager;
    }

    /**
     * L2 Cache - Redis (distributed).
     * Shared across application instances, survives restarts.
     * Uses JSON serialization for human-readable cache inspection.
     */
    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues();

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .withCacheConfiguration("products",
                        config.entryTtl(Duration.ofMinutes(10)))
                .withCacheConfiguration("productList",
                        config.entryTtl(Duration.ofMinutes(5)))
                .build();
    }
}
