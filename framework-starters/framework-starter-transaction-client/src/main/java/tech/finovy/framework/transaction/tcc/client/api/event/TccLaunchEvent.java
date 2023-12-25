package tech.finovy.framework.transaction.tcc.client.api.event;

import java.util.Map;

public class TccLaunchEvent<T> extends BaseEvent {
    private String typeName;

    private Map<String, String> headers;

    /**
     * 事件参数
     */
    private T param;

    public TccLaunchEvent(String typeName, Map<String, String> headers, T param) {
        this.typeName = typeName;
        this.headers = headers;
        this.param = param;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public T getParam() {
        return param;
    }

    public void setParam(T param) {
        this.param = param;
    }
}
