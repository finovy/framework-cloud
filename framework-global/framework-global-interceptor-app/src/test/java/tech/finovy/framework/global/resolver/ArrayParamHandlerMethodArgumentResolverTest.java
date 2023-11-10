package tech.finovy.framework.global.resolver;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatusCode;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@Slf4j
class ArrayParamHandlerMethodArgumentResolverTest {

    private ArrayParamHandlerMethodArgumentResolver arrayParamHandlerMethodArgumentResolverUnderTest;

    private static Method method;
    private static Method method1;

    static {
        Class<?> testClass;
        try {
            testClass = Class.forName("tech.finovy.framework.global.resolver.ResolverTest");
            method = testClass.getDeclaredMethod("test1", ResolverTest.Person.class);
            method1 = testClass.getDeclaredMethod("test3", ResolverTest.Info.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    @BeforeEach
    void setUp() {
        arrayParamHandlerMethodArgumentResolverUnderTest = new ArrayParamHandlerMethodArgumentResolver();
    }

    @Test
    void testSupportsParameter() {
        // Setup
        final MethodParameter methodParameter = new MethodParameter(method, 0);

        // Run the test
        final boolean result = arrayParamHandlerMethodArgumentResolverUnderTest.supportsParameter(methodParameter);

        // Verify the results
        assertTrue(result);
    }

    @Test
    void testResolveArgument() throws Exception {
        // Setup
        final MethodParameter parameter = new MethodParameter(method, 0);
        final ModelAndViewContainer mavContainer = new ModelAndViewContainer();
        mavContainer.setView("view");
        mavContainer.setRedirectModelScenario(false);
        mavContainer.setStatus(HttpStatusCode.valueOf(200));
        mavContainer.setBindingDisabled("attributeName");
        mavContainer.setRequestHandled(false);

        final NativeWebRequest webRequest = Mockito.mock(NativeWebRequest.class);
        final WebDataBinderFactory binderFactory = Mockito.mock(WebDataBinderFactory.class);

        final WebDataBinder binder = Mockito.mock(WebDataBinder.class);
        final BindingResult bindingResult = Mockito.mock(BindingResult.class);


        List<String> requestParams = Lists.newArrayList("user_name", "age");

        when(webRequest.getParameterNames()).thenAnswer(invocation -> requestParams.iterator());
        when(webRequest.getParameter(Mockito.anyString())).thenReturn("Alice").thenReturn("18");
        when(binderFactory.createBinder(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(binder);
        when(binder.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.hasErrors()).thenReturn(false);

        // Run the test
        final ResolverTest.Person result = (ResolverTest.Person) arrayParamHandlerMethodArgumentResolverUnderTest.resolveArgument(parameter, mavContainer, webRequest, binderFactory);

        // Verify the results
        log.info("{}", result);
        assert result != null;
        assertEquals("Alice", result.getUserName());
        assertEquals(18, result.getAge());


        List<String> requestParamsB = Lists.newArrayList("user_name", "age");

        when(webRequest.getParameterNames()).thenAnswer(invocation -> requestParamsB.iterator());
        when(webRequest.getParameter(Mockito.anyString())).thenReturn("['alice','bob']").thenReturn("18");
        when(binderFactory.createBinder(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(binder);
        when(binder.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.hasErrors()).thenReturn(false);
        final ResolverTest.Info resultB = (ResolverTest.Info) arrayParamHandlerMethodArgumentResolverUnderTest.resolveArgument( new MethodParameter(method1, 0), mavContainer, webRequest, binderFactory);
        assertEquals(18, resultB.getAge());
        when(webRequest.getParameter(Mockito.anyString())).thenReturn("alice,bob").thenReturn("18");
        final ResolverTest.Info resultC = (ResolverTest.Info) arrayParamHandlerMethodArgumentResolverUnderTest.resolveArgument( new MethodParameter(method1, 0), mavContainer, webRequest, binderFactory);
        assertEquals(18, resultC.getAge());
    }

}
