package tech.finovy.framework.distributed.event;


import tech.finovy.framework.distributed.event.api.AsyncEventService;
import tech.finovy.framework.distributed.event.entity.EventMessage;
import tech.finovy.framework.distributed.event.entity.EventSerialMessage;
import tech.finovy.framework.distributed.event.entity.PushEventResult;
import tech.finovy.framework.distributed.event.stub.AsyncEventServiceStubImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@Slf4j
@RunWith(MockitoJUnitRunner.Silent.class)
class TestAsyncCacheStub {
    @Mock
    private AsyncEventService asyncEventService;
    private AsyncEventServiceStubImpl asyncEventServiceStub;
    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        CompletableFuture<PushEventResult> cachePack=new CompletableFuture<>();
        asyncEventServiceStub=new AsyncEventServiceStubImpl(asyncEventService);
        doReturn(cachePack).when(asyncEventService).pushAsync(any(EventMessage.class));
        doReturn(cachePack).when(asyncEventService).pushSerialAsync(any(EventSerialMessage.class));
    }
    @Test
    @DisplayName("TestPush")
    void pushTest(){
        EventMessage cacheKey=new EventMessage();
        CompletableFuture<PushEventResult> result= asyncEventServiceStub.pushAsync(cacheKey);
        Assertions.assertNotNull(result);
    }
    @Test
    @DisplayName("TestPushSerial")
    void pushSerialTest(){
        EventSerialMessage cacheKey=new EventSerialMessage();
        CompletableFuture<PushEventResult> result=asyncEventServiceStub.pushSerialAsync(cacheKey);
        Assertions.assertNotNull(result);
    }
}
