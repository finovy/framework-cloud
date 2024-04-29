package tech.finovy.framework.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import tech.finovy.framework.distributed.cache.api.CacheService;
import tech.finovy.framework.global.resolver.ResolverTest;
import tech.finovy.framework.redis.entity.cache.entity.CachePack;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class SessionInterceptorTest {

    @InjectMocks
    private SessionInterceptor sessionInterceptorUnderTest;

    private static Method methodWithPassToken;
    private static Method methodNoPasstoken;

    @Mock
    private CacheService cacheService;


    static {
        Class<?> testClass;
        try {
            testClass = Class.forName("tech.finovy.framework.global.resolver.ResolverTest");
            methodWithPassToken = testClass.getDeclaredMethod("test1", ResolverTest.Person.class);
            methodNoPasstoken = testClass.getDeclaredMethod("test2", ResolverTest.Person.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testPreHandle() throws Exception {
        // Setup
        final HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        final HttpServletResponse httpServletResponse = Mockito.mock(HttpServletResponse.class);

        // case 1: pass
        final boolean result = sessionInterceptorUnderTest.preHandle(httpServletRequest, httpServletResponse, "object");
        assertTrue(result);
        // case 2: has passToken
        HandlerMethod obj = Mockito.mock(HandlerMethod.class);
        when(obj.getMethod()).thenReturn(methodWithPassToken);
        final boolean resultPasstoken = sessionInterceptorUnderTest.preHandle(httpServletRequest, httpServletResponse, obj);
        assertTrue(resultPasstoken);
        when(obj.getMethod()).thenReturn(methodNoPasstoken);

        when(httpServletRequest.getHeader("authorization")).thenReturn("token");


        final CachePack<String> stringCachePack = new CachePack<String>("test");
        stringCachePack.setData("eyJhbGciOiJIUzI1NiJ9.eyJYLUF1dGgtU2lnbmF0dXJlIjoiNmI5OTdmMWZkNGQ1ZTZkZmFiNGZkNjFjNWY0NTkzNDciLCJzdWIiOiJ1c2VySnNvbiIsIlgtQXV0aC1UaW1lc3RhbXAiOjE2OTcyODkzMzIxNDEsIlgtQXV0aC1Ob25jZSI6IjM0NmZhODgzLThhZjEtNGViOS04YmJkLTk4NGQ1MDMxY2FhMyIsImV4cCI6MzE3MDU3Mjg5MzMyLCJpYXQiOjE2OTcyODkzMzIsImp0aSI6IjMzMzM3Njk1LTRmNmMtNDk0NC04ZWMxLTY0MmJkMTBmMTg3MiIsIlgtQXV0aC1BcHBpZCI6ImFwcGlkIn0.xaJfvR14JytpFhs6-6ZZQVVqgAr7A27JSivmqOwDR2o");
        when(cacheService.getCache(String.class, "token", true)).thenReturn(stringCachePack);

        final boolean resultNoPasstoken = sessionInterceptorUnderTest.preHandle(httpServletRequest, httpServletResponse, obj);
        assertTrue(resultNoPasstoken);

        when(httpServletRequest.getHeader("authorization")).thenReturn(null);
        assertFalse(sessionInterceptorUnderTest.preHandle(httpServletRequest, httpServletResponse, obj));
    }

    @Test
    void testPostHandle() {
        // Setup
        final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        final HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        final ModelAndView modelAndView = new ModelAndView("viewName", "modelName", "modelObject");

        // Run the test
        sessionInterceptorUnderTest.postHandle(request, response, "handler", modelAndView);

        // Verify the results
    }


    @Test
    void testAfterCompletion() {
        // Setup
        final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        final HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        // Run the test
        sessionInterceptorUnderTest.afterCompletion(request, response, "handler", new Exception("message"));

        // Verify the results
    }

}
