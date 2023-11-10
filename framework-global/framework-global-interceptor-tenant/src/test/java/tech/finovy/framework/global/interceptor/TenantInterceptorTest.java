package tech.finovy.framework.global.interceptor;

import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.slf4j.MDC;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import tech.finovy.framework.global.TenantContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class TenantInterceptorTest {

    @Mock
    private TenantConfiguration mockTenantConfiguration;

    private AbstractTenantInterceptor abstractTenantInterceptorUnderTest;

    private AutoCloseable mockitoCloseable;

    @BeforeEach
    void setUp() {
        mockitoCloseable = openMocks(this);
        abstractTenantInterceptorUnderTest = new AbstractTenantInterceptor(mockTenantConfiguration) {
        };
    }

    @AfterEach
    void tearDown() throws Exception {
        mockitoCloseable.close();
    }

    @Test
    void testPreHandle() throws Exception {
        // Setup
        final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(mockTenantConfiguration.isEnable()).thenReturn(true);
        when(mockTenantConfiguration.getTraceIdKey()).thenReturn("trace_id");
        when(mockTenantConfiguration.getAppIdKey()).thenReturn("app_id");
        when(mockTenantConfiguration.isTraceDebug()).thenReturn(true);
        when(httpServletRequest.getHeader("trace_id")).thenReturn("tx_123");
        when(httpServletRequest.getHeader("app_id")).thenReturn("appID_123");

        final JSONObject bean = new JSONObject();
        final Class<?> aClass = Class.forName("com.alibaba.fastjson.JSONObject");
        // Run the test
        final boolean resultA = abstractTenantInterceptorUnderTest.preHandle(httpServletRequest, null, new HandlerMethod(bean, aClass.getDeclaredMethods()[0]));
        // Verify the results
        assertEquals("tx_123", MDC.get("trace_id"));
        assertEquals("appID_123",TenantContext.getCurrentTenant());
        assertTrue(resultA);
        final boolean resultB = abstractTenantInterceptorUnderTest.preHandle(httpServletRequest, null, "new HandlerMethod(bean, aClass.getDeclaredMethods()[0])");
        assertTrue(resultB);
        abstractTenantInterceptorUnderTest.afterCompletion(httpServletRequest, null, new HandlerMethod(bean, aClass.getDeclaredMethods()[0]), new Exception());
    }

    @Test
    void testPreHandle_ThrowsException() throws Exception {
        // Setup
        final HttpServletRequest httpServletRequest = null;
        when(mockTenantConfiguration.isEnable()).thenReturn(false);
        when(mockTenantConfiguration.getTraceIdKey()).thenReturn("result");
        when(mockTenantConfiguration.getAppIdKey()).thenReturn("result");
        when(mockTenantConfiguration.isTraceDebug()).thenReturn(false);

        // Run the test
        Assertions.assertTrue( abstractTenantInterceptorUnderTest.preHandle(httpServletRequest, null, "object"));
    }

    @Test
    void testPostHandle() {
        // Setup
        final ModelAndView modelAndView = new ModelAndView("viewName", "modelName", "modelObject");

        // Run the test
        abstractTenantInterceptorUnderTest.postHandle(null, null, "handler", modelAndView);

        // Verify the results
    }

    @Test
    void testPostHandle_ThrowsException() {
        // Setup
        final ModelAndView modelAndView = new ModelAndView("viewName", "modelName", "modelObject");

        // Run the test
        abstractTenantInterceptorUnderTest.postHandle(null, null, "handler", modelAndView);
    }

    @Test
    void testAfterCompletion() {
        // Setup
        // Run the test
        abstractTenantInterceptorUnderTest.afterCompletion(null, null, "handler", new Exception("message"));

        // Verify the results
    }

    @Test
    void testAfterCompletion_ThrowsException() {
        // Setup
        // Run the test
        abstractTenantInterceptorUnderTest.afterCompletion(null, null, "handler",
                new Exception("message"));
    }
}
