package tech.finovy.framework.global.resolver;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatusCode;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class UnderlineToCamelArgumentResolverTest {

    private UnderlineToCamelArgumentResolver underlineToCamelArgumentResolverUnderTest;

    private static Method method;

    static {
        Class<?> testClass;
        try {
            testClass = Class.forName("tech.finovy.framework.global.resolver.ResolverTest");
            method = testClass.getDeclaredMethod("test1", ResolverTest.Person.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    @BeforeEach
    void setUp() {
        underlineToCamelArgumentResolverUnderTest = new UnderlineToCamelArgumentResolver();
    }

    @Test
    void testSupportsParameter() {
        // Setup
        final MethodParameter methodParameter = new MethodParameter(method, 0);

        // Run the test
        final boolean result = underlineToCamelArgumentResolverUnderTest.supportsParameter(methodParameter);

        // Verify the results
        assertFalse(result);
    }

    @Test
    void testResolveArgument() throws Exception {
        // Setup
        final MethodParameter methodParameter = new MethodParameter(method, 0);
        final ModelAndViewContainer modelAndViewContainer = new ModelAndViewContainer();
        modelAndViewContainer.setView("view");
        modelAndViewContainer.setRedirectModelScenario(false);
        modelAndViewContainer.setStatus(HttpStatusCode.valueOf(200));
        modelAndViewContainer.setBindingDisabled("attributeName");
        modelAndViewContainer.setRequestHandled(false);

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
        final Object result = underlineToCamelArgumentResolverUnderTest.resolveArgument(methodParameter,
                modelAndViewContainer, webRequest, binderFactory);

        // Verify the results
    }

    @Test
    void testResolveArgument_ThrowsException() {
        // Setup
        final MethodParameter methodParameter = new MethodParameter(method, 0);
        final ModelAndViewContainer modelAndViewContainer = new ModelAndViewContainer();
        modelAndViewContainer.setView("view");
        modelAndViewContainer.setRedirectModel(new ModelMap("attributeName", "attributeValue"));
        modelAndViewContainer.setRedirectModelScenario(false);
        modelAndViewContainer.setStatus(HttpStatusCode.valueOf(200));
        modelAndViewContainer.setBindingDisabled("attributeName");
        modelAndViewContainer.setRequestHandled(false);

        final NativeWebRequest nativeWebRequest = null;
        final WebDataBinderFactory webDataBinderFactory = null;

        // Run the test
        assertThrows(Exception.class,
                () -> underlineToCamelArgumentResolverUnderTest.resolveArgument(methodParameter, modelAndViewContainer,
                        nativeWebRequest, webDataBinderFactory));
    }
}
