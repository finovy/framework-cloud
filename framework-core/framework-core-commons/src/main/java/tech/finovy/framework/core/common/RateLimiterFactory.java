package tech.finovy.framework.core.common;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dtype.huang
 */

public class RateLimiterFactory {
    private static final ConcurrentHashMap<String, RateLimiter> RESOURCE_RATE_LIMITER = new ConcurrentHashMap<>();

    public static RateLimiter createResourceRateLimiter(String resource, double qps) {
        if (RESOURCE_RATE_LIMITER.contains(resource)) {
            RateLimiter rateLimiter = RESOURCE_RATE_LIMITER.get(resource);
            rateLimiter.setRate(qps);
            return rateLimiter;
        } else {
            RateLimiter rateLimiter = RateLimiter.create(qps);
            RESOURCE_RATE_LIMITER.putIfAbsent(resource, rateLimiter);
        }
        return RESOURCE_RATE_LIMITER.get(resource);
    }

    public static boolean tryAcquire(String resource, double qps) {
        RateLimiter limiter = createResourceRateLimiter(resource, qps);
        return limiter.tryAcquire();
    }
}
