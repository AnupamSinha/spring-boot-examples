package com.anupam.ratelimiter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

/**
 * Rate limiter implementation using Redis sorted sets (sliding window log algorithm).
 * <p>
 * Algorithm:
 * <ol>
 *   <li>Use a sorted set per client+endpoint combination</li>
 *   <li>Score = timestamp of the request</li>
 *   <li>Member = unique request ID (to avoid collisions for same-millisecond requests)</li>
 *   <li>On each request:
 *     <ul>
 *       <li>Remove entries older than the window (ZREMRANGEBYSCORE)</li>
 *       <li>Count remaining entries (ZCARD)</li>
 *       <li>If count &lt; limit, add new entry (ZADD) and allow</li>
 *       <li>If count &gt;= limit, reject with 429</li>
 *     </ul>
 *   </li>
 *   <li>Set TTL on the key to auto-cleanup</li>
 * </ol>
 * </p>
 *
 * @author Anupam
 */
@Service
public class RateLimiterService {

    private static final Logger log = LoggerFactory.getLogger(RateLimiterService.class);

    private final ZSetOperations<String, String> zSetOps;
    private final StringRedisTemplate redisTemplate;

    /**
     * Constructs the service with a Redis template for sorted set operations.
     *
     * @param redisTemplate the Spring Redis template for string-based operations
     */
    public RateLimiterService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.zSetOps = redisTemplate.opsForZSet();
    }

    /**
     * Check if a request is allowed under the rate limit.
     * <p>
     * Performs the sliding window check: removes expired entries, counts current
     * entries, and either admits or rejects the request.
     * </p>
     *
     * @param key           unique key for the client + endpoint combination
     * @param maxRequests   maximum requests allowed in the window
     * @param windowSeconds time window in seconds
     * @return true if the request is allowed, false if rate limit exceeded
     */
    public boolean isAllowed(String key, int maxRequests, int windowSeconds) {
        long now = Instant.now().toEpochMilli();
        long windowStart = now - (windowSeconds * 1000L);

        // Remove expired entries outside the sliding window
        zSetOps.removeRangeByScore(key, 0, windowStart);

        // Count current requests in the window
        Long currentCount = zSetOps.zCard(key);

        if (currentCount != null && currentCount >= maxRequests) {
            log.info("Rate limit exceeded for key: {} ({}/{})", key, currentCount, maxRequests);
            return false;
        }

        // Add the current request with timestamp as score and a unique member value
        String member = now + ":" + UUID.randomUUID().toString().substring(0, 8);
        zSetOps.add(key, member, now);

        // Set TTL slightly longer than the window to auto-cleanup stale keys
        redisTemplate.expire(key, Duration.ofSeconds(windowSeconds + 10));

        log.debug("Request allowed for key: {} ({}/{})", key, (currentCount != null ? currentCount + 1 : 1), maxRequests);
        return true;
    }

    /**
     * Calculate how many seconds until the client can make another request.
     * <p>
     * Looks at the oldest entry in the current window to determine when it
     * will expire and free up a slot.
     * </p>
     *
     * @param key           the rate limit key
     * @param windowSeconds the window size in seconds
     * @return seconds until the oldest entry in the window expires
     */
    public long getRetryAfterSeconds(String key, int windowSeconds) {
        var oldest = zSetOps.rangeWithScores(key, 0, 0);

        if (oldest == null || oldest.isEmpty()) {
            return windowSeconds;
        }

        // Extract the oldest timestamp from the sorted set score
        double oldestScore = oldest.iterator().next().getScore();
        long oldestTimestamp = (long) oldestScore;
        long now = Instant.now().toEpochMilli();
        long windowMs = windowSeconds * 1000L;
        long retryAfterMs = (oldestTimestamp + windowMs) - now;

        // Ensure at least 1 second is returned
        return Math.max(1, (retryAfterMs / 1000) + 1);
    }

    /**
     * Get the remaining number of requests allowed in the current window.
     *
     * @param key           the rate limit key
     * @param maxRequests   the maximum requests allowed
     * @param windowSeconds the window size in seconds
     * @return number of remaining requests the client can still make
     */
    public long getRemainingRequests(String key, int maxRequests, int windowSeconds) {
        long now = Instant.now().toEpochMilli();
        long windowStart = now - (windowSeconds * 1000L);

        // Clean up expired entries before counting
        zSetOps.removeRangeByScore(key, 0, windowStart);
        Long currentCount = zSetOps.zCard(key);

        return Math.max(0, maxRequests - (currentCount != null ? currentCount : 0));
    }
}
