package tech.finovy.framework.distributed.event.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class EventMessage<T extends Serializable> extends  EventTransaction implements Serializable {
    private static final long serialVersionUID = 6053832234774577733L;
    private String topic;
    private String tags;
    private String application;
    private T body;

    public EventMessage() {
    }
}
