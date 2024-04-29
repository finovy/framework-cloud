package tech.finovy.framework.distributed.event.api;


import tech.finovy.framework.distributed.event.entity.EventMessage;
import tech.finovy.framework.distributed.event.entity.EventSerialMessage;
import tech.finovy.framework.distributed.event.entity.PushEventResult;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

public interface AsyncEventService {


    /** 推送消息到MQ
     * @param eventMessage
     * @return PushResult
     */
    CompletableFuture<PushEventResult> pushAsync(EventMessage<? extends Serializable> eventMessage);

    /** 推送消息到MQ
     * @param eventSerialMessage
     * @return PushResult
     */
    CompletableFuture<PushEventResult> pushSerialAsync(EventSerialMessage eventSerialMessage);

}
