package com.anupam.ratelimiter.exception;

/**
 * Exception thrown when a client exceeds the configured rate limit.
 * <p>
 * Carries metadata about the limit configuration and retry timing,
 * resulting in an HTTP 429 Too Many Requests response.
 * </p>
 *
 * @author Anupam
 */
public class RateLimitExceededException extends RuntimeException {

    private final int maxRequests;
    private final int windowSeconds;
    private final long retryAfterSeconds;

    /**
     * Constructs a new rate limit exceeded exception with limit details.
     *
     * @param maxRequests      the maximum requests allowed in the window
     * @param windowSeconds    the window duration in seconds
     * @param retryAfterSeconds seconds until the client can retry
     */
    public RateLimitExceededException(int maxRequests, int windowSeconds, long retryAfterSeconds) {
        super("Rate limit exceeded: %d requests per %d seconds. Retry after %d seconds."
                .formatted(maxRequests, windowSeconds, retryAfterSeconds));
        this.maxRequests = maxRequests;
        this.windowSeconds = windowSeconds;
        this.retryAfterSeconds = retryAfterSeconds;
    }

    /**
     * Returns the maximum number of requests allowed in the window.
     *
     * @return the request limit
     */
    public int getMaxRequests() {
        return maxRequests;
    }

    /**
     * Returns the time window duration in seconds.
     *
     * @return the window size in seconds
     */
    public int getWindowSeconds() {
        return windowSeconds;
    }

    /**
     * Returns the number of seconds the client should wait before retrying.
     *
     * @return seconds until retry is possible
     */
    public long getRetryAfterSeconds() {
        return retryAfterSeconds;
    }
}
