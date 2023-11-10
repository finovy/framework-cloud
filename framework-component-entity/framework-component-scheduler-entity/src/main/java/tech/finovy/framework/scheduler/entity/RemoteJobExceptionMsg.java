package tech.finovy.framework.scheduler.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class RemoteJobExceptionMsg extends RemoteJobExecuteConfig {
    @Serial
    private static final long serialVersionUID = 4020142617477964160L;
    private String errMsg;

    public RemoteJobExceptionMsg() {
    }
}
