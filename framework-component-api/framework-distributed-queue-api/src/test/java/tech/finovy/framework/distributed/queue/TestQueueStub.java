package tech.finovy.framework.distributed.queue;



import tech.finovy.framework.distributed.queue.api.QueueService;
import tech.finovy.framework.distributed.queue.entity.PushResult;
import tech.finovy.framework.distributed.queue.entity.QueueMessage;
import tech.finovy.framework.distributed.queue.entity.QueueSerialMessage;
import tech.finovy.framework.distributed.queue.stub.QueueServiceStubImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@Slf4j
@RunWith(MockitoJUnitRunner.Silent.class)
class TestQueueStub {
    @Mock
    private QueueService queueService;
    private QueueServiceStubImpl queueServiceStub;
    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        PushResult pushResult=new PushResult();
        queueServiceStub=new QueueServiceStubImpl(queueService);
        doReturn(pushResult).when(queueService).push(any(QueueMessage.class));
        doReturn(pushResult).when(queueService).pushSerial(any(QueueSerialMessage.class));
        doReturn(pushResult).when(queueService).push(any(List.class));
        doReturn(pushResult).when(queueService).pushSerial(any(List.class));
    }
    @Test
    @DisplayName("TestPushQueue")
    void pushQueueTest(){
        QueueMessage q=new QueueMessage();
        PushResult result= queueServiceStub.push(q);
        Assertions.assertNull(result.getErrMsg());
    }
    @Test
    @DisplayName("TestPushSerial")
    void pushSerialTest(){
        QueueSerialMessage q=new QueueSerialMessage();
        PushResult result= queueServiceStub.pushSerial(q);
        Assertions.assertNull(result.getErrMsg());
    }

    @Test
    @DisplayName("TestPushQueueBatch")
    void pushQueueBatchTest(){
        QueueMessage q=new QueueMessage();
        List<QueueMessage> m=new ArrayList<>();
        PushResult result= queueServiceStub.push(m);
        Assertions.assertNull(result.getErrMsg());
    }
    @Test
    @DisplayName("TestPushSerialBatch")
    void pushSeriaBatchlTest(){
        QueueSerialMessage q=new QueueSerialMessage();
        List<QueueSerialMessage> m=new ArrayList<>();
        PushResult result= queueServiceStub.pushSerial(m);
        Assertions.assertNull(result.getErrMsg());
    }

}
