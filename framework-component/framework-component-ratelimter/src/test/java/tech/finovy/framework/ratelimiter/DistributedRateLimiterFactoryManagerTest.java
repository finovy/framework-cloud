package tech.finovy.framework.ratelimiter;

import com.google.common.util.concurrent.RateLimiter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DistributedRateLimiterFactoryManagerTest {

    @Test
    void testCreateResourceRateLimiter() throws InterruptedException {
        // Setup
        // Run the test
        final DistributedRateLimiterFactoryManager manager = new DistributedRateLimiterFactoryManager();
        final RateLimiter A = manager.createResourceRateLimiter("resource",
                10);
        final RateLimiter B = manager.createResourceRateLimiter("resource",
                10);
        Assertions.assertTrue(A.tryAcquire());
        // RateLimiter's rate is continuous, not discrete. This means it generates tokens at a constant rate, rather than all at once at a specific moment.
        Thread.sleep(200L);
        Assertions.assertTrue(B.tryAcquire());
    }

}
