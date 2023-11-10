package tech.finovy.framework.distributed.event.mock;

import tech.finovy.framework.distributed.event.EventConstant;
import tech.finovy.framework.distributed.event.api.EventService;
import tech.finovy.framework.distributed.event.entity.EventMessage;
import tech.finovy.framework.distributed.event.entity.EventSerialMessage;
import tech.finovy.framework.distributed.event.entity.PushEventResult;

import java.io.Serializable;
import java.util.List;

public class EventServiceMockImpl implements EventService {

    @Override
    public PushEventResult push(EventMessage<? extends Serializable> queueMessage) {
        PushEventResult pushEventResult=new PushEventResult();
        pushEventResult.setErrMsg(EventConstant.NOPROVIDER);
        pushEventResult.setMock(true);
        return pushEventResult;
    }

    @Override
    public PushEventResult pushSerial(EventSerialMessage queueSerialMessage) {
        return creatPushEventResult();
    }

    @Override
    public PushEventResult pushSerial(List<EventSerialMessage> eventSerialMessage) {
        return creatPushEventResult();
    }
    private PushEventResult creatPushEventResult(){
        PushEventResult pushEventResult=new PushEventResult();
        pushEventResult.setErrMsg(EventConstant.NOPROVIDER);
        pushEventResult.setMock(true);
        return pushEventResult;
    }
}
