package tech.finovy.framework.redisson.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.redisson.RedissonJsonBucket;
import org.redisson.api.*;
import org.redisson.client.codec.Codec;
import org.redisson.client.codec.StringCodec;
import tech.finovy.framework.redis.entity.cache.entity.*;
import tech.finovy.framework.redisson.config.RedissonConfiguration;
import tech.finovy.framework.redisson.holder.RedisContext;
import tech.finovy.framework.redisson.holder.RedisContextHolder;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@SuppressWarnings("all")
class RedisCacheImplTest {

    @Mock
    private RedissonClient mockClient;
    @Mock
    private RedissonConfiguration mockConfiguration;

    private RedisCacheImpl redisCacheImplUnderTest;

    final RedisContext redisContext = RedisContextHolder.get();

    @BeforeEach
    void setUp() {
        initMocks(this);
        RedisContextHolder.get().setClient(mockClient);
        redisCacheImplUnderTest = new RedisCacheImpl(mockConfiguration);
    }

    @Test
    void testGetCache1() {
        final RedissonJsonBucket bucket = Mockito.mock(RedissonJsonBucket.class);
        when(bucket.isExists()).thenReturn(true);
        when(redisContext.createKey(anyString(), anyString(), anyBoolean())).thenReturn("key");
        when(mockClient.getBucket(anyString(), any(Codec.class))).thenReturn(bucket);
        when(redisContext.isDebug()).thenReturn(true);
        // case 1: getCache normal
        final CachePack<Serializable> result = redisCacheImplUnderTest.getCache(Serializable.class, "key");
        Assertions.assertTrue(result.isExists());
        final CachePack<Serializable> pack = redisCacheImplUnderTest.getCache(null, "key");
        Assertions.assertNotNull(pack.getErrMsg());
        final CachePack<Integer> packB = redisCacheImplUnderTest.getCache(Integer.class, "key");
        Assertions.assertTrue(packB.isExists());
        when(bucket.isExists()).thenReturn(false);
        final CachePack<Serializable> resultFalse = redisCacheImplUnderTest.getCache(Serializable.class, "key");
        Assertions.assertFalse(resultFalse.isExists());
    }

    @Test
    void testGetCache2() {
        final RBucketAsync bucketAsync = mock(RBucketAsync.class);
        final BatchResult batchResult = mock(BatchResult.class);
        final RBatch batch = mock(RBatch.class);
        when(batch.getBucket(anyString(), any(StringCodec.class))).thenReturn(bucketAsync);
        when(batch.execute()).thenReturn(batchResult);
        final ArrayList<String> response = Lists.newArrayList("A", "B", "C");
        when(batchResult.getResponses()).thenReturn(response);

        when(mockClient.createBatch(any(BatchOptions.class))).thenReturn(batch);
        when(redisContext.createKey(anyString(), anyString(), anyBoolean())).thenReturn("key");
        final CacheBatchPack<Serializable> result = redisCacheImplUnderTest.getCache(Serializable.class,
                List.of("key"));
        Assertions.assertTrue(result.isExists());
        final CacheBatchPack<Serializable> pack = redisCacheImplUnderTest.getCache(null, List.of("key"));
        Assertions.assertNotNull(pack.getErrMsg());
        final CacheBatchPack<Integer> packB = redisCacheImplUnderTest.getCache(Integer.class, List.of("key"));
        Assertions.assertTrue(packB.isExists());
    }

    @Test
    void testGetCachesByPattern1() {
        RedissonJsonBucket bucket = Mockito.mock(RedissonJsonBucket.class);
        when(bucket.isExists()).thenReturn(true);
        when(redisContext.createKey(anyString(), anyString(), anyBoolean())).thenReturn("key");
        when(mockClient.getBucket(anyString(), any(Codec.class))).thenReturn(bucket);
        when(redisContext.isDebug()).thenReturn(true);
        // Setup
        List<String> objects = new ArrayList<>();
        objects.add("A");
        objects.add("B");
        objects.add("C");
        final RKeys mock = mock(RKeys.class);
        when(mockClient.getKeys()).thenReturn(mock);
        when(mock.getKeysByPattern(anyString())).thenAnswer(invocation -> objects);
        // Run the test
        final List<CachePack<Serializable>> result = redisCacheImplUnderTest.getCachesByPattern(Serializable.class,
                "pattern");
        result.forEach(serializableCachePack -> Assertions.assertTrue(serializableCachePack.isExists()));
    }

