package tech.finovy.framework.core.common.loader;

import tech.finovy.framework.core.common.exception.EnhancedServiceNotFoundException;
import tech.finovy.framework.core.common.loader.spi.Echo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EnhancedServiceLoaderTest {

    /**
     * Test load by class and class loader.
     */
    @Test
    public void testLoadByClassAndClassLoader() {
        Hello load = EnhancedServiceLoader.load(Hello.class, Hello.class.getClassLoader());
        Assertions.assertEquals(load.say(), "123");
    }

    /**
     * Test load exception.
     */
    @Test
    public void testLoadException() {
        Assertions.assertThrows(EnhancedServiceNotFoundException.class, () -> {
            EnhancedServiceLoaderTest load = EnhancedServiceLoader.load(EnhancedServiceLoaderTest.class);
        });
    }

    /**
     * Test load by class.
     */
    @Test
    public void testLoadByClass() {
        Hello load = EnhancedServiceLoader.load(Hello.class);
        assertThat(load.say()).isEqualTo("123");
    }

    /**
     * Test load by class and activate name.
     */
    @Test
    public void testLoadByClassAndActivateName() {
        Hello englishHello = EnhancedServiceLoader.load(Hello.class, "EnglishHello");
        assertThat(englishHello.say()).isEqualTo("hello!");
    }

    /**
     * Test load by class and class loader and activate name.
     */
    @Test
    public void testLoadByClassAndClassLoaderAndActivateName() {
        Hello englishHello = EnhancedServiceLoader
                .load(Hello.class, "EnglishHello", EnhancedServiceLoaderTest.class.getClassLoader());
        assertThat(englishHello.say()).isEqualTo("hello!");
    }

    /**
     * Gets all extension class.
     */
    @Test
    public void getAllExtensionClass() {
        List<Class<Hello>> allExtensionClass = EnhancedServiceLoader.getAllExtensionClass(Hello.class);
        for (Class<Hello> helloClass : allExtensionClass) {
            System.out.println(helloClass.getSimpleName());
        }
        assertThat(allExtensionClass.get(3).getSimpleName()).isEqualTo((LatinHello.class.getSimpleName()));
        assertThat(allExtensionClass.get(2).getSimpleName()).isEqualTo((FrenchHello.class.getSimpleName()));
        //assertThat(allExtensionClass.get(1).getSimpleName()).isEqualTo((EnglishHello.class.getSimpleName()));
        assertThat(allExtensionClass.get(0).getSimpleName()).isEqualTo((ChineseHello.class.getSimpleName()));
    }

    /**
     * Gets all extension class 1.
     */
    @Test
    public void getAllExtensionClass1() {
        List<Class<Hello>> allExtensionClass = EnhancedServiceLoader.getAllExtensionClass(Hello.class, ClassLoader.getSystemClassLoader());
        assertThat(allExtensionClass).isNotEmpty();
    }

    @Test
    public void getSingletonExtensionInstance() {
        Hello hello1 = EnhancedServiceLoader.load(Hello.class, "ChineseHello");
        Hello hello2 = EnhancedServiceLoader.load(Hello.class, "ChineseHello");
        assertThat(hello1 == hello2).isTrue();
    }

    @Test
    public void getMultipleExtensionInstance() {
        Hello hello1 = EnhancedServiceLoader.load(Hello.class, "LatinHello");
        Hello hello2 = EnhancedServiceLoader.load(Hello.class, "LatinHello");
        assertThat(hello1 == hello2).isFalse();
    }

    @Test
    public void getAllInstances() {
        List<Hello> hellows1 = EnhancedServiceLoader.loadAll(Hello.class);
        List<Hello> hellows2 = EnhancedServiceLoader.loadAll(Hello.class);
        for (Hello hello : hellows1) {
            if (!hello.say().equals("123")) {
                assertThat(hellows2.contains(hello)).isTrue();
            } else {
                assertThat(hellows2.contains(hello)).isFalse();
            }
        }
    }

    @Test
    public void classCastExceptionTest() {
        Assertions.assertThrows(EnhancedServiceNotFoundException.class, () -> {
            Hello1 load = EnhancedServiceLoader.load(Hello1.class);
        });
    }

    @Test
    public void getArrSingletonExtensionInstance() {
        Class<?>[] arrType = {String.class};
        Object[] args = {"ping............................"};
        Echo echo = EnhancedServiceLoader.load(Echo.class, "PingHello", arrType, args);
        assertThat(echo.say()).isEqualTo("ping............................");
    }
}
