package tech.finovy.framework.distributed.event;


import tech.finovy.framework.distributed.event.entity.EventMessage;
import tech.finovy.framework.distributed.event.entity.EventSerialMessage;
import tech.finovy.framework.distributed.event.entity.PushEventResult;
import tech.finovy.framework.distributed.event.mock.AsyncEventServiceMockImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RunWith(MockitoJUnitRunner.Silent.class)
class TestAsyncCacheMock {

    private AsyncEventServiceMockImpl cacheServiceStub;
    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        cacheServiceStub=new AsyncEventServiceMockImpl();
    }
    @Test
    @DisplayName("TestPush")
    void pushTest(){
        EventMessage cacheKey=new EventMessage();
        CompletableFuture<PushEventResult> result= cacheServiceStub.pushAsync(cacheKey);
        Assertions.assertNotNull(result);
    }
    @Test
    @DisplayName("TestPushSerial")
    void pushSerialTest(){
        EventSerialMessage cacheKey=new EventSerialMessage();
        CompletableFuture<PushEventResult> result=cacheServiceStub.pushSerialAsync(cacheKey);
        Assertions.assertNotNull(result);
    }

}
