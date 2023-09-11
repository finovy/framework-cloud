package tech.finovy.framework.logappender.push.http;

import tech.finovy.framework.logappender.conf.ClientConfiguration;
import tech.finovy.framework.logappender.entry.ResponseMessage;
import tech.finovy.framework.logappender.exception.ClientException;
import tech.finovy.framework.logappender.exception.ExceptionFactory;
import tech.finovy.framework.logappender.push.internals.IdleConnectionReaper;
import tech.finovy.framework.logappender.utils.HttpUtil;
import tech.finovy.framework.logappender.utils.IOUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AUTH;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DefaultServiceClient extends AbstractServiceClient {
    protected static HttpRequestFactory httpRequestFactory = new HttpRequestFactory();
    protected CloseableHttpClient httpClient;
    protected HttpClientConnectionManager connectionManager;
    protected RequestConfig requestConfig;
    protected CredentialsProvider credentialsProvider;
    protected HttpHost proxyHttpHost;
    protected AuthCache authCache;

    public DefaultServiceClient(ClientConfiguration config) {
        super(config);
        this.connectionManager = createHttpClientConnectionManager();
        this.httpClient = createHttpClient(this.connectionManager);
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        requestConfigBuilder.setConnectTimeout(config.getConnectionTimeout());
        requestConfigBuilder.setSocketTimeout(config.getSocketTimeout());
        requestConfigBuilder.setConnectionRequestTimeout(config.getConnectionRequestTimeout());
        String proxyHost = config.getProxyHost();
        int proxyPort = config.getProxyPort();
        if (proxyHost != null && proxyPort > 0) {
            this.proxyHttpHost = new HttpHost(proxyHost, proxyPort);
            requestConfigBuilder.setProxy(proxyHttpHost);
            String proxyUsername = config.getProxyUsername();
            String proxyPassword = config.getProxyPassword();
            String proxyDomain = config.getProxyDomain();
            String proxyWorkstation = config.getProxyWorkstation();
            if (proxyUsername != null && proxyPassword != null) {
                this.credentialsProvider = new BasicCredentialsProvider();
                this.credentialsProvider.setCredentials(new AuthScope(proxyHost, proxyPort), new NTCredentials(proxyUsername, proxyPassword, proxyWorkstation, proxyDomain));
                this.authCache = new BasicAuthCache();
                authCache.put(this.proxyHttpHost, new BasicScheme());
            }
        }
        this.requestConfig = requestConfigBuilder.build();
    }

    protected static ResponseMessage buildResponse(AbstractServiceClient.Request request, CloseableHttpResponse httpResponse) throws IOException {
        assert (httpResponse != null);
        ResponseMessage response = new ResponseMessage();
        response.setUrl(request.getUri());
        if (httpResponse.getStatusLine() != null) {
            response.setStatusCode(httpResponse.getStatusLine().getStatusCode());
        }
        if (httpResponse.getEntity() != null) {
            if (response.isSuccessful()) {
                response.setContent(httpResponse.getEntity().getContent());
            } else {
                readAndSetErrorResponse(httpResponse.getEntity().getContent(), response);
            }
        }
        for (Header header : httpResponse.getAllHeaders()) {
            if (HttpHeaders.CONTENT_LENGTH.equals(header.getName())) {
                response.setContentLength(Long.parseLong(header.getValue()));
            }
            response.addHeader(header.getName(), header.getValue());
        }
        HttpUtil.convertHeaderCharsetFromIso88591(response.getHeaders());
        return response;
    }

    private static void readAndSetErrorResponse(InputStream originalContent, ResponseMessage response) throws IOException {
        byte[] contentBytes = IOUtils.readStreamAsByteArray(originalContent);
        response.setErrorResponseAsString(new String(contentBytes));
        response.setContent(new ByteArrayInputStream(contentBytes));
    }

    @Override
    public ResponseMessage sendRequestCore(AbstractServiceClient.Request request, String charset) throws IOException {
        HttpRequestBase httpRequest = httpRequestFactory.createHttpRequest(request, charset);
        setProxyAuthorizationIfNeed(httpRequest);
        HttpClientContext httpContext = createHttpContext();
        httpContext.setRequestConfig(this.requestConfig);
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpRequest, httpContext);
        } catch (IOException ex) {
            httpRequest.abort();
            throw ExceptionFactory.createNetworkException(ex);
        }

        return buildResponse(request, httpResponse);
    }

    protected CloseableHttpClient createHttpClient(HttpClientConnectionManager connectionManager) {
        return HttpClients.custom().setConnectionManager(connectionManager).disableContentCompression().disableAutomaticRetries().build();
    }

    protected HttpClientConnectionManager createHttpClientConnectionManager() {
        SSLContext sslContext = null;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, (TrustStrategy) (chain, authType) -> true).build();

        } catch (Exception e) {
            throw new ClientException(e.getMessage());
        }
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register(Protocol.HTTP.toString(), PlainConnectionSocketFactory.getSocketFactory())
                .register(Protocol.HTTPS.toString(), sslSocketFactory).build();
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(config.getMaxConnections());
        poolingHttpClientConnectionManager.setMaxTotal(config.getMaxConnections());
        poolingHttpClientConnectionManager.setValidateAfterInactivity(config.getValidateAfterInactivity());
        poolingHttpClientConnectionManager.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(config.getSocketTimeout()).setTcpNoDelay(true).build());
        if (config.isUseReaper()) {
            IdleConnectionReaper.setIdleConnectionTime(config.getIdleConnectionTime());
            IdleConnectionReaper.registerConnectionManager(poolingHttpClientConnectionManager);
        }
        return poolingHttpClientConnectionManager;
    }

    protected HttpClientContext createHttpContext() {
        HttpClientContext httpContext = HttpClientContext.create();
        httpContext.setRequestConfig(this.requestConfig);
        if (this.credentialsProvider != null) {
            httpContext.setCredentialsProvider(this.credentialsProvider);
            httpContext.setAuthCache(this.authCache);
        }
        return httpContext;
    }

    private void setProxyAuthorizationIfNeed(HttpRequestBase httpRequest) {
        if (this.credentialsProvider != null) {
            String auth = this.config.getProxyUsername() + ":" + this.config.getProxyPassword();
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes());
            String authHeader = "Basic " + new String(encodedAuth);
            httpRequest.addHeader(AUTH.PROXY_AUTH_RESP, authHeader);
        }
    }

    @Override
    public void shutdown() {
        IdleConnectionReaper.removeConnectionManager(this.connectionManager);
        this.connectionManager.shutdown();
    }
}
