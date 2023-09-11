package tech.finovy.framework.logappender.push.http;

import tech.finovy.framework.logappender.conf.ClientConfiguration;
import tech.finovy.framework.logappender.entry.AbstractHttpMessage;
import tech.finovy.framework.logappender.entry.RequestMessage;
import tech.finovy.framework.logappender.entry.ResponseMessage;
import tech.finovy.framework.logappender.exception.ClientException;
import tech.finovy.framework.logappender.exception.ServiceException;
import tech.finovy.framework.logappender.utils.Args;
import tech.finovy.framework.logappender.utils.HttpUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * @author dtype.huang
 */
public abstract class AbstractServiceClient {

    private static final int DEFAULT_MARK_LIMIT = 1024 * 4;
    protected ClientConfiguration config;

    protected AbstractServiceClient(ClientConfiguration config) {
        this.config = config;
    }

    public ResponseMessage sendRequest(RequestMessage request, String charset) throws ServiceException, ClientException {
        Args.notNull(request, "request");
        Args.notNullOrEmpty(charset, "charset");
        try {
            return sendRequestImpl(request, charset);
        } finally {
            try {
                request.close();
            } catch (IOException e) {
            }
        }
    }

    private ResponseMessage sendRequestImpl(RequestMessage request, String charset) throws ClientException, ServiceException {
        InputStream content = request.getContent();
        if (content != null && content.markSupported()) {
            content.mark(DEFAULT_MARK_LIMIT);
        }
        try {
            Request httpRequest = buildRequest(request, charset);
            return sendRequestCore(httpRequest, charset);
        } catch (ServiceException ex) {
            throw ex;
        } catch (ClientException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ClientException(ex.getMessage(), ex);
        }
    }

    protected abstract ResponseMessage sendRequestCore(Request request, String charset) throws Exception;

    private Request buildRequest(RequestMessage requestMessage, String charset) throws ClientException {
        Request request = new Request();
        request.setMethod(requestMessage.getMethod());
        request.setHeaders(requestMessage.getHeaders());
        if (request.getHeaders() != null) {
            HttpUtil.convertHeaderCharsetToIso88591(request.getHeaders());
        }
        final String delimiter = "/";
        String uri = requestMessage.getEndpoint().toString();
        if (!uri.endsWith(delimiter) && (requestMessage.getResourcePath() == null || !requestMessage.getResourcePath().startsWith(delimiter))) {
            uri += delimiter;
        }
        if (requestMessage.getResourcePath() != null) {
            uri += requestMessage.getResourcePath();
        }
        String paramString = HttpUtil.paramToQueryString(requestMessage.getParameters(), charset);
        boolean requestHasNoPayload = requestMessage.getContent() != null;
        boolean requestIsPost = requestMessage.getMethod() == HttpMethod.POST;
        boolean putParamsInUri = !requestIsPost || requestHasNoPayload;
        if (paramString != null && putParamsInUri) {
            uri += "?" + paramString;
        }
        request.setUrl(uri);
        if (requestIsPost && requestMessage.getContent() == null && paramString != null) {
            try {
                byte[] buf = paramString.getBytes(charset);
                ByteArrayInputStream content = new ByteArrayInputStream(buf);
                request.setContent(content);
                request.setContentLength(buf.length);
            } catch (UnsupportedEncodingException e) {
                throw new AssertionError("EncodingFailed" + e.getMessage());
            }
        } else {
            request.setContent(requestMessage.getContent());
            request.setContentLength(requestMessage.getContentLength());
        }
        return request;
    }

    public abstract void shutdown();

    public static class Request extends AbstractHttpMessage {
        private String uri;
        private HttpMethod method;

        public Request() {
        }

        public String getUri() {
            return this.uri;
        }

        public void setUrl(String uri) {
            this.uri = uri;
        }

        public HttpMethod getMethod() {
            return method;
        }

        public void setMethod(HttpMethod method) {
            this.method = method;
        }
    }
}
