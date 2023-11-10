package tech.finovy.framework.event.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import tech.finovy.framework.distributed.event.api.EventService;
import tech.finovy.framework.distributed.event.entity.EventMessage;
import tech.finovy.framework.distributed.event.entity.EventSerialMessage;
import tech.finovy.framework.distributed.event.entity.PushEventResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class AsyncEventServiceImplTest {

    @Mock
    private EventService mockEventService;

    private AsyncEventServiceImpl asyncEventServiceImplUnderTest;

    @BeforeEach
    void setUp() {
        initMocks(this);
        asyncEventServiceImplUnderTest = new AsyncEventServiceImpl(mockEventService);
    }

    @Test
    void testPushAsync() throws ExecutionException, InterruptedException {
        // Setup
        final EventMessage<String> eventMessage = new EventMessage<>();
        eventMessage.setTopic("topic");
        eventMessage.setTags("tags");
        eventMessage.setApplication("application");
        eventMessage.setBody("value");

        // Configure EventService.push(...).
        final PushEventResult pushEventResult = new PushEventResult();
        pushEventResult.setPushStatus("pushStatus");
        pushEventResult.setErrMsg("errMsg");
        pushEventResult.setMock(true);
        pushEventResult.setTopic("topic");
        pushEventResult.setTags("tags");
        when(mockEventService.push(eventMessage)).thenReturn(pushEventResult);

        // Run the test
        final CompletableFuture<PushEventResult> result = asyncEventServiceImplUnderTest.pushAsync(eventMessage);
        // Verify the results
        Assertions.assertTrue(result.get().isMock());
    }

    @Test
    void testPushSerialAsync() throws ExecutionException, InterruptedException {
        // Setup
        final EventSerialMessage eventSerialMessage = new EventSerialMessage();

        // Configure EventService.pushSerial(...).
        final PushEventResult pushEventResult = new PushEventResult();
        pushEventResult.setPushStatus("pushStatus");
        pushEventResult.setErrMsg("errMsg");
        pushEventResult.setMock(true);
        pushEventResult.setTopic("topic");
        pushEventResult.setTags("tags");
        when(mockEventService.pushSerial(eventSerialMessage)).thenReturn(pushEventResult);

        // Run the test
        final CompletableFuture<PushEventResult> result = asyncEventServiceImplUnderTest.pushSerialAsync(
                eventSerialMessage);
        // Verify the results
        Assertions.assertTrue(result.get().isMock());
    }
}
