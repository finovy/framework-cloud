package tech.finovy.framework.logappender.push.http;

import tech.finovy.framework.logappender.conf.ClientConfiguration;
import tech.finovy.framework.logappender.entry.ResponseMessage;
import tech.finovy.framework.logappender.exception.ClientException;
import tech.finovy.framework.logappender.exception.ErrorCodes;
import tech.finovy.framework.logappender.exception.ExceptionFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;

import java.io.IOException;
import java.util.concurrent.*;

public class TimeoutServiceClient extends DefaultServiceClient {
    private final ThreadPoolExecutor executor;

    public TimeoutServiceClient(ClientConfiguration config) {
        super(config);
        int processors = Runtime.getRuntime().availableProcessors();
        this.executor = createThreadPool(processors * 5, processors * 10, processors * 100);
    }

    public TimeoutServiceClient(ClientConfiguration config, int corePoolSize, int maximumPoolSize, int queueSize) {
        super(config);
        this.executor = createThreadPool(corePoolSize, maximumPoolSize, queueSize);
    }

    public TimeoutServiceClient(ClientConfiguration config, ThreadPoolExecutor executor) {
        super(config);
        this.executor = executor;
    }

    private ThreadPoolExecutor createThreadPool(int corePoolSize, int maximumPoolSize, int queueSize) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 60L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(queueSize),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }

    @Override
    public ResponseMessage sendRequestCore(AbstractServiceClient.Request request, String charset) throws IOException {

        HttpRequestBase httpRequest = httpRequestFactory.createHttpRequest(request, charset);
        HttpClientContext httpContext = HttpClientContext.create();
        httpContext.setRequestConfig(this.requestConfig);
        CloseableHttpResponse httpResponse = null;
        HttpRequestTask httpRequestTask = new HttpRequestTask(httpRequest, httpContext);
        Future<CloseableHttpResponse> future = executor.submit(httpRequestTask);
        try {
            httpResponse = future.get(this.config.getRequestTimeout(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            httpRequest.abort();
            throw new ClientException(e.getMessage(), e);
        } catch (ExecutionException e) {
            httpRequest.abort();
            throw ExceptionFactory.createNetworkException((IOException) e.getCause());
        } catch (TimeoutException e) {
            httpRequest.abort();
            throw new ClientException(e.getMessage(), ErrorCodes.REQUEST_TIMEOUT, "Unknown", e);
        }

        return buildResponse(request, httpResponse);
    }

    @Override
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(ClientConfiguration.DEFAULT_THREAD_POOL_WAIT_TIME, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
                executor.awaitTermination(ClientConfiguration.DEFAULT_THREAD_POOL_WAIT_TIME, TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException ie) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        super.shutdown();
    }

    class HttpRequestTask implements Callable<CloseableHttpResponse> {
        private final HttpRequestBase httpRequest;
        private final HttpClientContext httpContext;

        public HttpRequestTask(HttpRequestBase httpRequest, HttpClientContext httpContext) {
            this.httpRequest = httpRequest;
            this.httpContext = httpContext;
        }

        @Override
        public CloseableHttpResponse call() throws Exception {
            return httpClient.execute(httpRequest, httpContext);
        }
    }

}
