package tech.finovy.framework.cache.local.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CachePropertiesTest {

    private CacheProperties cachePropertiesUnderTest;

    @BeforeEach
    void setUp() {
        cachePropertiesUnderTest = new CacheProperties();
    }

    @Test
    void testEquals() {
        Assertions.assertEquals(1000, cachePropertiesUnderTest.getInitialCapacity());
        Assertions.assertEquals(5000, cachePropertiesUnderTest.getMaximumSize());
        Assertions.assertEquals(0, cachePropertiesUnderTest.getMaximumWeight());
        Assertions.assertEquals(60, cachePropertiesUnderTest.getExpireAfterAccess());
        Assertions.assertEquals(60, cachePropertiesUnderTest.getExpireAfterWrite());
        assertFalse(cachePropertiesUnderTest.isSoftValues());
        Assertions.assertTrue(cachePropertiesUnderTest.isWeakValues());
        assertFalse(cachePropertiesUnderTest.isWeakKeys());
        assertFalse(cachePropertiesUnderTest.isRecordStats());
    }

}
