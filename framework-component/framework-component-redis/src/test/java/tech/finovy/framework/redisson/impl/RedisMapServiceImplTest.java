package tech.finovy.framework.redisson.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import tech.finovy.framework.redisson.api.MapApi;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class RedisMapServiceImplTest {

    @Mock
    private MapApi mockMapApi;

    private RedisMapServiceImpl redisMapServiceImplUnderTest;

    private AutoCloseable mockitoCloseable;

    @BeforeEach
    void setUp() {
        mockitoCloseable = openMocks(this);
        redisMapServiceImplUnderTest = new RedisMapServiceImpl(mockMapApi);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockitoCloseable.close();
    }

    @Test
    void testPut() {
        // Setup
        when(mockMapApi.put(eq("mapKey"), eq("key"), any(Serializable.class))).thenReturn("value");
        final Serializable result = redisMapServiceImplUnderTest.put("mapKey", "key", "value");
        assertEquals("value", result);
    }

    @Test
    void testGet() {
        // Setup
        when(mockMapApi.get("mapKey", "key")).thenReturn("value");
        // Run the test
        final Serializable result = redisMapServiceImplUnderTest.get("mapKey", "key");
        // Verify the results
        assertEquals("value", result);
    }

    @Test
    void testContainsKey() {
        // Setup
        when(mockMapApi.containsKey("mapKey", "key")).thenReturn(false);

        // Run the test
        final boolean result = redisMapServiceImplUnderTest.containsKey("mapKey", "key");

        // Verify the results
        assertFalse(result);
    }

    @Test
    void testIsEmpty() {
        // Setup
        when(mockMapApi.isEmpty("mapKey")).thenReturn(false);

        // Run the test
        final boolean result = redisMapServiceImplUnderTest.isEmpty("mapKey");

        // Verify the results
        assertFalse(result);
    }

    @Test
    void testSize() {
        // Setup
        when(mockMapApi.size("mapKey")).thenReturn(0);

        // Run the test
        final int result = redisMapServiceImplUnderTest.size("mapKey");

        // Verify the results
        assertEquals(0, result);
    }

    @Test
    void testRemove() {
        // Setup
        when(mockMapApi.remove("mapKey", "key")).thenReturn("value");
        // Run the test
        final Serializable result = redisMapServiceImplUnderTest.remove("mapKey", "key");
        // Verify the results
        assertEquals("value", result);
    }

    @Test
    void testPutAll() {
        // Setup
        final Map<String, ? extends Serializable> m = Map.ofEntries(Map.entry("value", "value"));
        // Run the test
        redisMapServiceImplUnderTest.putAll("mapKey", m);
    }

    @Test
    void testClear() {
        // Setup
        // Run the test
        redisMapServiceImplUnderTest.clear("mapKey");

        // Verify the results
        verify(mockMapApi).clear("mapKey");
    }

    @Test
    void testKeySet() {
        // Setup
        when(mockMapApi.keySet("mapKey")).thenReturn(Set.of("value"));

        // Run the test
        final Set<String> result = redisMapServiceImplUnderTest.keySet("mapKey");

        // Verify the results
        assertEquals(Set.of("value"), result);
    }

    @Test
    void testKeySet_MapApiReturnsNoItems() {
        // Setup
        when(mockMapApi.keySet("mapKey")).thenReturn(Collections.emptySet());

        // Run the test
        final Set<String> result = redisMapServiceImplUnderTest.keySet("mapKey");

        // Verify the results
        assertEquals(Collections.emptySet(), result);
    }
}
