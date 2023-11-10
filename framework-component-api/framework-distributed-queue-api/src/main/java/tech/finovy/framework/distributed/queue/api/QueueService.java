package tech.finovy.framework.distributed.queue.api;

import tech.finovy.framework.distributed.queue.entity.PushResult;
import tech.finovy.framework.distributed.queue.entity.QueueMessage;
import tech.finovy.framework.distributed.queue.entity.QueueSerialMessage;

import java.io.Serializable;
import java.util.List;

public interface QueueService {
    PushResult push(QueueMessage<? extends Serializable> queueMessage);

    PushResult push(List<QueueMessage> queueMessage);

    PushResult pushSerial(QueueSerialMessage queueSerialMessage);

    PushResult pushSerial(List<QueueSerialMessage> queueSerialMessage);
}
