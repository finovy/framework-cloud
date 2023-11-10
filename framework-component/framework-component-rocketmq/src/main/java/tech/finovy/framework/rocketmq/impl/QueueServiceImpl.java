package tech.finovy.framework.rocketmq.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.finovy.framework.distributed.queue.api.QueueService;
import tech.finovy.framework.distributed.queue.entity.PushResult;
import tech.finovy.framework.distributed.queue.entity.QueueMessage;
import tech.finovy.framework.distributed.queue.entity.QueueSerialMessage;
import tech.finovy.framework.ratelimiter.DistributedRateLimiterFactoryManager;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class QueueServiceImpl implements QueueService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final DefaultMQProducer defaultMqProducer;
    private final DistributedRateLimiterFactoryManager rateLimiterFactoryManager;

    public QueueServiceImpl(DefaultMQProducer defaultMqProducer, DistributedRateLimiterFactoryManager rateLimiterFactoryManager) {
        this.defaultMqProducer = defaultMqProducer;
        this.rateLimiterFactoryManager = rateLimiterFactoryManager;
    }

    private static final String RATE_LIMITER_RESOURCE = "QueueService";

    @Override
    public PushResult push(QueueMessage queueMessage) {
        QueueSerialMessage serialMessage = new QueueSerialMessage();
        String body = JSON.toJSONString(queueMessage.getBody(), SerializerFeature.WriteSlashAsSpecial);
        serialMessage.setBody(body);
        serialMessage.setTags(queueMessage.getTags());
        serialMessage.setTransactionId(queueMessage.getTransactionId());
        serialMessage.setTopic(queueMessage.getTopic());
        return pushSerial(serialMessage);
    }

    @Override
    public PushResult push(List<QueueMessage> queueMessage) {
        List<QueueSerialMessage> batch = new ArrayList<>();
        for (QueueMessage ms : queueMessage) {
            QueueSerialMessage serialMessage = new QueueSerialMessage();
            String body = JSON.toJSONString(ms.getBody(), SerializerFeature.WriteSlashAsSpecial);
            serialMessage.setBody(body);
            serialMessage.setTags(ms.getTags());
            serialMessage.setTransactionId(ms.getTransactionId());
            serialMessage.setTopic(ms.getTopic());
            batch.add(serialMessage);
        }
        return pushSerial(batch);
    }

    @Override
    public PushResult pushSerial(QueueSerialMessage queueSerialMessage) {
        PushResult pushResult = new PushResult();
        pushResult.setTags(queueSerialMessage.getTags());
        pushResult.setTopic(queueSerialMessage.getTopic());
        pushResult.setTransactionId(queueSerialMessage.getTransactionId());
        try {
            Message msg = new Message(queueSerialMessage.getTopic(), queueSerialMessage.getTags(),
                    queueSerialMessage.getBody().getBytes(StandardCharsets.UTF_8)
            );
            msg.setTransactionId(queueSerialMessage.getTransactionId());
            SendResult sendResult = defaultMqProducer.send(msg);
            pushResult.setPushStatus(sendResult.getSendStatus().name());
        } catch (MQClientException | RemotingException | MQBrokerException e) {
            pushResult.setErrMsg(e.toString());
            if (rateLimiterFactoryManager.tryAcquire(RATE_LIMITER_RESOURCE, 5)) {
                LOGGER.error("Topic:{},Tags:{},TransactionId:{},MQ:{},error:{}", queueSerialMessage.getTopic(), queueSerialMessage.getTags(), queueSerialMessage.getTransactionId(), defaultMqProducer.getNamesrvAddr(), e.toString());
            }

        } catch (InterruptedException e) {
            pushResult.setErrMsg(e.toString());
            if (rateLimiterFactoryManager.tryAcquire(RATE_LIMITER_RESOURCE, 5)) {
                LOGGER.error("Topic:{},Tags:{},TransactionId:{},MQ:{},error:{}", queueSerialMessage.getTopic(), queueSerialMessage.getTags(), queueSerialMessage.getTransactionId(), defaultMqProducer.getNamesrvAddr(), e.toString());
            }
            Thread.currentThread().interrupt();
        }
        return pushResult;
    }

    @Override
    public PushResult pushSerial(List<QueueSerialMessage> queueSerialMessage) {
        List<Message> msgs = new ArrayList<>();
        PushResult pushResult = new PushResult();
        try {
            for (QueueSerialMessage serialMessage : queueSerialMessage) {
                Message msg = new Message(serialMessage.getTopic(), serialMessage.getTags(),
                        serialMessage.getBody().getBytes(StandardCharsets.UTF_8)
                );
                msg.setTransactionId(serialMessage.getTransactionId());
                msgs.add(msg);
            }
            SendResult sendResult = defaultMqProducer.send(msgs);
            pushResult.setPushStatus(sendResult.getSendStatus().name());
        } catch (MQClientException | RemotingException | MQBrokerException e) {
            pushResult.setErrMsg(e.toString());
            if (rateLimiterFactoryManager.tryAcquire(RATE_LIMITER_RESOURCE, 5)) {
                LOGGER.error("PushSerialBatch,error:{}", e.toString());
            }
        } catch (InterruptedException e) {
            pushResult.setErrMsg(e.toString());
            if (rateLimiterFactoryManager.tryAcquire(RATE_LIMITER_RESOURCE, 5)) {
                LOGGER.error("PushSerialBatch:{},MQ:{},error:{}", defaultMqProducer.getNamesrvAddr(), e.getMessage(), e);
            }
            Thread.currentThread().interrupt();
        }
        return pushResult;
    }
}
