package com.anupam.ratelimiter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to apply rate limiting to a method.
 * <p>
 * Uses a sliding window algorithm backed by Redis sorted sets.
 * Place this annotation on controller methods to enforce per-client
 * request limits within a configurable time window.
 * </p>
 *
 * @author Anupam
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    /**
     * Maximum number of requests allowed within the time window.
     *
     * @return the request limit
     */
    int requests();

    /**
     * Time window in seconds during which the request limit applies.
     *
     * @return the window duration in seconds
     */
    int seconds();
}
