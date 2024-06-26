package tech.finovy.framework.event;

import com.alibaba.fastjson2.JSON;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.finovy.framework.common.core.RateLimiterFactory;
import tech.finovy.framework.compress.CompressorFactory;
import tech.finovy.framework.disruptor.core.event.DisruptorEvent;
import tech.finovy.framework.disruptor.core.exception.DisruptorException;
import tech.finovy.framework.disruptor.core.listener.AbstractDisruptorListener;
import tech.finovy.framework.distributed.event.entity.EventMessage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

//@ConditionalOnProperty(name = "rocketmq.nameserver")
public class QueueAppEventDisruptorListener extends AbstractDisruptorListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueueAppEventDisruptorListener.class);

    private final DefaultMQProducer defaultMqProducer;

    private final EventProperties eventproperties;

    public QueueAppEventDisruptorListener(DefaultMQProducer defaultMqProducer, EventProperties eventproperties) {
        this.defaultMqProducer = defaultMqProducer;
        this.eventproperties = eventproperties;
//        engine.addProcessInterface(this);
    }

    @Override
    public void onEvent(DisruptorEvent event, int handlerId) {
        try {
            EventMessage eventMessage= (EventMessage) event.getEvent();
            byte[] mqMessage=JSON.toJSONString(eventMessage.getBody()).getBytes(StandardCharsets.UTF_8);
            if(StringUtils.isNotBlank(eventMessage.getMessageQueueCompressor())){
                mqMessage= CompressorFactory.compress(eventMessage.getMessageQueueCompressor(),mqMessage);
            }
            Message msg = new Message(eventMessage.getTopic(), eventMessage.getTags(),mqMessage);
            msg.setTransactionId(eventMessage.getTransactionId());
            Map<String,String> transactionHeaders=eventMessage.getTransactionHeaders();
            if(transactionHeaders!=null){
                for(Map.Entry<String,String> transHeader:transactionHeaders.entrySet()){
                    msg.putUserProperty(transHeader.getKey(),transHeader.getValue());
                }
            }
            msg.setTransactionId(eventMessage.getTransactionId());
            defaultMqProducer.sendOneway(msg);
        } catch (CompressorException | IOException |MQClientException | RemotingException |InterruptedException e) {
            if(RateLimiterFactory.tryAcquire(QueueAppEventConstant.RATE_LIMITER_RESOURCE, eventproperties.getLogRateLimiter())) {
                LOGGER.warn(e.toString());
            }
            Thread.currentThread().interrupt();
            throw new DisruptorException(e.toString());
        }
    }

    @Override
    public String getType() {
        return QueueAppEventConstant.SYS_QUEUE_APP_EVENT_MQ_TYPE;
    }
}
