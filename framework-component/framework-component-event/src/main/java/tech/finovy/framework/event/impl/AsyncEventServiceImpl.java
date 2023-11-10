package tech.finovy.framework.event.impl;

import tech.finovy.framework.distributed.event.api.AsyncEventService;
import tech.finovy.framework.distributed.event.api.EventService;
import tech.finovy.framework.distributed.event.entity.EventMessage;
import tech.finovy.framework.distributed.event.entity.EventSerialMessage;
import tech.finovy.framework.distributed.event.entity.PushEventResult;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

//@DubboService
//@ConditionalOnBean(RocketMqProducerConfig.class)
public class AsyncEventServiceImpl implements AsyncEventService {
    private final EventService eventService;

    public AsyncEventServiceImpl(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public CompletableFuture<PushEventResult> pushAsync(EventMessage<? extends Serializable> eventMessage) {
        return CompletableFuture.supplyAsync(() -> eventService.push(eventMessage));
    }

    @Override
    public CompletableFuture<PushEventResult> pushSerialAsync(EventSerialMessage eventSerialMessage) {
        return CompletableFuture.supplyAsync(() -> eventService.pushSerial(eventSerialMessage));
    }
}
