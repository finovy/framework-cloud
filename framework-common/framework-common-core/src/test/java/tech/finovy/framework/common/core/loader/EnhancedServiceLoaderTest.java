package tech.finovy.framework.common.core.loader;

import org.junit.jupiter.api.Test;
import tech.finovy.framework.common.core.exception.EnhancedServiceNotFoundException;
import tech.finovy.framework.common.core.Person;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EnhancedServiceLoaderTest {

    Person p = new Person();

    @Test
    void testLoad1() {
        // Setup
        final ClassLoader loader = ClassLoader.getSystemClassLoader();

        // Run the test
        final Person result = EnhancedServiceLoader.load(Person.class, loader);

        // Verify the results
        assertThat(result).isNotNull();
    }

    @Test
    void testLoad1_ThrowsEnhancedServiceNotFoundException() {
        // Setup
        final ClassLoader loader = ClassLoader.getSystemClassLoader();

        // Run the test
        assertThatThrownBy(() -> EnhancedServiceLoader.load(String.class, loader))
                .isInstanceOf(EnhancedServiceNotFoundException.class);
    }

    @Test
    void testLoad2() {
        assertThat(EnhancedServiceLoader.load(Person.class)).isNotNull();
        assertThatThrownBy(() -> EnhancedServiceLoader.load(String.class))
                .isInstanceOf(EnhancedServiceNotFoundException.class);
    }

    @Test
    void testLoad3() {
        assertThat(EnhancedServiceLoader.load(Person.class, "person")).isNotNull();
        assertThatThrownBy(() -> EnhancedServiceLoader.load(Person.class, "No"))
                .isInstanceOf(EnhancedServiceNotFoundException.class);
    }

    @Test
    void testLoad4() {
        // Setup
        final ClassLoader loader = ClassLoader.getSystemClassLoader();

        // Run the test
        final Person result = EnhancedServiceLoader.load(Person.class, "person", loader);

        // Verify the results
        assertThat(result).isNotNull();
    }

    @Test
    void testLoad4_ThrowsEnhancedServiceNotFoundException() {
        // Setup
        final ClassLoader loader = ClassLoader.getSystemClassLoader();

        // Run the test
        assertThatThrownBy(() -> EnhancedServiceLoader.load(String.class, "person", loader))
                .isInstanceOf(EnhancedServiceNotFoundException.class);
    }

    @Test
    void testLoad5() {
        assertThat(EnhancedServiceLoader.load(Person.class, "person", new Object[]{"args"})).isNotNull();
        assertThatThrownBy(
                () -> EnhancedServiceLoader.load(Person.class, "No", new Object[]{"args"}))
                .isInstanceOf(EnhancedServiceNotFoundException.class);
    }

    @Test
    void testLoad6() {
        assertThat(EnhancedServiceLoader.load(Person.class, "person", new Class<?>[]{Person.class},
                new Object[]{"args"})).isNotNull();
        assertThatThrownBy(() -> EnhancedServiceLoader.load(Person.class, "No", new Class<?>[]{Person.class},
                new Object[]{"args"})).isInstanceOf(EnhancedServiceNotFoundException.class);
    }

    @Test
    void testLoadAll1() {
        assertThat(EnhancedServiceLoader.loadAll(Person.class)).isNotNull();
    }

    @Test
    void testLoadAll2() {
//        assertThat(EnhancedServiceLoader.loadAll(Person.class, new Class<?>[]{Person.class},
//                new Object[]{"args"})).isNotNull();
    }

    @Test
    void testUnloadAll() {
        // Setup
        // Run the test
        EnhancedServiceLoader.unloadAll();

        // Verify the results
    }

    @Test
    void testUnload1() {
        // Setup
        // Run the test
        EnhancedServiceLoader.unload(String.class);

        // Verify the results
    }

    @Test
    void testUnload2() {
        // Setup
        // Run the test
        EnhancedServiceLoader.unload(String.class, "activateName");

        // Verify the results
    }

//    @Test
//    void testGetAllExtensionClass1() {
//        assertThat(EnhancedServiceLoader.getAllExtensionClass(String.class)).isEqualTo(List.of(String.class));
//        assertThat(EnhancedServiceLoader.getAllExtensionClass(String.class)).isEqualTo(Collections.emptyList());
//    }
//
//    @Test
//    void testGetAllExtensionClass2() {
//        // Setup
//        final ClassLoader loader = ClassLoader.getPlatformClassLoader();
//
//        // Run the test
//        final List<Class<String>> result = EnhancedServiceLoader.getAllExtensionClass(String.class, loader);
//
//        // Verify the results
//        assertThat(result).isEqualTo(List.of(String.class));
//    }
}
