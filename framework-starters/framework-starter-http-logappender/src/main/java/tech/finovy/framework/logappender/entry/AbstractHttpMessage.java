package tech.finovy.framework.logappender.entry;

import tech.finovy.framework.logappender.push.internals.CaseInsensitiveMap;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author dtype.huang
 */
public abstract class AbstractHttpMessage {
    private Map<String, String> headers = new CaseInsensitiveMap<>();
    private InputStream content;
    private long contentLength;

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        assert (headers != null);
        this.headers = headers;
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public InputStream getContent() {
        return content;
    }

    public void setContent(InputStream content) {
        this.content = content;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public void close() throws IOException {
        if (content != null) {
            content.close();
            content = null;
        }
    }
}
