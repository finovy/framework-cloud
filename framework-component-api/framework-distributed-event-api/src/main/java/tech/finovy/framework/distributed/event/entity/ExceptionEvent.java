package tech.finovy.framework.distributed.event.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class ExceptionEvent extends EventTransaction implements Serializable {
    private static final long serialVersionUID = -3592155479357358658L;
    private String errMsg;
    private String exceptionType;
    private String statusCode;

    public ExceptionEvent() {
    }
}
