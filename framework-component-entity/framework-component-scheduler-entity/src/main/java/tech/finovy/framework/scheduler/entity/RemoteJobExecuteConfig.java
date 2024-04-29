package tech.finovy.framework.scheduler.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class RemoteJobExecuteConfig extends RemoteJobConfig{
    @Serial
    private static final long serialVersionUID = 7569743505539291135L;
    private String taskId;
    private int shardingItem;
    private double rateLimiter;
    private String shardingParameter;
    private long executeIndex;
    private String applicationName;

    public RemoteJobExecuteConfig() {
    }
}
