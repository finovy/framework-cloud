package tech.finovy.framework.distributed.event.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
@EqualsAndHashCode(callSuper = true)
@Data
public class DatasourceEvent extends EventTransaction implements Serializable {

    private static final long serialVersionUID = -102146578226704420L;
    /**
     * 系统名称
     */
    private String applicationName;
    /**
     * 收集时间
     */
    private Timestamp collectTime;
    /**
     * 本机ip
     */
    private String hostName;
    private String eventType;
    private List<Map<String, Object>> input;
    public DatasourceEvent() {
    }
    public DatasourceEvent(String eventType,List<Map<String, Object>> input) {
        this.eventType=eventType;
        this.input=input;
    }
}
