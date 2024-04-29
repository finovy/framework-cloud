package tech.finovy.framework.redisson.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import tech.finovy.framework.redisson.holder.RedisContext;
import tech.finovy.framework.redisson.holder.RedisContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class DistributedIdImplTest {

    @Mock
    private RedissonClient mockClient;

    private DistributedIdImpl distributedIdImplUnderTest;

    private AutoCloseable mockitoCloseable;

    @BeforeEach
    void setUp() {
        mockitoCloseable = openMocks(this);
        RedisContextHolder.get().setClient(mockClient);
        distributedIdImplUnderTest = new DistributedIdImpl();
    }

    @AfterEach
    void tearDown() throws Exception {
        mockitoCloseable.close();
    }

    @Test
    void testIncrementAndGet() {
        final RedisContext redisContext = RedisContextHolder.get();
        // Setup
        final RAtomicLong redissonAtomicLong = Mockito.mock(RAtomicLong.class);
        when(redisContext.createKey("key", "ID")).thenReturn("name");
        when(mockClient.getAtomicLong("name")).thenReturn(redissonAtomicLong);
        when(redissonAtomicLong.getAndIncrement()).thenReturn(1L);
        // Run the test
        final long result = distributedIdImplUnderTest.incrementAndGet("key");
        // Verify the results
        assertEquals(1L, result);
    }

    @Test
    void testSet() {
        final RedisContext redisContext = RedisContextHolder.get();
        // Setup
        final RAtomicLong redissonAtomicLong = Mockito.mock(RAtomicLong.class);
        when(redisContext.createKey("key", "ID")).thenReturn("name");
        when(mockClient.getAtomicLong("name")).thenReturn(redissonAtomicLong);
        // Run the test
        distributedIdImplUnderTest.set("key", 0L);
        // Verify the results
    }
}
