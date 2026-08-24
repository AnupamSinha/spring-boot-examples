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
 *
 * Algorithm:
 * 1. Use a sorted set per client+endpoint combination
 * 2. Score = timestamp of the request
 * 3. Member = unique request ID (to avoid collisions for same-millisecond requests)
 * 4. On each request:
 *    a. Remove entries older than the window (ZREMRANGEBYSCORE)
 *    b. Count remaining entries (ZCARD)
 *    c. If count < limit, add new entry (ZADD) and allow
 *    d. If count >= limit, reject with 429
 * 5. Set TTL on the key to auto-cleanup
 */
@Service
public class RateLimiterService {

    private static final Logger log = LoggerFactory.getLogger(RateLimiterService.class);

    private final ZSetOperations<String, String> zSetOps;
    private final StringRedisTemplate redisTemplate;

    public RateLimiterService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.zSetOps = redisTemplate.opsForZSet();
    }

    /**
     * Check if a request is allowed under the rate limit.
     *
     * @param key      unique key for the client + endpoint combination
     * @param maxRequests maximum requests allowed in the window
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

        // Add the current request with timestamp as score
        String member = now + ":" + UUID.randomUUID().toString().substring(0, 8);
        zSetOps.add(key, member, now);

        // Set TTL slightly longer than the window to auto-cleanup
        redisTemplate.expire(key, Duration.ofSeconds(windowSeconds + 10));

        log.debug("Request allowed for key: {} ({}/{})", key, (currentCount != null ? currentCount + 1 : 1), maxRequests);
        return true;
    }

    /**
     * Calculate how many seconds until the client can make another request.
     *
     * @param key          the rate limit key
     * @param windowSeconds the window size in seconds
     * @return seconds until the oldest entry in the window expires
     */
    public long getRetryAfterSeconds(String key, int windowSeconds) {
        var oldest = zSetOps.rangeWithScores(key, 0, 0);

        if (oldest == null || oldest.isEmpty()) {
            return windowSeconds;
        }

        double oldestScore = oldest.iterator().next().getScore();
        long oldestTimestamp = (long) oldestScore;
        long now = Instant.now().toEpochMilli();
        long windowMs = windowSeconds * 1000L;
        long retryAfterMs = (oldestTimestamp + windowMs) - now;

        return Math.max(1, (retryAfterMs / 1000) + 1);
    }

    /**
     * Get the remaining number of requests allowed in the current window.
     *
     * @param key          the rate limit key
     * @param maxRequests  the maximum requests allowed
     * @param windowSeconds the window size in seconds
     * @return number of remaining requests
     */
    public long getRemainingRequests(String key, int maxRequests, int windowSeconds) {
        long now = Instant.now().toEpochMilli();
        long windowStart = now - (windowSeconds * 1000L);

        zSetOps.removeRangeByScore(key, 0, windowStart);
        Long currentCount = zSetOps.zCard(key);

        return Math.max(0, maxRequests - (currentCount != null ? currentCount : 0));
    }
}
