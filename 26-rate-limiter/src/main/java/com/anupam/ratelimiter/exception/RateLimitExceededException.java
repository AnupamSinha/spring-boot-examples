package com.anupam.ratelimiter.exception;

/**
 * Exception thrown when a client exceeds the configured rate limit.
 * Results in an HTTP 429 Too Many Requests response.
 */
public class RateLimitExceededException extends RuntimeException {

    private final int maxRequests;
    private final int windowSeconds;
    private final long retryAfterSeconds;

    public RateLimitExceededException(int maxRequests, int windowSeconds, long retryAfterSeconds) {
        super("Rate limit exceeded: %d requests per %d seconds. Retry after %d seconds."
                .formatted(maxRequests, windowSeconds, retryAfterSeconds));
        this.maxRequests = maxRequests;
        this.windowSeconds = windowSeconds;
        this.retryAfterSeconds = retryAfterSeconds;
    }

    public int getMaxRequests() {
        return maxRequests;
    }

    public int getWindowSeconds() {
        return windowSeconds;
    }

    public long getRetryAfterSeconds() {
        return retryAfterSeconds;
    }
}
