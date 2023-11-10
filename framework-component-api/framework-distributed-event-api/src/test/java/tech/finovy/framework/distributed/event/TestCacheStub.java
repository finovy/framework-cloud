package tech.finovy.framework.distributed.event;


import tech.finovy.framework.distributed.event.api.EventService;
import tech.finovy.framework.distributed.event.entity.EventMessage;
import tech.finovy.framework.distributed.event.entity.EventSerialMessage;
import tech.finovy.framework.distributed.event.entity.PushEventResult;
import tech.finovy.framework.distributed.event.stub.EventServiceStubImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@Slf4j
@RunWith(MockitoJUnitRunner.Silent.class)
class TestCacheStub {
    @Mock
    private EventService eventService;
    private EventServiceStubImpl eventServiceMock;
    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        PushEventResult cachePack=new PushEventResult();
        cachePack.setErrMsg("mock");
        eventServiceMock=new EventServiceStubImpl(eventService);
        doReturn(cachePack).when(eventService).push(any(EventMessage.class));
        doReturn(cachePack).when(eventService).pushSerial(any(EventSerialMessage.class));
    }
    @Test
    @DisplayName("TestPush")
    void pushTest(){
        EventMessage cacheKey=new EventMessage();
        PushEventResult result= eventServiceMock.push(cacheKey);
        Assertions.assertNotNull(result.getErrMsg());
    }
    @Test
    @DisplayName("TestPushSerial")
    void pushSerialTest(){
        EventSerialMessage cacheKey=new EventSerialMessage();
        PushEventResult result=eventServiceMock.pushSerial(cacheKey);
        Assertions.assertNotNull(result.getErrMsg());
    }
}
