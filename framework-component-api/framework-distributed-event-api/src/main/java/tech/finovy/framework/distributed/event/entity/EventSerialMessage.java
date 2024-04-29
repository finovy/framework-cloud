package tech.finovy.framework.distributed.event.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
public class EventSerialMessage extends EventMessage<String> {

    @Serial
    private static final long serialVersionUID = 6053832234774577733L;
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public EventSerialMessage() {
    }
}
