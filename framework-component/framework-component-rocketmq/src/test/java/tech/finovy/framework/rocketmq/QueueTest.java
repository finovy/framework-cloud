package tech.finovy.framework.rocketmq;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import tech.finovy.framework.distributed.queue.api.QueueService;
import tech.finovy.framework.distributed.queue.entity.PushResult;
import tech.finovy.framework.distributed.queue.entity.QueueMessage;
import tech.finovy.framework.distributed.queue.entity.QueueSerialMessage;
import tech.finovy.framework.ratelimiter.DistributedRateLimiterFactoryManager;
import tech.finovy.framework.rocketmq.impl.QueueServiceImpl;

import java.util.Collection;

@Slf4j
public class QueueTest {

    private QueueService queueService;
    DefaultMQProducer defaultMqProducer;

    @BeforeEach
    public void init() {
    }

    @Test
    public void testQueueService() throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        defaultMqProducer = Mockito.mock(DefaultMQProducer.class);
        DistributedRateLimiterFactoryManager rateLimiterFactoryManager = Mockito.mock(DistributedRateLimiterFactoryManager.class);
        queueService = new QueueServiceImpl(defaultMqProducer, rateLimiterFactoryManager);
        // normal
        final SendResult sendResult = new SendResult();
        sendResult.setSendStatus(SendStatus.SEND_OK);
        Mockito.when(defaultMqProducer.send(Mockito.any(Message.class))).thenReturn(sendResult);
        Mockito.when(defaultMqProducer.send(Mockito.any(Collection.class))).thenReturn(sendResult);
        // case 1: push single
        QueueMessage<String> queueMessage = new QueueMessage<>();
        queueMessage.setTopic("unit-test");
        PushResult singleResult = queueService.push(queueMessage);
        Assertions.assertEquals(singleResult.getPushStatus(), SendStatus.SEND_OK.name());
        // case 2: push batch
        final PushResult batchResult = queueService.push(Lists.newArrayList(queueMessage, queueMessage));
        Assertions.assertEquals(batchResult.getPushStatus(), SendStatus.SEND_OK.name());
        // case 3: pushSerial
        final QueueSerialMessage message = new QueueSerialMessage();
        message.setTopic("unit-test");
        message.setBody("for test");
        final PushResult serialSingleResult = queueService.pushSerial(message);
        Assertions.assertEquals(serialSingleResult.getPushStatus(), SendStatus.SEND_OK.name());
        // case 4: pushSerial batch
        final PushResult serialBatchResult = queueService.pushSerial(Lists.newArrayList(message, message));
        Assertions.assertEquals(serialBatchResult.getPushStatus(), SendStatus.SEND_OK.name());
        // error-1
        Mockito.when(defaultMqProducer.send(Mockito.any(Message.class))).thenThrow(new MQClientException(401, "forbidden"));
        Mockito.when(defaultMqProducer.send(Mockito.any(Collection.class))).thenThrow(new MQClientException(401, "forbidden"));
        Mockito.when(rateLimiterFactoryManager.tryAcquire("QueueService", 5)).thenReturn(true);
        // case 5: pushSerial error
        final PushResult serialSingleResultError = queueService.pushSerial(message);
        Assertions.assertNotNull(serialSingleResultError.getErrMsg());
        // case 6: pushSerial batch error
        final PushResult serialBatchResultError = queueService.pushSerial(Lists.newArrayList(message, message));
        Assertions.assertNotNull(serialBatchResultError.getErrMsg());
        // error-2
        Mockito.when(defaultMqProducer.send(Mockito.any(Message.class))).thenThrow(new InterruptedException());
        Mockito.when(defaultMqProducer.send(Mockito.any(Collection.class))).thenThrow(new InterruptedException());
        Mockito.when(rateLimiterFactoryManager.tryAcquire("QueueService", 5)).thenReturn(true);
        // case 7: pushSerial error
        final PushResult serialSingleResultInterrupt = queueService.pushSerial(message);
        Assertions.assertNotNull(serialSingleResultInterrupt.getErrMsg());
        // case 8: pushSerial batch error
        final PushResult serialBatchResultInterrupt = queueService.pushSerial(Lists.newArrayList(message, message));
        Assertions.assertNotNull(serialBatchResultInterrupt.getErrMsg());
    }

}
