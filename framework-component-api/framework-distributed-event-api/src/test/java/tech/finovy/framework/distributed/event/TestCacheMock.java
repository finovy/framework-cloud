package tech.finovy.framework.distributed.event;

import tech.finovy.framework.distributed.event.entity.EventMessage;
import tech.finovy.framework.distributed.event.entity.EventSerialMessage;
import tech.finovy.framework.distributed.event.entity.PushEventResult;
import tech.finovy.framework.distributed.event.mock.EventServiceMockImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@Slf4j
@RunWith(MockitoJUnitRunner.Silent.class)
class TestCacheMock {

    private EventServiceMockImpl cacheServiceStub;
    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        cacheServiceStub=new EventServiceMockImpl();
    }
    @Test
    @DisplayName("TestPush")
    void pushTest(){
        EventMessage cacheKey=new EventMessage();
        PushEventResult result= cacheServiceStub.push(cacheKey);
        Assertions.assertNotNull(result.getErrMsg());
        Assertions.assertTrue(result.isMock());
    }
    @Test
    @DisplayName("TestPushSerial")
    void pushSerialTest(){
        EventSerialMessage cacheKey=new EventSerialMessage();
        PushEventResult result=cacheServiceStub.pushSerial(cacheKey);
        Assertions.assertNotNull(result.getErrMsg());
        Assertions.assertTrue(result.isMock());
    }

}
