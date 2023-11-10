package tech.finovy.framework.global.interceptor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TenantConfigurationTest {

    private TenantConfiguration tenantConfigurationUnderTest;

    @BeforeEach
    void setUp() {
        tenantConfigurationUnderTest = new TenantConfiguration();
    }

    @Test
    void testGet() {
        tenantConfigurationUnderTest.setTraceIdKey("trace-id");
        assertEquals("trace-id", tenantConfigurationUnderTest.getTraceIdKey());
        tenantConfigurationUnderTest.setAppIdKey("app-id");
        assertEquals("app-id", tenantConfigurationUnderTest.getAppIdKey());
        assertTrue(tenantConfigurationUnderTest.isEnable());
        assertFalse(tenantConfigurationUnderTest.isTraceDebug());
    }

}