    @Test
    void testPutCache1() {
        // Setup
        final CachePack<String> cachePack = new CachePack<>("key", false);
        cachePack.setData("put cache test");
        RedissonJsonBucket bucket = Mockito.mock(RedissonJsonBucket.class);
        when(bucket.isExists()).thenReturn(true);
        when(redisContext.createKey(anyString(), anyString(), anyBoolean())).thenReturn("key");
        when(mockClient.getBucket(anyString(), any(Codec.class))).thenReturn(bucket);
        when(redisContext.isDebug()).thenReturn(true);
        when(mockConfiguration.getKeyDefaultTtl()).thenReturn(0L);
        // Run the test
        final CachePack<String> result = redisCacheImplUnderTest.putCache(cachePack);
        // Verify the results
        Assertions.assertNull(result.getErrMsg());
        cachePack.setData(null);
        final CachePack<String> resultB = redisCacheImplUnderTest.putCache(cachePack);
        // Verify the results
        Assertions.assertNotNull(resultB.getErrMsg());
        final CachePack<Integer> cachePackInteger = new CachePack<>("key", false);
        cachePackInteger.setData(1);
        final CachePack<Integer> resultC = redisCacheImplUnderTest.putCache(cachePackInteger);
        // Verify the results
        Assertions.assertNull(resultC.getErrMsg());
        final CachePack<JSONObject> cachePackJson = new CachePack<>("key", false);
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("age","18");
        cachePackJson.setData(jsonObject);
        final CachePack<JSONObject> resultD = redisCacheImplUnderTest.putCache(cachePackJson);
        Assertions.assertNull(resultD.getErrMsg());
        // error
        when(mockConfiguration.getKeyDefaultTtl()).thenThrow(new RuntimeException());
        final CachePack<JSONObject> resultF = redisCacheImplUnderTest.putCache(cachePackJson);
        Assertions.assertNotNull(resultF.getErrMsg());

    }

    @Test
    void testPutCache2() {
        // Setup
        final CacheBatchPack<String> cachePack = new CacheBatchPack<>(String.class,
                Map.ofEntries(Map.entry("value", "value")), false);
        final HashMap<String, String> data = new HashMap<>();
        data.put("name", "Alice");
        data.put("name", "Bob");
        cachePack.setData(data);
        RedissonJsonBucket bucket = Mockito.mock(RedissonJsonBucket.class);
        when(bucket.isExists()).thenReturn(true);

        final RBatch batch = mock(RBatch.class);
        final RBucketAsync bucketAsync = mock(RBucketAsync.class);
        final BatchResult batchResult = mock(BatchResult.class);
        when(batch.getBucket(anyString(), any(StringCodec.class))).thenReturn(bucketAsync);
        when(mockClient.createBatch(any(BatchOptions.class))).thenReturn(batch);
        when(redisContext.createKey(anyString(), anyString(), anyBoolean())).thenReturn("key");
        when(mockClient.getBucket(anyString(), any(Codec.class))).thenReturn(bucket);
        when(redisContext.isDebug()).thenReturn(true);
        when(mockConfiguration.getKeyDefaultTtl()).thenReturn(0L);
        List<Boolean> response = Lists.newArrayList(true);
        when(batch.execute()).thenReturn(batchResult);
        when(batchResult.getResponses()).thenReturn(response);
        // Run the test
        final BatchSimpleResult result = redisCacheImplUnderTest.putCache(cachePack);
        // Verify the results
        Assertions.assertTrue(result.isSuccess());

        // integer
        final CacheBatchPack<Integer> cachePackB = new CacheBatchPack<>(Integer.class,
                Map.ofEntries(Map.entry("value", 1)), false);
        final BatchSimpleResult resultB = redisCacheImplUnderTest.putCache(cachePackB);
        assertTrue(resultB.isSuccess());
        // json
        final CacheBatchPack<JSONObject> cachePackC = new CacheBatchPack<>(JSONObject.class,
                Map.ofEntries(Map.entry("value", new JSONObject())), false);
        final BatchSimpleResult resultC = redisCacheImplUnderTest.putCache(cachePackC);
        assertTrue(resultC.isSuccess());
    }

