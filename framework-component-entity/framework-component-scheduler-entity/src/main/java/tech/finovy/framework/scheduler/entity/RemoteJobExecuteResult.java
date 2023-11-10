package tech.finovy.framework.scheduler.entity;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.Date;

@Data
@SuperBuilder
public class RemoteJobExecuteResult extends RemoteJobExecuteConfig {
    @Serial
    private static final long serialVersionUID = -891668256872069574L;
    private String result;
    private long spends;
    private String type;
    private boolean success;
    private String httpStatusCode;
    private Date time;

    public RemoteJobExecuteResult() {
    }
}
