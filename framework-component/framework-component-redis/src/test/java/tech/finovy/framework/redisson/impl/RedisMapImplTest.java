package tech.finovy.framework.redisson.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import tech.finovy.framework.redisson.holder.RedisContext;
import tech.finovy.framework.redisson.holder.RedisContextHolder;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class RedisMapImplTest {

    @Mock
    private RedissonClient mockClient;

    private RedisMapImpl redisMapImplUnderTest;

    private AutoCloseable mockitoCloseable;

    final RedisContext redisContext = RedisContextHolder.get();

    @BeforeEach
    void setUp() {
        mockitoCloseable = openMocks(this);
        RedisContextHolder.get().setClient(mockClient);
        redisMapImplUnderTest = new RedisMapImpl();
    }

    @AfterEach
    void tearDown() throws Exception {
        mockitoCloseable.close();
    }

    @Test
    void testPut() {
        // Setup
        when(redisContext.createMapKey(Mockito.anyString())).thenReturn("mapKey");
        final RMap map = Mockito.mock(RMap.class);
        when(mockClient.getMap(Mockito.anyString())).thenReturn(map);
        // Run the test
        redisMapImplUnderTest.put("mapKey", "key", "value");
    }

    @Test
    void testFastPut() {
        // Setup
        when(redisContext.createMapKey(Mockito.anyString())).thenReturn("mapKey");
        final RMap map = Mockito.mock(RMap.class);
        when(mockClient.getMap(Mockito.anyString())).thenReturn(map);
        // Run the test
        final boolean result = redisMapImplUnderTest.fastPut("mapKey", "key", "value");
        // Verify the results
        assertFalse(result);
    }

    @Test
    void testGet() {
        // Setup
        when(redisContext.createMapKey(Mockito.anyString())).thenReturn("mapKey");
        final RMap map = Mockito.mock(RMap.class);
        when(mockClient.getMap(Mockito.anyString())).thenReturn(map);

        // Run the test
        final Serializable resultA = redisMapImplUnderTest.get("mapKey", "key");
        // Run the test
        final Serializable resultB = redisMapImplUnderTest.get("mapKey", "key", true);
        // Verify the results
        assertNull(resultA);
        assertNull(resultB);
    }


    @Test
    void testComputeIfAbsent() {
        // Setup
        final Function<? super String, ? extends Serializable> mappingFunction = val -> {
            return "value";
        };
        when(redisContext.createMapKey(Mockito.anyString())).thenReturn("mapKey");
        final RMap map = Mockito.mock(RMap.class);
        when(mockClient.getMap(Mockito.anyString())).thenReturn(map);

        redisMapImplUnderTest.computeIfAbsent("mapKey", "key", mappingFunction);

    }

    @Test
    void testContainsKey() {
        // Setup
        when(redisContext.createMapKey(Mockito.anyString())).thenReturn("mapKey");
        final RMap map = Mockito.mock(RMap.class);
        when(mockClient.getMap(Mockito.anyString())).thenReturn(map);

        // Run the test
        final boolean result = redisMapImplUnderTest.containsKey("mapKey", "key");

        // Verify the results
        assertFalse(result);
    }

    @Test
    void testIsEmpty() {
        // Setup
        when(redisContext.createMapKey(Mockito.anyString())).thenReturn("mapKey");
        final RMap map = Mockito.mock(RMap.class);
        when(mockClient.getMap(Mockito.anyString())).thenReturn(map);

        // Run the test
        final boolean result = redisMapImplUnderTest.isEmpty("mapKey");

        // Verify the results
        assertFalse(result);
    }

    @Test
    void testSize() {
        // Setup
        when(redisContext.createMapKey(Mockito.anyString())).thenReturn("mapKey");
        final RMap map = Mockito.mock(RMap.class);
        when(mockClient.getMap(Mockito.anyString())).thenReturn(map);

        // Run the test
        final int result = redisMapImplUnderTest.size("mapKey");

        // Verify the results
        assertEquals(0, result);
    }

    @Test
    void testRemove() {
        // Setup
        when(redisContext.createMapKey(Mockito.anyString())).thenReturn("mapKey");
        final RMap map = Mockito.mock(RMap.class);
        when(mockClient.getMap(Mockito.anyString())).thenReturn(map);

        redisMapImplUnderTest.remove("mapKey", "key");

    }

    @Test
    void testFastRemove() {
        // Setup
        when(redisContext.createMapKey(Mockito.anyString())).thenReturn("mapKey");
        final RMap map = Mockito.mock(RMap.class);
        when(mockClient.getMap(Mockito.anyString())).thenReturn(map);

        // Run the test
        final long result = redisMapImplUnderTest.fastRemove("mapKey", "key");

        // Verify the results
        assertEquals(0L, result);
    }

    @Test
    void testPutAll() {
        // Setup
        final Map<String, ? extends Serializable> m = Map.ofEntries(Map.entry("value", "value"));
        when(redisContext.createMapKey(Mockito.anyString())).thenReturn("mapKey");
        final RMap map = Mockito.mock(RMap.class);
        when(mockClient.getMap(Mockito.anyString())).thenReturn(map);

        // Run the test
        redisMapImplUnderTest.putAll("mapKey", m);

        // Verify the results
    }

    @Test
    void testClear() {
        // Setup
        when(redisContext.createMapKey(Mockito.anyString())).thenReturn("mapKey");
        final RMap map = Mockito.mock(RMap.class);
        when(mockClient.getMap(Mockito.anyString())).thenReturn(map);

        // Run the test
        redisMapImplUnderTest.clear("mapKey");

        // Verify the results
    }

    @Test
    void testKeySet() {
        // Setup
        when(redisContext.createMapKey(Mockito.anyString())).thenReturn("mapKey");
        final RMap map = Mockito.mock(RMap.class);
        when(mockClient.getMap(Mockito.anyString())).thenReturn(map);
        when(map.keySet()).thenReturn(Set.of("value"));

        // Run the test
        final Set<String> result = redisMapImplUnderTest.keySet("mapKey");

        // Verify the results
        assertEquals(Set.of("value"), result);
    }

    @Test
    void testReadAllKeySet() {
        // Setup
        when(redisContext.createMapKey(Mockito.anyString())).thenReturn("mapKey");
        final RMap map = Mockito.mock(RMap.class);
        when(mockClient.getMap(Mockito.anyString())).thenReturn(map);
        when(map.readAllKeySet()).thenReturn(Set.of("value"));

        // Run the test
        final Set<String> result = redisMapImplUnderTest.readAllKeySet("mapKey");

        // Verify the results
        assertEquals(Set.of("value"), result);
    }

    @Test
    void testReadAllValues() {
        // Setup
        when(redisContext.createMapKey(Mockito.anyString())).thenReturn("mapKey");
        final RMap map = Mockito.mock(RMap.class);
        when(mockClient.getMap(Mockito.anyString())).thenReturn(map);

        // Run the test
        final Collection<Serializable> result = redisMapImplUnderTest.readAllValues("mapKey");

        // Verify the results
    }

    @Test
    void testDestroy() {
        // Setup
        when(redisContext.createMapKey(Mockito.anyString())).thenReturn("mapKey");
        final RMap map = Mockito.mock(RMap.class);
        when(mockClient.getMap(Mockito.anyString())).thenReturn(map);

        // Run the test
        redisMapImplUnderTest.destroy("mapKey");

        // Verify the results
    }

    @Test
    void testValues() {
        // Setup
        when(redisContext.createMapKey(Mockito.anyString())).thenReturn("mapKey");
        final RMap map = Mockito.mock(RMap.class);
        when(mockClient.getMap(Mockito.anyString())).thenReturn(map);

        // Run the test
        final Collection result = redisMapImplUnderTest.values("mapKey");

        // Verify the results
    }
}
