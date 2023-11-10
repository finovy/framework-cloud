package tech.finovy.framework.distributed.event.stub;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import tech.finovy.framework.distributed.event.api.EventService;
import tech.finovy.framework.distributed.event.entity.EventMessage;
import tech.finovy.framework.distributed.event.entity.EventSerialMessage;
import tech.finovy.framework.distributed.event.entity.PushEventResult;

import java.util.List;


public class EventServiceStubImpl implements EventService {
    private EventService queueService;
    public EventServiceStubImpl(EventService queueService){
        this.queueService=queueService;
    }
    @Override
    public PushEventResult push(EventMessage queueMessage) {
        EventSerialMessage serialMessage=new EventSerialMessage();
        String body=JSON.toJSONString(queueMessage.getBody(), SerializerFeature.WriteSlashAsSpecial);
        serialMessage.setBody(body);
        serialMessage.setTransactionId(queueMessage.getTransactionId());
        serialMessage.setTopic(queueMessage.getTopic());
        serialMessage.setTags(queueMessage.getTags());
      return  queueService.pushSerial(serialMessage);
    }

    @Override
    public PushEventResult pushSerial(EventSerialMessage queueSerialMessage) {
       return queueService.pushSerial(queueSerialMessage);
    }

    @Override
    public PushEventResult pushSerial(List<EventSerialMessage> eventSerialMessage) {
        return queueService.pushSerial(eventSerialMessage);
    }
}
