package tech.finovy.framework.distributed.queue.entity;

import lombok.Data;

@Data
public class QueueSerialMessage extends QueueMessage<String> {
    private static final long serialVersionUID = 6053832234774577733L;
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
