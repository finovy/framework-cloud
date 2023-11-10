package tech.finovy.framework.distributed.queue.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class PushResult implements Serializable {
    private static final long serialVersionUID = -4084033274651896757L;
    private String pushStatus;
    private String errMsg;
    private String transactionId;
    private String topic;
    private String tags;
}
