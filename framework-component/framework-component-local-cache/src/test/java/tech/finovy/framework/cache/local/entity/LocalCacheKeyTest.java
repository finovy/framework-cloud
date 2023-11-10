package tech.finovy.framework.cache.local.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocalCacheKeyTest {

    private LocalCacheKey localCacheKeyUnderTest;

    @BeforeEach
    void setUp() {
        localCacheKeyUnderTest = new LocalCacheKey(String.class, "key");
    }

    @Test
    void testEquals() {
        // branch
        assertFalse(localCacheKeyUnderTest.equals("o"));
        //
        assertFalse(localCacheKeyUnderTest.equals(new LocalCacheKey(String.class, "key1")));
        assertFalse(localCacheKeyUnderTest.equals(new LocalCacheKey(Integer.class, "key1")));
        assertTrue(localCacheKeyUnderTest.equals(new LocalCacheKey(String.class, "key")));
    }

    @Test
    void testHashCode() {
        assertEquals(-347261244, localCacheKeyUnderTest.hashCode());
    }

}
