package tech.finovy.framework.event.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import tech.finovy.framework.disruptor.core.event.DisruptorEvent;
import tech.finovy.framework.disruptor.spi.DisruptorEngine;
import tech.finovy.framework.distributed.event.entity.EventMessage;
import tech.finovy.framework.distributed.event.entity.EventSerialMessage;
import tech.finovy.framework.distributed.event.entity.PushEventResult;
import tech.finovy.framework.event.EventProperties;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class EventServiceImplTest {

    @Mock
    private EventProperties mockProperties;
    @Mock
    private DisruptorEngine mockDisruptorService;

    private EventServiceImpl eventServiceImplUnderTest;

    private AutoCloseable mockitoCloseable;

    @BeforeEach
    void setUp() {
        mockitoCloseable = openMocks(this);
        eventServiceImplUnderTest = new EventServiceImpl(mockProperties, mockDisruptorService);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockitoCloseable.close();
    }

    @Test
    void testPush() {
        // Setup
        final EventMessage<String> eventMessage = new EventMessage<>();
        eventMessage.setTransactionId("transactionId");
        eventMessage.setTransactionHeaders(Map.ofEntries(Map.entry("value", "value")));
        eventMessage.setTopic("eventTopic");
        eventMessage.setTags("eventTags");
        eventMessage.setApplication("application");
        eventMessage.setBody("value");

        final PushEventResult expectedResult = new PushEventResult();
        expectedResult.setTransactionId("transactionId");
        expectedResult.setTransactionHeaders(Map.ofEntries(Map.entry("value", "value")));
        expectedResult.setPushStatus("SUCCESS");
        expectedResult.setErrMsg("List<EventSerialMessage> is empty");
        expectedResult.setMock(false);
        expectedResult.setTopic("topic");
        expectedResult.setTags("tags");

        when(mockProperties.isDebug()).thenReturn(true);
        // Run the test
        final PushEventResult A = eventServiceImplUnderTest.push(eventMessage);
        // Verify the results
        assertEquals(expectedResult.getPushStatus(), A.getPushStatus());
        verify(mockDisruptorService).post(any(DisruptorEvent.class));
        // error branch
        final PushEventResult B = eventServiceImplUnderTest.push(null);
        Assertions.assertNotNull(B.getErrMsg());
    }

    @Test
    void testPushSerial1() {
        // Setup
        final EventSerialMessage eventSerialMessage = new EventSerialMessage();
        eventSerialMessage.setTransactionId("transactionId");
        eventSerialMessage.setTransactionHeaders(Map.ofEntries(Map.entry("value", "value")));
        eventSerialMessage.setTopic("eventTopic");
        eventSerialMessage.setTags("eventTags");
        eventSerialMessage.setApplication("application");
        eventSerialMessage.setBody(null);

        final PushEventResult expectedResult = new PushEventResult();
        expectedResult.setTransactionId("transactionId");
        expectedResult.setTransactionHeaders(Map.ofEntries(Map.entry("value", "value")));
        expectedResult.setPushStatus("SUCCESS");
        expectedResult.setErrMsg("List<EventSerialMessage> is empty");
        expectedResult.setMock(false);
        expectedResult.setTopic("topic");
        expectedResult.setTags("tags");

        when(mockProperties.isDebug()).thenReturn(true);

        // Run the test
        final PushEventResult A = eventServiceImplUnderTest.pushSerial(eventSerialMessage);

        // Verify the results
        assertEquals(expectedResult.getPushStatus(), A.getPushStatus());
        verify(mockDisruptorService).post(any(DisruptorEvent.class));

        // error branch
        final PushEventResult B = eventServiceImplUnderTest.push(null);
        Assertions.assertNotNull(B.getErrMsg());
    }

    @Test
    void testPushSerial2() {
        // Setup
        final EventSerialMessage eventSerialMessage1 = new EventSerialMessage();
        eventSerialMessage1.setTransactionId("transactionId");
        eventSerialMessage1.setTransactionHeaders(Map.ofEntries(Map.entry("value", "value")));
        eventSerialMessage1.setTopic("eventTopic");
        eventSerialMessage1.setTags("eventTags");
        eventSerialMessage1.setApplication("application");
        eventSerialMessage1.setBody(null);
        final List<EventSerialMessage> eventSerialMessage = List.of(eventSerialMessage1);
        final PushEventResult expectedResult = new PushEventResult();
        expectedResult.setTransactionId("transactionId");
        expectedResult.setTransactionHeaders(Map.ofEntries(Map.entry("value", "value")));
        expectedResult.setPushStatus("SUCCESS");
        expectedResult.setErrMsg("List<EventSerialMessage> is empty");
        expectedResult.setMock(false);
        expectedResult.setTopic("topic");
        expectedResult.setTags("tags");

        when(mockProperties.isDebug()).thenReturn(true);

        // Run the test
        final PushEventResult A = eventServiceImplUnderTest.pushSerial(eventSerialMessage);

        // Verify the results
        assertEquals(expectedResult.getPushStatus(), A.getPushStatus());
        verify(mockDisruptorService).post(any(DisruptorEvent.class));

        // error branch
        final PushEventResult B = eventServiceImplUnderTest.pushSerial(Collections.emptyList());
        Assertions.assertNotNull(B.getErrMsg());
    }
}
