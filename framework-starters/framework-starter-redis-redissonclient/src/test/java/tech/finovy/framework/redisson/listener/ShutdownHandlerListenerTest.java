package tech.finovy.framework.redisson.listener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.finovy.framework.disruptor.core.event.DisruptorEvent;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShutdownHandlerListenerTest {

    private ShutdownHandlerListener shutdownHandlerListenerUnderTest;

    @BeforeEach
    void setUp() {
        shutdownHandlerListenerUnderTest = new ShutdownHandlerListener();
    }

    @Test
    void testGetType() {
        assertEquals("SHUTDOWN_REDIS_EVENT_LISTENER_TYPE", shutdownHandlerListenerUnderTest.getType());
    }

    @Test
    void testOnEvent() {
        // Setup
        final DisruptorEvent<Integer> event = new DisruptorEvent<>();
        event.setExecuteListener(Set.of("value"));
        event.setEventRetryCount(0);
        event.setApplication("application");
        event.setEventTags("eventTags");
        event.setEventTopic("eventTopic");
        event.setEventId(0L);
        event.setTransactionId("transactionId");
        event.setDisruptorEvent(1);
        // Run the test
        shutdownHandlerListenerUnderTest.onEvent(event, 0);

        // Verify the results
    }
}
