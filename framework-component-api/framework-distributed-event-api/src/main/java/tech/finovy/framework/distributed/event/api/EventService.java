package tech.finovy.framework.distributed.event.api;


import tech.finovy.framework.distributed.event.entity.EventMessage;
import tech.finovy.framework.distributed.event.entity.EventSerialMessage;
import tech.finovy.framework.distributed.event.entity.PushEventResult;

import java.io.Serializable;
import java.util.List;

public interface EventService {
    /** 推送消息到MQ
     * @param eventMessage
     * @return PushResult
     */
    PushEventResult push(EventMessage<? extends Serializable> eventMessage);

    /** 推送消息到MQ
     * @param eventSerialMessage
     * @return PushResult
     */
    PushEventResult pushSerial(EventSerialMessage eventSerialMessage);
    PushEventResult pushSerial(List<EventSerialMessage> eventSerialMessage);
}
