package tech.finovy.framework.redisson.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import tech.finovy.framework.redis.entity.lock.entity.DistributedLock;
import tech.finovy.framework.redisson.holder.RedisContext;
import tech.finovy.framework.redisson.holder.RedisContextHolder;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class DistributedLockImplTest {

    @Mock
    private RedissonClient mockClient;

    private DistributedLockImpl distributedLockImplUnderTest;

    private AutoCloseable mockitoCloseable;

    @BeforeEach
    void setUp() {
        mockitoCloseable = openMocks(this);
        RedisContextHolder.get().setClient(mockClient);
        distributedLockImplUnderTest = new DistributedLockImpl();
    }

    @AfterEach
    void tearDown() throws Exception {
        mockitoCloseable.close();
    }

    @Test
    void testLock() {
        final RedisContext redisContext = RedisContextHolder.get();
        // Setup
        final DistributedLock lock = new DistributedLock();
        lock.setId(123L);
        lock.setKey("test-lock");
        when(redisContext.createKey(anyString(), any(), anyBoolean())).thenReturn("key");
        final RBucket bucket = mock(RBucket.class);
        when(mockClient.getBucket(anyString(), any(JsonJacksonCodec.class))).thenReturn(bucket);
        // case 1: lock success
        when(bucket.trySet(lock, lock.getLeaseTime(), TimeUnit.MILLISECONDS)).thenReturn(true);
        // Verify the results
        Assertions.assertTrue(distributedLockImplUnderTest.lock(lock).isLocked());
        // case 2: lock fail
        when(bucket.trySet(lock, lock.getLeaseTime(), TimeUnit.MILLISECONDS)).thenReturn(false);
        // Verify the results
        Assertions.assertFalse(distributedLockImplUnderTest.lock(lock).isLocked());
        // case 3: lock exist
        when(bucket.trySet(lock, lock.getLeaseTime(), TimeUnit.MILLISECONDS)).thenReturn(false);
        DistributedLock lockMock = mock(DistributedLock.class);
        when(lockMock.getId()).thenReturn(123L);
        when(bucket.get()).thenReturn(lockMock);
        // Verify the results
        Assertions.assertTrue(distributedLockImplUnderTest.lock(lock).isLocked());
    }

    @Test
    void testErrorBranchLock(){
        final RedisContext redisContext = RedisContextHolder.get();
        final RBucket errorBucket = mock(RBucket.class);
        when(redisContext.createKey(anyString(), any(), anyBoolean())).thenReturn("key");
        when(mockClient.getBucket(anyString(), any(JsonJacksonCodec.class))).thenReturn(errorBucket);
        // exception branch
        final DistributedLock errorLock = new DistributedLock();
        errorLock.setErrMsg("test error branch");
        Assertions.assertFalse(distributedLockImplUnderTest.lock(errorLock).isLocked());
        errorLock.setErrMsg(null);
        errorLock.setId(-1L);
        errorLock.setLeaseTime(0L);
        errorLock.setKey("test-lock");
        when(errorBucket.trySet(errorLock)).thenReturn(true);
        Assertions.assertTrue(distributedLockImplUnderTest.lock(errorLock).isLocked());

        final DistributedLock secondErrorLock = new DistributedLock();
        secondErrorLock.setId(errorLock.getId());
        secondErrorLock.setLeaseTime(0L);
        when(errorBucket.trySet(errorLock)).thenReturn(false);
        when(errorBucket.get()).thenReturn(secondErrorLock);
        Assertions.assertTrue(distributedLockImplUnderTest.lock(errorLock).isLocked());
    }

    @Test
    void testUnlock() {
        final RedisContext redisContext = RedisContextHolder.get();
        // Setup
        final DistributedLock lock = new DistributedLock();
        lock.setId(123L);
        lock.setKey("test-lock");
        when(redisContext.createKey(anyString(), any(), anyBoolean())).thenReturn("key");
        final RBucket bucket = mock(RBucket.class);
        when(mockClient.getBucket(anyString(), any(JsonJacksonCodec.class))).thenReturn(bucket);
        // Run the test
        final DistributedLock resultCase1 = distributedLockImplUnderTest.unlock(lock);
        Assertions.assertNull(resultCase1.getErrMsg());
        // case 2:
        when(bucket.isExists()).thenReturn(true);
        DistributedLock lockMock = mock(DistributedLock.class);
        when(lockMock.getId()).thenReturn(123L);
        when(bucket.get()).thenReturn(lockMock);
        final DistributedLock resultCase2 = distributedLockImplUnderTest.unlock(lock);
        Assertions.assertNull(resultCase2.getErrMsg());
        when(lockMock.getId()).thenReturn(1234L);
        Assertions.assertNull(distributedLockImplUnderTest.unlock(lock).getErrMsg());
    }

    @Test
    void testErrorBranchUnlock(){
        final RedisContext redisContext = RedisContextHolder.get();
        Assertions.assertNotNull(distributedLockImplUnderTest.unlock(null).getErrMsg());
        final DistributedLock errorLock = new DistributedLock();
        Assertions.assertNotNull(distributedLockImplUnderTest.unlock(errorLock).getErrMsg());
        final RBucket errorBucket = mock(RBucket.class);
        when(redisContext.createKey(anyString(), any(), anyBoolean())).thenReturn("key");
        when(mockClient.getBucket(anyString(), any(JsonJacksonCodec.class))).thenReturn(errorBucket);
        // exception branch
        errorLock.setId(-1L);
        errorLock.setKey("test");
        when(errorBucket.get()).thenReturn(null);
        when(errorBucket.isExists()).thenReturn(true);
        Assertions.assertFalse(distributedLockImplUnderTest.unlock(errorLock).isLocked());
    }

    @Test
    void testFinished() {
        final RedisContext redisContext = RedisContextHolder.get();
        // Setup
        final DistributedLock lock = new DistributedLock();
        boolean error = false;
        try {
            distributedLockImplUnderTest.finished(lock);
        } catch (Exception e) {
            error = true;
        }
        Assertions.assertTrue(error);

        lock.setId(123L);
        lock.setKey("test-lock");
        when(redisContext.createKey(anyString(), any(), anyBoolean())).thenReturn("key");
        final RBucket bucket = mock(RBucket.class);
        when(mockClient.getBucket(anyString(), any(JsonJacksonCodec.class))).thenReturn(bucket);
        when(bucket.isExists()).thenReturn(true);
        DistributedLock lockMock = mock(DistributedLock.class);
        when(lockMock.getId()).thenReturn(123L);
        when(bucket.get()).thenReturn(lockMock);
        // Run the test
        final DistributedLock result = distributedLockImplUnderTest.finished(lock);
        Assertions.assertTrue(result.isFinished());
        when(lockMock.getFinishedTimeToLive()).thenReturn(1L);
        final DistributedLock resultBranch = distributedLockImplUnderTest.finished(lock);
        Assertions.assertTrue(resultBranch.isFinished());

        // branch
        final DistributedLock branchLock = new DistributedLock();
        branchLock.setId(123L);
        branchLock.setKey("test-lock");
        when(bucket.isExists()).thenReturn(false);
        // Verify the results
        Assertions.assertFalse(distributedLockImplUnderTest.finished(branchLock).isFinished());
    }
}
