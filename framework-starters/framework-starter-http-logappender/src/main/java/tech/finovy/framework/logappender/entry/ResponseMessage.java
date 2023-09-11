package tech.finovy.framework.logappender.entry;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ResponseMessage extends AbstractHttpMessage {
    private static final int HTTP_SUCCESS_STATUS_CODE = 200;
    private String uri;
    private int statusCode;
    private byte[] body = null;
    private String errorResponseAsString;

    public String getUri() {
        return uri;
    }

    public void setUrl(String uri) {
        this.uri = uri;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isSuccessful() {
        return statusCode / 100 == HTTP_SUCCESS_STATUS_CODE / 100;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public byte[] getRawBody() {
        return this.body;
    }

    public String getStringBody() {
        return new String(this.body, StandardCharsets.UTF_8);
    }

    public String getErrorResponseAsString() {
        return errorResponseAsString;
    }

    public void setErrorResponseAsString(String errorResponseAsString) {
        this.errorResponseAsString = errorResponseAsString;
    }

    public String getRequestId() {
        final Map<String, String> headers = getHeaders();
        final String requestId = headers.get("x-log-requestid");
        return requestId == null ? "" : requestId;
    }
}
