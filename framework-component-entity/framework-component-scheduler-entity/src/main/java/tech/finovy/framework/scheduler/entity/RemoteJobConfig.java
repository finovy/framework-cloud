package tech.finovy.framework.scheduler.entity;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
public class RemoteJobConfig  implements Serializable {
    private static final long serialVersionUID = 158436232857028391L;
    private String jobKey;
    private String jobName;
    private String jobParameter;
    private int shardingTotalCount;
    private boolean fetchData;

    public RemoteJobConfig() {
    }
}
