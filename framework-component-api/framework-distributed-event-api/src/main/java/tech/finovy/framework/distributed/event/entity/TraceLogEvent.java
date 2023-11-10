package tech.finovy.framework.distributed.event.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class TraceLogEvent implements Serializable {
    private static final long serialVersionUID = -8113014397056682172L;

    private String timestamp;
    private String level;
    private String thread;
    private Map<String, String> mdc;
    private String logger;
    private String message;
    private String context;
    public TraceLogEvent() {
    }
}
