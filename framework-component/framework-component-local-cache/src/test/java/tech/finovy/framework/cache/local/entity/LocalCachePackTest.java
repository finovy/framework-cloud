package tech.finovy.framework.cache.local.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class LocalCachePackTest {

    private LocalCachePack<String> localCachePackUnderTest;

    @BeforeEach
    void setUp() {
        localCachePackUnderTest = new LocalCachePack<>(String.class, "key");
    }

    @Test
    void testIsExists() {
        localCachePackUnderTest.setData(null);
    }

    @Test
    void testEquals() {
        assertFalse(localCachePackUnderTest.equals("o"));
    }

    @Test
    void testHashCode() {
        assertEquals(-347261244, localCachePackUnderTest.hashCode());
    }
}
