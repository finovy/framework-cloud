package tech.finovy.framework.disruptor.core.handler;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import tech.finovy.framework.disruptor.core.DisruptorEventConfiguration;
import tech.finovy.framework.disruptor.core.DisruptorEventContext;
import tech.finovy.framework.disruptor.core.event.DisruptorEvent;
import tech.finovy.framework.disruptor.core.event.DisruptorEventTranslator;

import java.util.Set;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class DisruptorQueueExceptionHandlerTest {

    @Mock
    private DisruptorEventConfiguration mockConfiguration;
    @Mock
    private DisruptorEventTranslator mockPublisher;
    @Mock
    private DisruptorEventContext mockContext;

    private DisruptorQueueExceptionHandler disruptorQueueExceptionHandlerUnderTest;

    private AutoCloseable mockitoCloseable;

    @BeforeEach
    void setUp() {
        mockitoCloseable = openMocks(this);
        disruptorQueueExceptionHandlerUnderTest = new DisruptorQueueExceptionHandler(mockConfiguration, mockPublisher,
                mockContext);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockitoCloseable.close();
    }

    @Test
    void testHandleEventException() {
        // Setup
        final DisruptorEvent<String> event = new DisruptorEvent<>();
        event.setExecuteListener(Set.of("value"));
        event.setEventRetryCount(0);
        event.setApplication("application");
        event.setEventTags("eventTags");
        event.setEventTopic("eventTopic");
        event.setEventId(0L);
        event.setTransactionId("transactionId");
        when(mockContext.getRingBuffer()).thenReturn(null);
        when(mockConfiguration.isDebug()).thenReturn(true);

        // Run the test
        disruptorQueueExceptionHandlerUnderTest.handleEventException(new Exception("message"), 0L, event);
        event.setEventRetryCount(2);
        disruptorQueueExceptionHandlerUnderTest.handleEventException(new Exception("message"), 0L, event);

        RingBuffer ringBuffer = new Disruptor<>(DisruptorEvent::new, 1024, r -> {
            Thread t = new Thread(r);
            t.setName("DisruptorThread" + "_" + 1);
            return t;
        }, ProducerType.MULTI, new SleepingWaitStrategy()).getRingBuffer();
        when(mockContext.getRingBuffer()).thenReturn(ringBuffer);
        disruptorQueueExceptionHandlerUnderTest.handleEventException(new Exception("message"), 0L, event);
    }


    @Test
    void testHandleOnStartException() {
        // Setup
        // Run the test
        disruptorQueueExceptionHandlerUnderTest.handleOnStartException(new Exception("message"));

        // Verify the results
    }

    @Test
    void testHandleOnShutdownException() {
        // Setup
        // Run the test
        disruptorQueueExceptionHandlerUnderTest.handleOnShutdownException(new Exception("message"));

        // Verify the results
    }

    @Test
    public void testConfiguration() {
        final DisruptorEventConfiguration disruptorEventConfiguration = new DisruptorEventConfiguration();
        Assertions.assertEquals(0, disruptorEventConfiguration.getWaitStrategy());
        Assertions.assertEquals("DisruptorEvent", disruptorEventConfiguration.getName());
        Assertions.assertEquals("DisruptorEvent", disruptorEventConfiguration.getApplicationName());
        Assertions.assertEquals(1, disruptorEventConfiguration.getRetryCount());
        Assertions.assertEquals(1024, disruptorEventConfiguration.getRingBufferSize());
        Assertions.assertEquals(24, disruptorEventConfiguration.getMaxAvailableProcessors());
    }
}
