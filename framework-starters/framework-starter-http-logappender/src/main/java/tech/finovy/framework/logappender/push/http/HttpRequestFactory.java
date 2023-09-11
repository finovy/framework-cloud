package tech.finovy.framework.logappender.push.http;

import tech.finovy.framework.logappender.exception.ClientException;
import org.apache.http.client.methods.*;

import java.util.Map.Entry;

class HttpRequestFactory {

    public HttpRequestBase createHttpRequest(AbstractServiceClient.Request request, String charset) {
        String uri = request.getUri();
        HttpRequestBase httpRequest;
        HttpMethod method = request.getMethod();
        if (method == HttpMethod.POST) {
            HttpPost postMethod = new HttpPost(uri);
            if (request.getContent() != null) {
                postMethod.setEntity(new RepeatableInputStreamEntity(request));
            }
            httpRequest = postMethod;
        } else if (method == HttpMethod.PUT) {
            HttpPut putMethod = new HttpPut(uri);
            if (request.getContent() != null) {
                putMethod.setEntity(new RepeatableInputStreamEntity(request));
            }
            httpRequest = putMethod;
        } else if (method == HttpMethod.GET) {
            httpRequest = new HttpGet(uri);
        } else if (method == HttpMethod.DELETE) {
            httpRequest = new HttpDelete(uri);
        } else if (method == HttpMethod.HEAD) {
            httpRequest = new HttpHead(uri);
        } else if (method == HttpMethod.OPTIONS) {
            httpRequest = new HttpOptions(uri);
        } else {
            throw new ClientException("Unknown HTTP method name: " + method.toString());
        }
        configureRequestHeaders(request, charset, httpRequest);
        return httpRequest;
    }

    private void configureRequestHeaders(AbstractServiceClient.Request request, String charset, HttpRequestBase httpRequest) {
        for (Entry<String, String> entry : request.getHeaders().entrySet()) {
            if (entry.getKey().equalsIgnoreCase(HttpHeaders.CONTENT_LENGTH)) {
                continue;
            }
            httpRequest.addHeader(entry.getKey(), entry.getValue());
        }
        if (httpRequest.getHeaders(HttpHeaders.CONTENT_TYPE) == null || httpRequest.getHeaders(HttpHeaders.CONTENT_TYPE).length == 0) {
            httpRequest.addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded; " + "charset=" + charset.toLowerCase());
        }
    }
}
