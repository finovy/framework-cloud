package tech.finovy.framework.distributed.event.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class StartupEvent extends EventTransaction implements Serializable {

    private static final long serialVersionUID = -59809451327640605L;
    private String message;

    public StartupEvent() {
    }
}
