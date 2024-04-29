package tech.finovy.framework.distributed.queue.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class QueueMessage<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = 6053832234774577733L;
    private String transactionId;
    private String topic;
    private String tags;
    private T body;
}
