package tech.finovy.framework.core.common.thread;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class NamedThreadFactoryTest {
    private static final int THREAD_TOTAL_SIZE = 3;
    private static final int DEFAULT_THREAD_PREFIX_COUNTER = 1;

    @Test
    public void testNewThread() {
        NamedThreadFactory namedThreadFactory = new NamedThreadFactory("testNameThread", 5);

        Thread testNameThread = namedThreadFactory
                .newThread(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
        assertThat(testNameThread.getName()).startsWith("testNameThread");
        assertThat(testNameThread.isDaemon()).isTrue();
    }

    @Test
    public void testConstructorWithPrefixAndDaemons() {
        NamedThreadFactory factory = new NamedThreadFactory("prefix", true);
        Thread thread = factory.newThread(() -> {
        });

        assertThat(thread.getName()).startsWith("prefix");
        assertThat(thread.isDaemon()).isTrue();
    }


    @Test
    public void testThreadNameHasCounterWithPrefixCounter() {
        NamedThreadFactory factory = new NamedThreadFactory("prefix", THREAD_TOTAL_SIZE, true);
        for (int i = 0; i < THREAD_TOTAL_SIZE; i++) {
            Thread thread = factory.newThread(() -> {
            });


            // the first _DEFAULT_THREAD_PREFIX_COUNTER is meaning thread counter
            assertThat("prefix_" + DEFAULT_THREAD_PREFIX_COUNTER + "_" + (i + 1) + "_" + THREAD_TOTAL_SIZE)
                    .isEqualTo(thread.getName());
        }
    }

    @Test
    public void testNamedThreadFactoryWithSecurityManager() {
        NamedThreadFactory factory = new NamedThreadFactory("testThreadGroup", true);
        Thread thread = factory.newThread(() -> {
        });
        assertThat(thread.getThreadGroup()).isNotNull();
    }

}
