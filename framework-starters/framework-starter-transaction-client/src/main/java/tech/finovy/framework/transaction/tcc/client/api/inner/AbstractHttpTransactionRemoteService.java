package tech.finovy.framework.transaction.tcc.client.api.inner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import tech.finovy.framework.transaction.tcc.client.auth.SecurityHelper;
import tech.finovy.framework.transaction.tcc.client.loadbalance.InstanceTable;
import tech.finovy.framework.transaction.tcc.client.log.Log;

import java.io.IOException;
import java.util.Map;

import static tech.finovy.framework.transaction.tcc.client.constant.Constants.SECRET_KEY;


public abstract class AbstractHttpTransactionRemoteService implements TransactionRemoteService {

    protected static final String GET = "get";
    protected static final String POST = "post";

    private static final PoolingHttpClientConnectionManager connectionManager;
    private static final CloseableHttpClient httpClient;

    static {
        connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(1000);
        connectionManager.setValidateAfterInactivity(5000);
        connectionManager.setDefaultMaxPerRoute(500);
        ConnectionKeepAliveStrategy keepAliveStrategy = new DefaultConnectionKeepAliveStrategy() {
            @Override
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                long keepAliveDuration = super.getKeepAliveDuration(response, context);
                if (keepAliveDuration == -1) {
                    // 如果没有通过响应头设置最大存活时间，则默认设置为30秒
                    keepAliveDuration = 30 * 1000;
                }
                return keepAliveDuration;
            }
        };
        httpClient = HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(20000)
                        .build())
                .setKeepAliveStrategy(keepAliveStrategy) // 设置连接的最大存活时间
                .build();
    }

    public <T1, T2> T1 request(String method, String host, String urlSuffix, Map<String, String> headers, T2 body, String jpath) {
        String url = (StringUtils.isEmpty(host) ? InstanceTable.getInstance().getUrl() : host) + urlSuffix;
        try {
            HttpRequestBase request;
            switch (method) {
                case GET -> request = new HttpGet(url);
                case POST -> {
                    request = new HttpPost(url);
                    if (ObjectUtils.isNotEmpty(body)) {
                        StringEntity requestEntity = new StringEntity(JSON.toJSONString(SecurityHelper.encrypt(System.getenv(SECRET_KEY), body)), ContentType.APPLICATION_JSON);
                        ((HttpPost) request).setEntity(requestEntity);
                    }
                }
                default -> throw new IllegalArgumentException("Invalid HTTP method: " + method);
            }
            if (headers != null && !headers.isEmpty()) {
                headers.forEach(request::addHeader);
            }
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK && StringUtils.isNotBlank(jpath)) {
                    String responseBody = EntityUtils.toString(response.getEntity());
                    if (StringUtils.isNotBlank(responseBody)) {
                        return (T1) JSONPath.extract(responseBody, jpath);
                    }
                }
                return null;
            }
        } catch (IOException e) {
            Log.warn("request exception:{}", e.getMessage(), e);
            return null;
        }
    }

}
