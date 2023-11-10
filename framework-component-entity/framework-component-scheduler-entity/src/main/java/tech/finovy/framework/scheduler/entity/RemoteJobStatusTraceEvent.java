package tech.finovy.framework.scheduler.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class RemoteJobStatusTraceEvent extends RemoteJobConfig {
    @Serial
    private static final long serialVersionUID = 6689003403190800483L;
    private String hostname;
    private String ip;
    private String taskId;
    private String shardingItem;
    private Date startTime ;
    private Date completeTime;
    private boolean success;
    private String failureCause;
    private String type;
    private String state;
    private String message;

    public RemoteJobStatusTraceEvent() {
    }
}
