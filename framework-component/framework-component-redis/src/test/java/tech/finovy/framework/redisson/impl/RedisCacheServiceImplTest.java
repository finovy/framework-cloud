package tech.finovy.framework.redisson.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import tech.finovy.framework.redis.entity.cache.entity.*;
import tech.finovy.framework.redisson.api.CacheApi;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class RedisCacheServiceImplTest {

    @Mock
    private CacheApi mockCacheApi;

    private RedisCacheServiceImpl redisCacheServiceImplUnderTest;

    private AutoCloseable mockitoCloseable;

    @BeforeEach
    void setUp() {
        mockitoCloseable = openMocks(this);
        redisCacheServiceImplUnderTest = new RedisCacheServiceImpl(mockCacheApi);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockitoCloseable.close();
    }

    @Test
    void testGetCache1() {
        // Setup
        when(mockCacheApi.getCache(Serializable.class, "key", false)).thenReturn(new CachePack<>("key", false));
        // Run the test
        final CachePack<Serializable> result = redisCacheServiceImplUnderTest.getCache(Serializable.class, "key");
        // Verify the results
        assertNotNull(result);
    }

    @Test
    void testGetCache2() {
        // Setup
        // Configure CacheApi.getCache(...).
        final CacheBatchPack cacheBatchPack = new CacheBatchPack<>(Object.class, Map.ofEntries(), false);
        when(mockCacheApi.getCache(Serializable.class, List.of("value"))).thenReturn(cacheBatchPack);
        // Run the test
        final CacheBatchPack result = redisCacheServiceImplUnderTest.getCache(Serializable.class, List.of("value"));
        // Verify the results
        assertNotNull(result);
    }

    @Test
    void testGetCache3() {
        // Setup
        when(mockCacheApi.getCache(Serializable.class, "key", false)).thenReturn(new CachePack<>("key", false));
        // Run the test
        final CachePack<Serializable> result = redisCacheServiceImplUnderTest.getCache(Serializable.class, "key",
                false);
        // Verify the results
        assertNotNull(result);
    }

    @Test
    void testGetCache4() {
        // Setup
        // Configure CacheApi.getCache(...).
        final CacheBatchPack cacheBatchPack = new CacheBatchPack<>(Object.class, Map.ofEntries(), false);
        when(mockCacheApi.getCache(Serializable.class, List.of("value"), false)).thenReturn(cacheBatchPack);

        // Run the test
        final CacheBatchPack result = redisCacheServiceImplUnderTest.getCache(Serializable.class, List.of("value"),
                false);
        // Verify the results
        assertNotNull(result);
    }

    @Test
    void testGetCachesByPattern1() {
        // Setup
        when(mockCacheApi.getCachesByPattern(Serializable.class, "pattern", false))
                .thenReturn(List.of(new CachePack<>("key", false)));

        // Run the test
        final List<CachePack<Serializable>> result = redisCacheServiceImplUnderTest.getCachesByPattern(
                Serializable.class, "pattern", false);
        // Verify the results
        assertNotNull(result);
    }

    @Test
    void testGetCachesByPattern1_CacheApiReturnsNoItems() {
        // Setup
        when(mockCacheApi.getCachesByPattern(Serializable.class, "pattern", false)).thenReturn(Collections.emptyList());

        // Run the test
        final List<CachePack<Serializable>> result = redisCacheServiceImplUnderTest.getCachesByPattern(
                Serializable.class, "pattern", false);
        // Verify the results
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testGetCachesByPattern2() {
        // Setup
        when(mockCacheApi.getCachesByPattern(Serializable.class, "pattern"))
                .thenReturn(List.of(new CachePack<>("key", false)));

        // Run the test
        final List<CachePack<Serializable>> result = redisCacheServiceImplUnderTest.getCachesByPattern(
                Serializable.class, "pattern");
        assertNotNull(result);
    }

    @Test
    void testGetCachesByPattern2_CacheApiReturnsNoItems() {
        // Setup
        when(mockCacheApi.getCachesByPattern(Serializable.class, "pattern")).thenReturn(Collections.emptyList());

        // Run the test
        final List<CachePack<Serializable>> result = redisCacheServiceImplUnderTest.getCachesByPattern(
                Serializable.class, "pattern");

        // Verify the results
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testPutCache1() {
        // Setup
        final CachePack<Serializable> cachePack = new CachePack<>("key", false);
        when(mockCacheApi.putCache(new CachePack<>("key", false))).thenReturn(new CachePack<>("key", false));

        // Run the test
        final CachePack<Serializable> result = redisCacheServiceImplUnderTest.putCache(cachePack);

        // Verify the results
    }

    @Test
    void testPutCache2() {
        assertNull(redisCacheServiceImplUnderTest.putCache(
                new CacheBatchPack<>(Serializable.class, Map.ofEntries(Map.entry("value", "value")), false)));
    }

    @Test
    void testGetSerialCache1() {
        // Setup
        final CacheKey cacheKey = new CacheKey("key", false, Object.class);
        final SerialCache expectedResult = new SerialCache("key", false);
        when(mockCacheApi.getSerialCache(new CacheKey("key", false, Object.class)))
                .thenReturn(new SerialCache("key", false));

        // Run the test
        final SerialCache result = redisCacheServiceImplUnderTest.getSerialCache(cacheKey);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    void testGetSerialCache2() {
        assertNull(redisCacheServiceImplUnderTest.getSerialCache(
                new CacheBatchKey(List.of("value"), false, Object.class)));
    }

    @Test
    void testPutSerialCache1() {
        // Setup
        final SerialCache serialCache = new SerialCache("key", false);
        final SerialCache expectedResult = new SerialCache("key", false);
        when(mockCacheApi.putSerialCache(new SerialCache("key", false))).thenReturn(new SerialCache("key", false));

        // Run the test
        final SerialCache result = redisCacheServiceImplUnderTest.putSerialCache(serialCache);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    void testPutSerialCache2() {
        // Setup
        final List<SerialCache> batchCache = List.of(new SerialCache("key", false));

        // Configure CacheApi.putSerialCache(...).
        final BatchSimpleResult batchSimpleResult = new BatchSimpleResult();
        batchSimpleResult.setErrKeys(List.of("value"));
        batchSimpleResult.setSuccessKey(List.of("value"));
        batchSimpleResult.setSuccess(false);
        when(mockCacheApi.putSerialCache(List.of(new SerialCache("key", false)))).thenReturn(batchSimpleResult);

        // Run the test
        final BatchSimpleResult result = redisCacheServiceImplUnderTest.putSerialCache(batchCache);

        // Verify the results
    }

    @Test
    void testDeleteCache1() {
        // Setup
        final CacheKey cacheKey = new CacheKey("key", false, Object.class);
        when(mockCacheApi.deleteCache(new CacheKey("key", false, Object.class))).thenReturn(false);

        // Run the test
        final boolean result = redisCacheServiceImplUnderTest.deleteCache(cacheKey);

        // Verify the results
        assertFalse(result);
    }

    @Test
    void testDeleteCache2() {
        // Setup
        final CacheBatchKey cacheKey = new CacheBatchKey(List.of("value"), false, Object.class);

        // Configure CacheApi.deleteCache(...).
        final BatchSimpleResult batchSimpleResult = new BatchSimpleResult();
        batchSimpleResult.setErrKeys(List.of("value"));
        batchSimpleResult.setSuccessKey(List.of("value"));
        batchSimpleResult.setSuccess(false);
        when(mockCacheApi.deleteCache(new CacheBatchKey(List.of("value"), false, Object.class)))
                .thenReturn(batchSimpleResult);

        // Run the test
        final BatchSimpleResult result = redisCacheServiceImplUnderTest.deleteCache(cacheKey);

        // Verify the results
    }

    @Test
    void testDeleteCachesByPattern() {
        // Setup
        when(mockCacheApi.deleteCachesByPattern("pattern")).thenReturn(0L);

        // Run the test
        final long result = redisCacheServiceImplUnderTest.deleteCachesByPattern("pattern");

        // Verify the results
        assertEquals(0L, result);
    }

    @Test
    void testSetExpire() {
        // Setup
        final CacheKey cacheKey = new CacheKey("key", false, Object.class);
        when(mockCacheApi.setExpire(new CacheKey("key", false, Object.class))).thenReturn(false);

        // Run the test
        final boolean result = redisCacheServiceImplUnderTest.setExpire(cacheKey);

        // Verify the results
        assertFalse(result);
    }
}
