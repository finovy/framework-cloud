package tech.finovy.framework.logappender.entry;

import tech.finovy.framework.logappender.push.http.HttpMethod;
import tech.finovy.framework.logappender.utils.Args;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class RequestMessage extends AbstractHttpMessage {
    private HttpMethod method = HttpMethod.GET;
    private URI endpoint;
    private String resourcePath;
    private Map<String, String> parameters = new HashMap<>();

    public RequestMessage() {
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public URI getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(URI endpoint) {
        this.endpoint = endpoint;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        Args.notNull(parameters, "parameters");
        this.parameters = parameters;
    }
}
