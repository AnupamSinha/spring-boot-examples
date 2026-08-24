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

@Aspect
@Component
public class RateLimitAspect {

    private static final Logger log = LoggerFactory.getLogger(RateLimitAspect.class);

    private final RateLimiterService rateLimiterService;

    public RateLimitAspect(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @Around("@annotation(com.anupam.ratelimiter.annotation.RateLimit)")
    public Object enforce(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RateLimit rateLimit = signature.getMethod().getAnnotation(RateLimit.class);

        String clientId = extractClientIp();
        String endpoint = signature.getDeclaringType().getSimpleName() + "." + signature.getName();
        String key = "rate_limit:" + endpoint + ":" + clientId;

        log.debug("Rate limit check — key: {}, limit: {}/{} seconds", key, rateLimit.requests(), rateLimit.seconds());

        boolean allowed = rateLimiterService.isAllowed(key, rateLimit.requests(), rateLimit.seconds());

        if (!allowed) {
            long retryAfter = rateLimiterService.getRetryAfterSeconds(key, rateLimit.seconds());
            throw new RateLimitExceededException(rateLimit.requests(), rateLimit.seconds(), retryAfter);
        }

        return joinPoint.proceed();
    }

    private String extractClientIp() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            return "unknown";
        }

        HttpServletRequest request = attributes.getRequest();

        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isBlank()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }
}
