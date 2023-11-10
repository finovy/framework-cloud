package tech.finovy.framework.redisson.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.redisson.api.RAtomicLong;
import tech.finovy.framework.redisson.client.RedissonClientInterface;
import tech.finovy.framework.redisson.holder.RedisContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class DistributedIdImplTest {

    @Mock
    private RedissonClientInterface mockClient;

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
        // Setup
        final RAtomicLong redissonAtomicLong = Mockito.mock(RAtomicLong.class);
        when(mockClient.createKey("key", "ID")).thenReturn("name");
        when(mockClient.getAtomicLong("name")).thenReturn(redissonAtomicLong);
        when(redissonAtomicLong.getAndIncrement()).thenReturn(1L);
        // Run the test
        final long result = distributedIdImplUnderTest.incrementAndGet("key");
        // Verify the results
        assertEquals(1L, result);
    }

    @Test
    void testSet() {
        // Setup
        final RAtomicLong redissonAtomicLong = Mockito.mock(RAtomicLong.class);
        when(mockClient.createKey("key", "ID")).thenReturn("name");
        when(mockClient.getAtomicLong("name")).thenReturn(redissonAtomicLong);
        // Run the test
        distributedIdImplUnderTest.set("key", 0L);
        // Verify the results
    }
}
