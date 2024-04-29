package tech.finovy.framework.distributed.event.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class RequestEvent extends EventTransaction implements Serializable {
    private static final long serialVersionUID = -8113014397056682172L;
    private String requestOriginalParams;
    private String requestParams;
    private String contentType;
    private String url;
    private String remoteAddress;
    private String httpMethod;
    private String scheme;
    private String host;
    private int port;
    private String requestId;
    private Map<String,String> headers;
    public RequestEvent() {
    }
}
