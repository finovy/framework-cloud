package tech.finovy.framework.event.impl;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.finovy.framework.disruptor.core.event.DisruptorEvent;
import tech.finovy.framework.disruptor.core.exception.DisruptorException;
import tech.finovy.framework.disruptor.spi.DisruptorEngine;
import tech.finovy.framework.distributed.event.api.EventService;
import tech.finovy.framework.distributed.event.entity.EventMessage;
import tech.finovy.framework.distributed.event.entity.EventSerialMessage;
import tech.finovy.framework.distributed.event.entity.PushEventResult;
import tech.finovy.framework.event.EventProperties;
import tech.finovy.framework.event.QueueAppEventConstant;

import java.io.Serializable;
import java.util.List;

//@DubboService()
//@ConditionalOnProperty(name = "rocketmq.nameserver")
public class EventServiceImpl implements EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceImpl.class);

    private final EventProperties properties;
    private final DisruptorEngine disruptorService;

    public EventServiceImpl(EventProperties properties, DisruptorEngine disruptorService) {
        this.properties = properties;
        this.disruptorService = disruptorService;
    }

    @Override
    public PushEventResult push(EventMessage<? extends Serializable> eventMessage) {
        if(eventMessage==null){
            PushEventResult result=new PushEventResult();
            result.setErrMsg("Receive EventMessage IS NULL");
            LOGGER.warn(result.getErrMsg());
            return result;
        }
        return pushQueue(eventMessage);
    }

    @Override
    public PushEventResult pushSerial(EventSerialMessage eventSerialMessage) {
        if(eventSerialMessage==null){
            PushEventResult result=new PushEventResult();
            result.setErrMsg("Receive EventSerialMessage IS NULL");
            LOGGER.warn(result.getErrMsg());
            return result;
        }
        if (properties.isDebug()) {
            LOGGER.info("Application:{},TxID:{},Topic:{},Tag:{},TxHeaders:{},Body={}", eventSerialMessage.getApplication(),
                    eventSerialMessage.getTransactionId(), eventSerialMessage.getTopic(),
                    eventSerialMessage.getTags(),eventSerialMessage.getTransactionHeaders(), JSON.toJSONString(eventSerialMessage.getBody()));
        }
        PushEventResult r = new PushEventResult();
        DisruptorEvent<EventSerialMessage> disruptorEvent = new DisruptorEvent<>();
        disruptorEvent.setTransactionId(eventSerialMessage.getTransactionId());
        disruptorEvent.setDisruptorEventType(QueueAppEventConstant.SYS_QUEUE_APP_SERIAL_EVENT_MQ_TYPE);
        disruptorEvent.setDisruptorEvent(eventSerialMessage);
        disruptorEvent.setEventTopic(eventSerialMessage.getTopic());
        disruptorEvent.setEventTags(eventSerialMessage.getTags());
        disruptorEvent.setApplication(eventSerialMessage.getApplication());
        try {
            disruptorService.post(disruptorEvent);
        } catch (DisruptorException e) {
            r.setErrMsg(e.toString());
        }
        r.setPushStatus("SUCCESS");
        return r;
    }

    @Override
    public PushEventResult pushSerial(List<EventSerialMessage> eventSerialMessage) {
        if(CollectionUtils.isEmpty(eventSerialMessage)){
            PushEventResult r = new PushEventResult();
            r.setErrMsg("List<EventSerialMessage> is empty");
            return r;
        }
        PushEventResult r=null;
        for(EventSerialMessage serialMessage:eventSerialMessage){
            r= pushQueue(serialMessage);
        }
        return r;
    }


    private PushEventResult pushQueue(EventMessage<? extends Serializable> eventMessage) {
        PushEventResult r = new PushEventResult();
        DisruptorEvent<EventMessage> disruptorEvent = new DisruptorEvent();
        disruptorEvent.setTransactionId(eventMessage.getTransactionId());
        disruptorEvent.setDisruptorEventType(QueueAppEventConstant.SYS_QUEUE_APP_EVENT_MQ_TYPE);
        disruptorEvent.setDisruptorEvent(eventMessage);
        disruptorEvent.setEventTags(eventMessage.getTags());
        disruptorEvent.setEventTopic(eventMessage.getTopic());
        disruptorEvent.setApplication(eventMessage.getApplication());
        if (properties.isDebug()) {
            LOGGER.info("Application:{},TxID:{},Topic:{},Tag:{},TxHeaders:{},Body={}", eventMessage.getApplication(),
                    eventMessage.getTransactionId(), eventMessage.getTopic(),
                    eventMessage.getTags(),eventMessage.getTransactionHeaders(), JSON.toJSONString(eventMessage.getBody()));
        }
        try {
            disruptorService.post(disruptorEvent);
        } catch (DisruptorException e) {
            r.setErrMsg(e.toString());
        }
        r.setPushStatus("SUCCESS");
        return r;
    }
}
