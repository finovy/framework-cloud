package tech.finovy.framework.distributed.event.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class PushEventResult extends EventTransaction implements Serializable {
    private static final long serialVersionUID = -4084033274651896757L;
    private String pushStatus;
    private String errMsg;
    private boolean mock;
    private String topic;
    private String tags;

    public PushEventResult() {
    }
}
