package tech.finovy.framework.redisson.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import tech.finovy.framework.redisson.config.RedissonConfiguration;
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


    @BeforeEach
    void setUp() {
        mockitoCloseable = openMocks(this);
        RedisContextHolder.get().setClient(mockClient);
        redisMapImplUnderTest = new RedisMapImpl();
        final RedissonConfiguration redissonConfiguration = new RedissonConfiguration();
        redissonConfiguration.setKeyHashModSize(1024);
        redissonConfiguration.setKeyVersion("1.0");
        redissonConfiguration.setKeyPrefix("test");
        RedisContextHolder.get().setRedissonConfiguration(redissonConfiguration,1);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockitoCloseable.close();
    }

    @Test
    void testPut() {
        // Setup
        final RMap map = Mockito.mock(RMap.class);
        when(mockClient.getMap(Mockito.anyString())).thenReturn(map);
        // Run the test
        redisMapImplUnderTest.put("mapKey", "key", "value");
    }

    @Test
    void testFastPut() {
        // Setup
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
        final RMap map = Mockito.mock(RMap.class);
        when(mockClient.getMap(Mockito.anyString())).thenReturn(map);

        redisMapImplUnderTest.computeIfAbsent("mapKey", "key", mappingFunction);

    }

    @Test
    void testContainsKey() {
        // Setup
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
        final RMap map = Mockito.mock(RMap.class);
        when(mockClient.getMap(Mockito.anyString())).thenReturn(map);

        redisMapImplUnderTest.remove("mapKey", "key");

    }

    @Test
    void testFastRemove() {
        // Setup
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
        final RMap map = Mockito.mock(RMap.class);
        when(mockClient.getMap(Mockito.anyString())).thenReturn(map);

        // Run the test
        redisMapImplUnderTest.putAll("mapKey", m);

        // Verify the results
    }

    @Test
    void testClear() {
        // Setup
        final RMap map = Mockito.mock(RMap.class);
        when(mockClient.getMap(Mockito.anyString())).thenReturn(map);

        // Run the test
        redisMapImplUnderTest.clear("mapKey");

        // Verify the results
    }

    @Test
    void testKeySet() {
        // Setup
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
        final RMap map = Mockito.mock(RMap.class);
        when(mockClient.getMap(Mockito.anyString())).thenReturn(map);

        // Run the test
        final Collection<Serializable> result = redisMapImplUnderTest.readAllValues("mapKey");

        // Verify the results
    }

    @Test
    void testDestroy() {
        // Setup
        final RMap map = Mockito.mock(RMap.class);
        when(mockClient.getMap(Mockito.anyString())).thenReturn(map);

        // Run the test
        redisMapImplUnderTest.destroy("mapKey");

        // Verify the results
    }

    @Test
    void testValues() {
        // Setup
        final RMap map = Mockito.mock(RMap.class);
        when(mockClient.getMap(Mockito.anyString())).thenReturn(map);

        // Run the test
        final Collection result = redisMapImplUnderTest.values("mapKey");

        // Verify the results
    }
}