    @Test
    void testGetSerialCache1() {

        final RBucket bucket = mock(RBucket.class);
        final BatchResult batchResult = mock(BatchResult.class);
        final RBatch batch = mock(RBatch.class);
        when(mockClient.getBucket(anyString(), any(StringCodec.class))).thenReturn(bucket);
        when(batch.execute()).thenReturn(batchResult);
        when(bucket.isExists()).thenReturn(true);
        final ArrayList<String> response = Lists.newArrayList("A", "B", "C");
        when(batchResult.getResponses()).thenReturn(response);


        final CacheKey cacheKey = new CacheKey("key", false, Object.class);
        when(mockClient.createBatch(any(BatchOptions.class))).thenReturn(batch);
        when(redisContext.createKey(anyString(), anyString(), anyBoolean())).thenReturn("key");
        when(redisContext.isDebug()).thenReturn(true);
        cacheKey.setRefreshTimeToLive(true);
        final SerialCache result = redisCacheImplUnderTest.getSerialCache(cacheKey);
        Assertions.assertTrue(result.isExists());
        when(mockClient.getBucket(anyString(), any(StringCodec.class))).thenThrow(new RuntimeException("test"));

        final SerialCache resultB = redisCacheImplUnderTest.getSerialCache(cacheKey);
        Assertions.assertFalse(resultB.isExists());

    }

    @Test
    void testPutSerialCache1() {
        final CacheBatchPack<String> cachePack = new CacheBatchPack<>(String.class,
                Map.ofEntries(Map.entry("value", "value")), false);
        final HashMap<String, String> data = new HashMap<>();
        data.put("name", "Alice");
        data.put("name", "Bob");
        cachePack.setData(data);
        RedissonJsonBucket bucket = Mockito.mock(RedissonJsonBucket.class);
        when(bucket.isExists()).thenReturn(true);

        final RBatch batch = mock(RBatch.class);
        final RBucketAsync bucketAsync = mock(RBucketAsync.class);
        final BatchResult batchResult = mock(BatchResult.class);
        when(batch.getBucket(anyString(), any(StringCodec.class))).thenReturn(bucketAsync);
        when(mockClient.createBatch(any(BatchOptions.class))).thenReturn(batch);
        when(redisContext.createKey(anyString(), anyString(), anyBoolean())).thenReturn("key");
        when(mockClient.getBucket(anyString(), any(Codec.class))).thenReturn(bucket);
        when(redisContext.isDebug()).thenReturn(true);
        when(mockConfiguration.getKeyDefaultTtl()).thenReturn(0L);
        List<Boolean> response = Lists.newArrayList(true);
        when(batch.execute()).thenReturn(batchResult);
        when(batchResult.getResponses()).thenReturn(response);
        final SerialCache serialCache = new SerialCache("key", false);
        serialCache.setExpireAt(100);
        final SerialCache result = redisCacheImplUnderTest.putSerialCache(serialCache);
        Assertions.assertNull(result.getErrMsg());

        // branch
        final SerialCache serialCacheC = new SerialCache("key", false);
        serialCacheC.setExpireAt(100);
        serialCacheC.setTimeToLive(100);
        final SerialCache resultC = redisCacheImplUnderTest.putSerialCache(serialCacheC);
        Assertions.assertNull(resultC.getErrMsg());

        final SerialCache serialCacheD = new SerialCache("key", false);
        serialCacheD.setExpireAt(100);
        serialCacheD.setTimeToLiveRemain(100);
        final SerialCache resultD = redisCacheImplUnderTest.putSerialCache(serialCacheD);
        Assertions.assertNull(resultD.getErrMsg());

        when(mockClient.getBucket(anyString(), any(StringCodec.class))).thenThrow(new RuntimeException("test"));

        final SerialCache resultB = redisCacheImplUnderTest.putSerialCache(serialCache);
        Assertions.assertFalse(resultB.isExists());

    }

