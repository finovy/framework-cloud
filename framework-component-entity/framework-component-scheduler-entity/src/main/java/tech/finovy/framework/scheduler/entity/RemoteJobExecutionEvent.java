package tech.finovy.framework.scheduler.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class RemoteJobExecutionEvent extends RemoteJobConfig {
    @Serial
    private static final long serialVersionUID = 3229030556379213592L;
    private String hostname;
    private String ip;
    private String taskId;
    private int shardingItem;
    private Date startTime ;
    private Date completeTime;
    private boolean success;
    private String failureCause;
    private String type;

    public RemoteJobExecutionEvent() {
    }
}
