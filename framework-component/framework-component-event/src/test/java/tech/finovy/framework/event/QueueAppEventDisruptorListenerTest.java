package tech.finovy.framework.event;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import tech.finovy.framework.disruptor.core.event.DisruptorEvent;
import tech.finovy.framework.disruptor.core.exception.DisruptorException;
import tech.finovy.framework.disruptor.spi.DisruptorEngine;
import tech.finovy.framework.distributed.event.entity.EventMessage;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class QueueAppEventDisruptorListenerTest {

    @Mock
    private DefaultMQProducer mockDefaultMqProducer;
    @Mock
    private EventProperties mockEventproperties;

    private QueueAppEventDisruptorListener queueAppEventDisruptorListenerUnderTest;

    private AutoCloseable mockitoCloseable;

    @BeforeEach
    void setUp() {
        mockitoCloseable = openMocks(this);
        queueAppEventDisruptorListenerUnderTest = new QueueAppEventDisruptorListener(mockDefaultMqProducer,
                mockEventproperties);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockitoCloseable.close();
    }

    @Test
    void testOnEvent() throws Exception {
        // Setup
        final DisruptorEvent event = new DisruptorEvent<>();
        event.setExecuteListener(Set.of("value"));
        event.setEventRetryCount(0);
        event.setApplication("application");
        event.setEventTags("eventTags");
        event.setEventTopic("eventTopic");
        event.setEventId(0L);
        event.setTransactionId("transactionId");
        // message
        final EventMessage<String> eventMessage = new EventMessage<>();
        eventMessage.setTopic("topic");
        eventMessage.setTags("tags");
        eventMessage.setApplication("application");
        eventMessage.setBody("value");
        eventMessage.setTransactionHeaders("key", "value");
        eventMessage.setMessageQueueCompressor("gz");
        event.setDisruptorEvent(eventMessage);
        // Run the test
        queueAppEventDisruptorListenerUnderTest.onEvent(event, 0);

        // Verify the results
        verify(mockDefaultMqProducer).sendOneway(any(Message.class));

        doThrow(RemotingException.class).when(mockDefaultMqProducer).sendOneway(any(Message.class));
        when(mockEventproperties.getLogRateLimiter()).thenReturn(5.0);
        assertThrows(DisruptorException.class,()->queueAppEventDisruptorListenerUnderTest.onEvent(event, 0));
    }

    @Test
    void testGetType() {
        assertEquals("SYS_QUEUE_APP_EVENT_MQ_TYPE", queueAppEventDisruptorListenerUnderTest.getType());
    }
}