    @Test
    void testDeleteCache1() {
        // Setup
        final CacheKey cacheKey = new CacheKey("key", false, Object.class);
        when(redisContext.createKey(anyString(), anyString(), anyBoolean())).thenReturn("key");
        when(redisContext.isDebug()).thenReturn(true);
        final RBucket bucket = mock(RBucket.class);
        final BatchResult batchResult = mock(BatchResult.class);
        final RBatch batch = mock(RBatch.class);
        when(mockClient.getBucket(anyString(), any(StringCodec.class))).thenReturn(bucket);
        when(bucket.delete()).thenReturn(true);
        // Run the test
        final boolean result = redisCacheImplUnderTest.deleteCache(cacheKey);
        // Verify the results
        assertTrue(result);
    }

    @Test
    void testDeleteCache2() {
        // Setup
        final CacheBatchKey cacheKey = new CacheBatchKey(List.of("value"), false, Object.class);
        final RBatch batch = mock(RBatch.class);
        final RBucketAsync bucketAsync = mock(RBucketAsync.class);
        final BatchResult batchResult = mock(BatchResult.class);
        when(batch.getBucket(anyString(), any(StringCodec.class))).thenReturn(bucketAsync);
        when(mockClient.createBatch(any(BatchOptions.class))).thenReturn(batch);
        when(redisContext.createKey(anyString(), anyString(), anyBoolean())).thenReturn("key");
        List<Boolean> response = Lists.newArrayList(true);
        when(batch.execute()).thenReturn(batchResult);
        when(batchResult.getResponses()).thenReturn(response);

        // Run the test
        final BatchSimpleResult result = redisCacheImplUnderTest.deleteCache(cacheKey);
        // Verify the results
        Assertions.assertTrue(result.isSuccess());
    }

    @Test
    void testDeleteCachesByPattern1() {
        final RBatch batch = mock(RBatch.class);
        final RBucketAsync bucketAsync = mock(RBucketAsync.class);
        final BatchResult batchResult = mock(BatchResult.class);
        when(batch.getBucket(anyString(), any(StringCodec.class))).thenReturn(bucketAsync);
        when(mockClient.createBatch(any(BatchOptions.class))).thenReturn(batch);
        when(redisContext.isDebug()).thenReturn(true);

        List<Boolean> response = Lists.newArrayList(true);
        when(batch.execute()).thenReturn(batchResult);
        when(batchResult.getResponses()).thenReturn(response);

        List<String> objects = new ArrayList<>();
        objects.add("A");
        objects.add("B");
        objects.add("C");
        final RKeys mock = mock(RKeys.class);
        when(mockClient.getKeys()).thenReturn(mock);
        when(mock.getKeysByPattern(anyString())).thenAnswer(invocation -> objects);
        final RBucket bucket = mock(RBucket.class);
        when(mockClient.getBucket(anyString(), any(StringCodec.class))).thenReturn(bucket);
        when(bucket.delete()).thenReturn(true);
        when(redisContext.createKey(anyString(), any(), anyBoolean())).thenReturn("key");
        // Run the test
        final long result = redisCacheImplUnderTest.deleteCachesByPattern("key");
        // Verify the results
        assertEquals(3, result);
    }

    @Test
    void testSetExpire() {
        // Setup
        final CacheKey cacheKey = new CacheKey("key", false, Object.class);
        cacheKey.setTimeToLive(100L);
        when(redisContext.createKey(anyString(), any(), anyBoolean())).thenReturn("key");
        when(redisContext.isDebug()).thenReturn(true);
        final RBucket bucket = mock(RBucket.class);
        when(mockClient.getBucket(anyString(), any(StringCodec.class))).thenReturn(bucket);
        when(bucket.expire(100L, TimeUnit.MILLISECONDS)).thenReturn(true);
        // Run the test
        final boolean result = redisCacheImplUnderTest.setExpire(cacheKey);
        // Verify the results
        assertTrue(result);
    }
}
