package tech.finovy.framework.distributed.queue.stub;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import tech.finovy.framework.distributed.queue.api.QueueService;
import tech.finovy.framework.distributed.queue.entity.PushResult;
import tech.finovy.framework.distributed.queue.entity.QueueMessage;
import tech.finovy.framework.distributed.queue.entity.QueueSerialMessage;

import java.util.ArrayList;
import java.util.List;


public class QueueServiceStubImpl implements QueueService {
    private QueueService queueService;
    public QueueServiceStubImpl(QueueService queueService){
        this.queueService=queueService;
    }
    @Override
    public PushResult push(QueueMessage queueMessage) {
        QueueSerialMessage serialMessage=new QueueSerialMessage();
        String body=JSON.toJSONString(queueMessage.getBody(), SerializerFeature.WriteSlashAsSpecial);
        serialMessage.setBody(body);
        serialMessage.setTransactionId(queueMessage.getTransactionId());
        serialMessage.setTopic(queueMessage.getTopic());
        serialMessage.setTags(queueMessage.getTags());
      return  queueService.pushSerial(serialMessage);
    }

    @Override
    public PushResult push(List<QueueMessage> queueMessage) {
        List<QueueSerialMessage> batch=new ArrayList<>();
        for(QueueMessage ms:queueMessage) {
            QueueSerialMessage serialMessage = new QueueSerialMessage();
            String body = JSON.toJSONString(ms.getBody(), SerializerFeature.WriteSlashAsSpecial);
            serialMessage.setBody(body);
            serialMessage.setTags(ms.getTags());
            serialMessage.setTransactionId(ms.getTransactionId());
            serialMessage.setTopic(ms.getTopic());
            batch.add(serialMessage);
        }
        return queueService.pushSerial(batch);
    }

    @Override
    public PushResult pushSerial(QueueSerialMessage queueSerialMessage) {
       return queueService.pushSerial(queueSerialMessage);
    }

    @Override
    public PushResult pushSerial(List<QueueSerialMessage> queueSerialMessage) {
        return queueService.pushSerial(queueSerialMessage);
    }
}
