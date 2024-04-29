package tech.finovy.framework.distributed.event.stub;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import tech.finovy.framework.distributed.event.EventConstant;
import tech.finovy.framework.distributed.event.api.AsyncEventService;
import tech.finovy.framework.distributed.event.entity.EventMessage;
import tech.finovy.framework.distributed.event.entity.EventSerialMessage;
import tech.finovy.framework.distributed.event.entity.PushEventResult;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class AsyncEventServiceStubImpl implements AsyncEventService {
    private AsyncEventService asyncEventService;
    public AsyncEventServiceStubImpl(AsyncEventService asyncEventService){
        this.asyncEventService=asyncEventService;
    }

    @Override
    public CompletableFuture<PushEventResult> pushAsync(EventMessage<? extends Serializable> eventMessage) {
        try {
            EventSerialMessage serialMessage=new EventSerialMessage();
            String body=JSON.toJSONString(eventMessage.getBody(), SerializerFeature.WriteSlashAsSpecial);
            serialMessage.setBody(body);
            serialMessage.setTransactionId(eventMessage.getTransactionId());
            serialMessage.setTopic(eventMessage.getTopic());
            serialMessage.setTags(eventMessage.getTags());
            return  asyncEventService.pushSerialAsync(serialMessage);
        } catch (Exception e) {
           log.info(e.toString());
        }
        return CompletableFuture.supplyAsync(() -> {
            PushEventResult pushEventResult=new PushEventResult();
            pushEventResult.setErrMsg(EventConstant.NOPROVIDER);
            pushEventResult.setMock(true);
            return pushEventResult;
        });

    }

    @Override
    public CompletableFuture<PushEventResult> pushSerialAsync(EventSerialMessage eventSerialMessage) {
        try {
            return  asyncEventService.pushSerialAsync(eventSerialMessage);
        } catch (Exception e) {
            log.info(e.toString());
        }
        return CompletableFuture.supplyAsync(() -> {
            PushEventResult pushEventResult=new PushEventResult();
            pushEventResult.setErrMsg(EventConstant.NOPROVIDER);
            pushEventResult.setMock(true);
            return pushEventResult;
        });
    }
}
