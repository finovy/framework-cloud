package tech.finovy.framework.distributed.event.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResponseEvent extends EventTransaction implements Serializable {
    private static final long serialVersionUID = 6929429062342198607L;
    private String statusCode;
    private String routeId;
    private long spends;
    private String filterType;
    private String body;
    private String originalBody;
    private String contentType;
    private String responseCompresssor;
    private String acceptCompresssor;
    private Map<String,String> headers;
    public ResponseEvent() {
    }
}
