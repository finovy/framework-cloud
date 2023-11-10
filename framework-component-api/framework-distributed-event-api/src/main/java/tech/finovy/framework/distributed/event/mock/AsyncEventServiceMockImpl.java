package tech.finovy.framework.distributed.event.mock;

import tech.finovy.framework.distributed.event.EventConstant;
import tech.finovy.framework.distributed.event.api.AsyncEventService;
import tech.finovy.framework.distributed.event.entity.EventMessage;
import tech.finovy.framework.distributed.event.entity.EventSerialMessage;
import tech.finovy.framework.distributed.event.entity.PushEventResult;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class AsyncEventServiceMockImpl implements AsyncEventService {

    @Override
    public CompletableFuture<PushEventResult> pushAsync(EventMessage<? extends Serializable> queueMessage) {
        PushEventResult pushEventResult=new PushEventResult();
        pushEventResult.setErrMsg(EventConstant.NOPROVIDER);
        pushEventResult.setMock(true);
        return CompletableFuture.supplyAsync(() -> pushEventResult);
    }

    @Override
    public CompletableFuture<PushEventResult> pushSerialAsync(EventSerialMessage queueSerialMessage) {
         return CompletableFuture.supplyAsync(() -> {
            PushEventResult pushEventResult=new PushEventResult();
            pushEventResult.setErrMsg(EventConstant.NOPROVIDER);
            pushEventResult.setMock(true);
            return pushEventResult;
        });
    }
}
