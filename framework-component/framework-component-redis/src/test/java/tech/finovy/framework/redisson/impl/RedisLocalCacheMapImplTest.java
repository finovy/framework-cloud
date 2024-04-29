package tech.finovy.framework.redisson.impl;

import com.google.errorprone.annotations.Var;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RedissonClient;
import tech.finovy.framework.redisson.holder.RedisContext;
import tech.finovy.framework.redisson.holder.RedisContextHolder;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class RedisLocalCacheMapImplTest {

    @Mock
    private RedissonClient mockClient;
    @Mock
    private LocalCachedMapOptions mockOptions;

    private RedisLocalCacheMapImpl redisLocalCacheMapImplUnderTest;

    private AutoCloseable mockitoCloseable;
    
    RedisContext redisContext = RedisContextHolder.get();

    @BeforeEach
    void setUp() {
        mockitoCloseable = openMocks(this);
        RedisContextHolder.get().setClient(mockClient);
        redisLocalCacheMapImplUnderTest = new RedisLocalCacheMapImpl(mockOptions);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockitoCloseable.close();
    }

    @Test
    void testPut() {
        // Setup
        final RLocalCachedMap cachedMap = Mockito.mock(RLocalCachedMap.class);

        when(redisContext.createLocalCacheMapKey("mapKey")).thenReturn("name");
        when(mockClient.getLocalCachedMap(eq("name"), any(LocalCachedMapOptions.class))).thenReturn(cachedMap);

        // Run the test
        redisLocalCacheMapImplUnderTest.put("mapKey", "key", "value");
    }

    @Test
    void testFastPut() {
        final RLocalCachedMap cachedMap = Mockito.mock(RLocalCachedMap.class);
        when(redisContext.createLocalCacheMapKey("mapKey")).thenReturn("name");
        when(mockClient.getLocalCachedMap(eq("name"), any(LocalCachedMapOptions.class))).thenReturn(cachedMap);
        // Run the test
        final boolean result = redisLocalCacheMapImplUnderTest.fastPut("mapKey", "key", "value");

    }

    @Test
    void testGet() {
        final RLocalCachedMap cachedMap = Mockito.mock(RLocalCachedMap.class);
        when(redisContext.createLocalCacheMapKey("mapKey")).thenReturn("name");
        when(mockClient.getLocalCachedMap(eq("name"), any(LocalCachedMapOptions.class))).thenReturn(cachedMap);

        // Run the test
        final Serializable result = redisLocalCacheMapImplUnderTest.get("mapKey", "key");

        // Verify the results
    }

    @Test
    void testComputeIfAbsent() {
        // Setup
        final Function<? super String, ? extends Serializable> mappingFunction = val -> {
            return "value";
        };
        final RLocalCachedMap cachedMap = Mockito.mock(RLocalCachedMap.class);
        when(redisContext.createLocalCacheMapKey("mapKey")).thenReturn("name");
        when(mockClient.getLocalCachedMap(eq("name"), any(LocalCachedMapOptions.class))).thenReturn(cachedMap);

        // Run the test
        final Serializable result = redisLocalCacheMapImplUnderTest.computeIfAbsent("mapKey", "key", mappingFunction);

        // Verify the results
    }

    @Test
    void testContainsKey() {
        // Setup
        final RLocalCachedMap cachedMap = Mockito.mock(RLocalCachedMap.class);
        when(redisContext.createLocalCacheMapKey("mapKey")).thenReturn("name");
        when(mockClient.getLocalCachedMap(eq("name"), any(LocalCachedMapOptions.class))).thenReturn(cachedMap);

        // Run the test
        final boolean result = redisLocalCacheMapImplUnderTest.containsKey("mapKey", "key");

        // Verify the results
        assertFalse(result);
    }

    @Test
    void testIsEmpty() {
        // Setup
        final RLocalCachedMap cachedMap = Mockito.mock(RLocalCachedMap.class);
        when(redisContext.createLocalCacheMapKey("mapKey")).thenReturn("name");
        when(mockClient.getLocalCachedMap(eq("name"), any(LocalCachedMapOptions.class))).thenReturn(cachedMap);

        // Run the test
        final boolean result = redisLocalCacheMapImplUnderTest.isEmpty("mapKey");

        // Verify the results
        assertFalse(result);
    }

    @Test
    void testSize() {
        // Setup
        final RLocalCachedMap cachedMap = Mockito.mock(RLocalCachedMap.class);
        when(redisContext.createLocalCacheMapKey("mapKey")).thenReturn("name");
        when(mockClient.getLocalCachedMap(eq("name"), any(LocalCachedMapOptions.class))).thenReturn(cachedMap);

        // Run the test
        final int result = redisLocalCacheMapImplUnderTest.size("mapKey");

        // Verify the results
        assertEquals(0, result);
    }

    @Test
    void testRemove() {
        // Setup
        final RLocalCachedMap cachedMap = Mockito.mock(RLocalCachedMap.class);
        when(redisContext.createLocalCacheMapKey("mapKey")).thenReturn("name");
        when(mockClient.getLocalCachedMap(eq("name"), any(LocalCachedMapOptions.class))).thenReturn(cachedMap);

        // Run the test
        final Serializable result = redisLocalCacheMapImplUnderTest.remove("mapKey", "key");

        // Verify the results
    }

    @Test
    void testFastRemove() {
        final RLocalCachedMap cachedMap = Mockito.mock(RLocalCachedMap.class);
        when(redisContext.createLocalCacheMapKey("mapKey")).thenReturn("name");
        when(mockClient.getLocalCachedMap(eq("name"), any(LocalCachedMapOptions.class))).thenReturn(cachedMap);

        // Run the test
        final long result = redisLocalCacheMapImplUnderTest.fastRemove("mapKey", "key");

        // Verify the results
        assertEquals(0L, result);
    }

    @Test
    void testPutAll() {
        // Setup
        final Map<String, ? extends Serializable> m = Map.ofEntries(Map.entry("value", "value"));
        final RLocalCachedMap cachedMap = Mockito.mock(RLocalCachedMap.class);
        when(redisContext.createLocalCacheMapKey("mapKey")).thenReturn("name");
        when(mockClient.getLocalCachedMap(eq("name"), any(LocalCachedMapOptions.class))).thenReturn(cachedMap);

        // Run the test
        redisLocalCacheMapImplUnderTest.putAll("mapKey", m);

        // Verify the results
    }

    @Test
    void testClear() {
        // Setup
        final RLocalCachedMap cachedMap = Mockito.mock(RLocalCachedMap.class);
        when(redisContext.createLocalCacheMapKey("mapKey")).thenReturn("name");
        when(mockClient.getLocalCachedMap(eq("name"), any(LocalCachedMapOptions.class))).thenReturn(cachedMap);

        // Run the test
        redisLocalCacheMapImplUnderTest.clear("mapKey");

        // Verify the results
    }

    @Test
    void testKeySet() {
        // Setup
        final RLocalCachedMap cachedMap = Mockito.mock(RLocalCachedMap.class);
        when(redisContext.createLocalCacheMapKey("mapKey")).thenReturn("name");
        when(mockClient.getLocalCachedMap(eq("name"), any(LocalCachedMapOptions.class))).thenReturn(cachedMap);
        when(cachedMap.keySet()).thenReturn(Set.of("value"));

        // Run the test
        final Set<String> result = redisLocalCacheMapImplUnderTest.keySet("mapKey");

        // Verify the results
        assertEquals(Set.of("value"), result);
    }

    @Test
    void testReadAllKeySet() {
        // Setup
        final RLocalCachedMap cachedMap = Mockito.mock(RLocalCachedMap.class);
        when(redisContext.createLocalCacheMapKey("mapKey")).thenReturn("name");
        when(mockClient.getLocalCachedMap(eq("name"), any(LocalCachedMapOptions.class))).thenReturn(cachedMap);
        when(cachedMap.readAllKeySet()).thenReturn(Set.of("value"));

        // Run the test
        final Set<String> result = redisLocalCacheMapImplUnderTest.readAllKeySet("mapKey");

        // Verify the results
        assertEquals(Set.of("value"), result);
    }

    @Test
    void testCachedKeySet() {
        // Setup
        final RLocalCachedMap cachedMap = Mockito.mock(RLocalCachedMap.class);
        when(redisContext.createLocalCacheMapKey("mapKey")).thenReturn("name");
        when(mockClient.getLocalCachedMap(eq("name"), any(LocalCachedMapOptions.class))).thenReturn(cachedMap);
        redisLocalCacheMapImplUnderTest.cachedKeySet("mapKey");
    }

    @Test
    void testCachedValues() {
        // Setup
        final RLocalCachedMap cachedMap = Mockito.mock(RLocalCachedMap.class);
        when(redisContext.createLocalCacheMapKey("mapKey")).thenReturn("name");
        when(mockClient.getLocalCachedMap(eq("name"), any(LocalCachedMapOptions.class))).thenReturn(cachedMap);

        // Run the test
        redisLocalCacheMapImplUnderTest.cachedValues("mapKey");

        // Verify the results
    }

    @Test
    void testPreloadCache1() {
        // Setup
        final RLocalCachedMap cachedMap = Mockito.mock(RLocalCachedMap.class);
        when(redisContext.createLocalCacheMapKey("mapKey")).thenReturn("name");
        when(mockClient.getLocalCachedMap(eq("name"), any(LocalCachedMapOptions.class))).thenReturn(cachedMap);

        // Run the test
        redisLocalCacheMapImplUnderTest.preloadCache("mapKey");

        // Verify the results
    }

    @Test
    void testPreloadCache2() {
        // Setup
        final RLocalCachedMap cachedMap = Mockito.mock(RLocalCachedMap.class);
        when(redisContext.createLocalCacheMapKey("mapKey")).thenReturn("name");
        when(mockClient.getLocalCachedMap(eq("name"), any(LocalCachedMapOptions.class))).thenReturn(cachedMap);

        // Run the test
        redisLocalCacheMapImplUnderTest.preloadCache("mapKey", 0);

        // Verify the results
    }

    @Test
    void testDestroy() {
        // Setup
        final RLocalCachedMap cachedMap = Mockito.mock(RLocalCachedMap.class);
        when(redisContext.createLocalCacheMapKey("mapKey")).thenReturn("name");
        when(mockClient.getLocalCachedMap(eq("name"), any(LocalCachedMapOptions.class))).thenReturn(cachedMap);

        // Run the test
        redisLocalCacheMapImplUnderTest.destroy("mapKey");

        // Verify the results
    }

    @Test
    void testValues() {
        // Setup
        final RLocalCachedMap cachedMap = Mockito.mock(RLocalCachedMap.class);
        when(redisContext.createLocalCacheMapKey("mapKey")).thenReturn("name");
        when(mockClient.getLocalCachedMap(eq("name"), any(LocalCachedMapOptions.class))).thenReturn(cachedMap);

        // Run the test
        final Collection result = redisLocalCacheMapImplUnderTest.values("mapKey");

        // Verify the results
    }

    @Test
    void testReadAllValues() {
        // Setup
        final RLocalCachedMap cachedMap = Mockito.mock(RLocalCachedMap.class);
        when(redisContext.createLocalCacheMapKey("mapKey")).thenReturn("name");
        when(mockClient.getLocalCachedMap(eq("name"), any(LocalCachedMapOptions.class))).thenReturn(cachedMap);

        // Run the test
        final Collection result = redisLocalCacheMapImplUnderTest.readAllValues("mapKey");

        // Verify the results
    }
}
