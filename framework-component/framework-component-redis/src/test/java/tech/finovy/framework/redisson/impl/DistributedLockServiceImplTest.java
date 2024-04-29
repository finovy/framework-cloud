package tech.finovy.framework.redisson.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import tech.finovy.framework.redis.entity.lock.entity.DistributedLock;
import tech.finovy.framework.redisson.api.DistributedLockApi;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class DistributedLockServiceImplTest {

    @Mock
    private DistributedLockApi mockDistributedLockApi;

    private DistributedLockServiceImpl distributedLockServiceImplUnderTest;

    private AutoCloseable mockitoCloseable;

    @BeforeEach
    void setUp() {
        mockitoCloseable = openMocks(this);
        distributedLockServiceImplUnderTest = new DistributedLockServiceImpl(mockDistributedLockApi);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockitoCloseable.close();
    }

    @Test
    void testLock() {
        // Setup
        final DistributedLock lock = new DistributedLock("key", false, 0L, 0L, 0L, false, false, false, 0L, false,
                "info", "errMsg");
        // Configure DistributedLockApi.lock(...).
        final DistributedLock distributedLock = new DistributedLock("key", false, 0L, 0L, 0L, false, true, false, 0L,
                false, "info", "errMsg");
        when(mockDistributedLockApi.lock(any(DistributedLock.class))).thenReturn(distributedLock);
        // Run the test
        final DistributedLock result = distributedLockServiceImplUnderTest.lock(lock);
        Assertions.assertTrue(result.isLocked());
        // Verify the results
    }

    @Test
    void testUnlock() {
        // Setup
        final DistributedLock lock = new DistributedLock("key", false, 0L, 0L, 0L, false, false, false, 0L, false,
                "info", "errMsg");

        // Configure DistributedLockApi.unlock(...).
        final DistributedLock distributedLock = new DistributedLock("key", false, 0L, 0L, 0L, false, false, true, 0L,
                false, "info", "errMsg");
        when(mockDistributedLockApi.unlock(any(DistributedLock.class))).thenReturn(distributedLock);
        // Run the test
        final DistributedLock result = distributedLockServiceImplUnderTest.unlock(lock);
        // Verify the results
        Assertions.assertTrue(result.isFinished());

        // error branch
        when(mockDistributedLockApi.unlock(any(DistributedLock.class))).thenThrow(new RuntimeException("for test"));
        Assertions.assertNotNull(distributedLockServiceImplUnderTest.unlock(lock).getErrMsg());
    }

    @Test
    void testFinished() {
        // Setup
        final DistributedLock lock = new DistributedLock("key", false, 0L, 0L, 0L, false, false, false, 0L, false,
                "info", "errMsg");
        // Configure DistributedLockApi.finished(...).
        final DistributedLock distributedLock = new DistributedLock("key", false, 0L, 0L, 0L, false, false, true, 0L,
                false, "info", "errMsg");
        when(mockDistributedLockApi.finished(any(DistributedLock.class))).thenReturn(distributedLock);
        // Run the test
        final DistributedLock result = distributedLockServiceImplUnderTest.finished(lock);
        // Verify the results
        Assertions.assertTrue(result.isFinished());

    }
}
