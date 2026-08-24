package com.anupam.ratelimiter.aspect;

import com.anupam.ratelimiter.annotation.RateLimit;
import com.anupam.ratelimiter.exception.RateLimitExceededException;
import com.anupam.ratelimiter.service.RateLimiterService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * AOP aspect that intercepts methods annotated with {@link RateLimit}
 * and enforces per-client rate limiting using the sliding window algorithm.
 * <p>
 * The aspect extracts the client IP address from the incoming HTTP request
 * and delegates the actual rate check to {@link RateLimiterService}.
 * </p>
 *
 * @author Anupam
 */
@Aspect
@Component
public class RateLimitAspect {

    private static final Logger log = LoggerFactory.getLogger(RateLimitAspect.class);

    private final RateLimiterService rateLimiterService;

    /**
     * Constructs the aspect with the required rate limiter service.
     *
     * @param rateLimiterService the service that performs rate limit checks against Redis
     */
    public RateLimitAspect(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    /**
     * Around advice that enforces rate limiting on annotated methods.
     * <p>
     * Builds a unique key from the endpoint name and client IP, then checks
     * whether the request is allowed. If not, throws {@link RateLimitExceededException}.
     * </p>
     *
     * @param joinPoint the intercepted method execution
     * @return the result of the original method if the request is allowed
     * @throws Throwable if the original method throws, or if rate limit is exceeded
     */
    @Around("@annotation(com.anupam.ratelimiter.annotation.RateLimit)")
    public Object enforce(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RateLimit rateLimit = signature.getMethod().getAnnotation(RateLimit.class);

        // Build a unique key combining the endpoint and client identifier
        String clientId = extractClientIp();
        String endpoint = signature.getDeclaringType().getSimpleName() + "." + signature.getName();
        String key = "rate_limit:" + endpoint + ":" + clientId;

        log.debug("Rate limit check — key: {}, limit: {}/{} seconds", key, rateLimit.requests(), rateLimit.seconds());

        // Delegate the sliding window check to the service layer
        boolean allowed = rateLimiterService.isAllowed(key, rateLimit.requests(), rateLimit.seconds());

        if (!allowed) {
            // Calculate retry-after header value before rejecting
            long retryAfter = rateLimiterService.getRetryAfterSeconds(key, rateLimit.seconds());
            throw new RateLimitExceededException(rateLimit.requests(), rateLimit.seconds(), retryAfter);
        }

        return joinPoint.proceed();
    }

    /**
     * Extracts the client IP address from the current HTTP request.
     * <p>
     * Checks proxy headers (X-Forwarded-For, X-Real-IP) before falling
     * back to the remote address reported by the servlet container.
     * </p>
     *
     * @return the client IP address, or "unknown" if no request context is available
     */
    private String extractClientIp() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            return "unknown";
        }

        HttpServletRequest request = attributes.getRequest();

        // Check X-Forwarded-For header (first IP in the chain is the original client)
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }

        // Check X-Real-IP header (set by reverse proxies like Nginx)
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isBlank()) {
            return xRealIp;
        }

        // Fallback to the servlet-reported remote address
        return request.getRemoteAddr();
    }
}
