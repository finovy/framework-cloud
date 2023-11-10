package tech.finovy.framework.elasticsearch;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicRequestLine;
import org.apache.http.message.BasicStatusLine;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import tech.finovy.framework.elasticsearch.api.ElasticSearchService;
import tech.finovy.framework.elasticsearch.entity.BulkRequest;
import tech.finovy.framework.elasticsearch.entity.DeleteRequest;
import tech.finovy.framework.elasticsearch.entity.QueryRequest;
import tech.finovy.framework.elasticsearch.entity.UpdateRequest;
import tech.finovy.framework.elasticsearch.exception.ElasticSearchException;
import tech.finovy.framework.elasticsearch.impl.ElasticSearchContext;
import tech.finovy.framework.elasticsearch.impl.ElasticSearchContextHolder;
import tech.finovy.framework.elasticsearch.impl.ElasticSearchServiceImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.List;

import static org.mockito.Mockito.doAnswer;

@Slf4j
public class ElasticsearchTest {

    private ElasticSearchService elasticSearchService;
    RestClient client;

    @BeforeEach
    public void init() {
        client = Mockito.mock(RestClient.class);
        ElasticSearchContextHolder.get().setClient(client);
        elasticSearchService = new ElasticSearchServiceImpl();
    }

    @Test
    @DisplayName("TestElasticSearchService")
    void testElasticsearchService() throws Exception {
        // 1.index
        QueryRequest queryRequest = new QueryRequest("GET", "");
        Mockito.when(client.performRequest(Mockito.any(Request.class))).thenReturn(mockResponse(200));
        final JSONObject index = elasticSearchService.index("/user");
        log.info("index:{}", index);
        Assertions.assertNotNull(index);
        // 2.deleteIndex
        Mockito.when(client.performRequest(Mockito.any(Request.class))).thenReturn(mockResponse(200));
        final JSONObject deleteIndex = elasticSearchService.deleteIndex("/user");
        log.info("deleteIndex:{}", deleteIndex);
        Assertions.assertNotNull(deleteIndex);
        // 3.get
        Mockito.when(client.performRequest(Mockito.any(Request.class))).thenReturn(mockResponse(200));
        queryRequest.setJsonEntity("{\"query\":{}}");
        queryRequest.setFrom(10);
        queryRequest.setSize(10);
        queryRequest.setFields("name", "age");
        JSONObject get = elasticSearchService.get(queryRequest);
        log.info("get:{}", get);
        Assertions.assertNotNull(get);
        // 4.update
        Mockito.when(client.performRequest(Mockito.any(Request.class))).thenReturn(mockResponse(200));
        final UpdateRequest updateRequest = new UpdateRequest("/user", "info", "1");
        final JSONObject updateResponse = elasticSearchService.update(updateRequest);
        log.info("updateResponse:{}", updateResponse);
        Assertions.assertNotNull(updateResponse);
        // 5.delete
        Mockito.when(client.performRequest(Mockito.any(Request.class))).thenReturn(mockResponse(200));
        final DeleteRequest deleteRequest = new DeleteRequest("/user", "info", "1");
        final JSONObject deleteResponse = elasticSearchService.delete(deleteRequest);
        log.info("deleteResponse:{}", deleteResponse);
        Assertions.assertNotNull(deleteResponse);
        // 6.batch
        // 设置 performRequestAsync 方法的行为
        doAnswer(invocation -> {
            Request request = invocation.getArgument(0);
            ResponseListener listener = invocation.getArgument(1);
            if (request.getMethod().equals("GET")) {
                listener.onSuccess(mockResponse(200));
            } else {
                listener.onFailure(new Exception("Simulated failure"));
            }
            return null;
        }).when(client).performRequestAsync(Mockito.any(Request.class), Mockito.any(ResponseListener.class));
        List<QueryRequest> queryRequests = Lists.newArrayList(new QueryRequest("GET", ""), new QueryRequest("UPDATE", ""));
        final List<QueryRequest> batchResponse = elasticSearchService.postBatch(queryRequests);
        log.info("batchResponse:{}", batchResponse);
        Assertions.assertNotNull(batchResponse);
        // 7. exist
        Mockito.when(client.performRequest(Mockito.any(Request.class))).thenReturn(mockResponse(200));
        final Boolean existIndex = elasticSearchService.isExistIndex("/user", "info");
        log.info("existIndex:{}", existIndex);
        Assertions.assertTrue(existIndex);
        // 8. bulk
        Mockito.when(client.performRequest(Mockito.any(Request.class))).thenReturn(mockResponse(200));
        List<BulkRequest> bulkRequests = Lists.newArrayList(new BulkRequest("/user", "info", "1", "PUT"), new BulkRequest("/user", "info", "1", "PUT"));
        final String bulk = elasticSearchService.bulk(bulkRequests);
        log.info("bulk:{}", bulk);
        Assertions.assertEquals("success", bulk);

        Mockito.when(client.performRequest(Mockito.any(Request.class))).thenReturn(mockResponse(404));
        final String bulkFail = elasticSearchService.bulk(bulkRequests);
        log.info("bulk:{}", bulkFail);
        Assertions.assertEquals("[Chunked: false]", bulkFail);
    }

    @SneakyThrows
    private Response mockResponse(int code) {
        Class<Response> responseClass = Response.class;
        Constructor<Response> constructor = responseClass.getDeclaredConstructor(RequestLine.class, HttpHost.class, HttpResponse.class);
        constructor.setAccessible(true);
        RequestLine requestLine = new BasicRequestLine("m", "u", new ProtocolVersion("A", 1, 1)); // 你需要提供相应的参数
        HttpHost host = new HttpHost("www.finovy.tech"); // 你需要提供相应的参数
        HttpResponse response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("A", 1, 1), code, code == 200 ? "Success" : "Fail"));
        final BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
        String str = "{\"code\":" + code + "}";
        byte[] bytes = str.getBytes();
        basicHttpEntity.setContent(new ByteArrayInputStream(bytes));
        response.setEntity(basicHttpEntity);
        return constructor.newInstance(requestLine, host, response);
    }

    @Test
    public void testErrorBranch() throws IOException {
        Mockito.when(client.performRequest(Mockito.any(Request.class))).thenThrow(new IOException());
        Assertions.assertThrows(ElasticSearchException.class, () -> elasticSearchService.index("/user"));

        QueryRequest queryRequest = new QueryRequest("GET", "");
        Mockito.when(client.performRequest(Mockito.any(Request.class))).thenThrow(new IOException());

        Assertions.assertThrows(ElasticSearchException.class, () -> elasticSearchService.get(queryRequest));

        final UpdateRequest updateRequest = new UpdateRequest("/user", "info", "1");
        Mockito.when(client.performRequest(Mockito.any(Request.class))).thenThrow(new IOException());
        Assertions.assertThrows(ElasticSearchException.class, () -> elasticSearchService.update(updateRequest));

        final DeleteRequest deleteRequest = new DeleteRequest("/user", "info", "1");
        Mockito.when(client.performRequest(Mockito.any(Request.class))).thenThrow(new IOException());
        Assertions.assertThrows(ElasticSearchException.class, () -> elasticSearchService.delete(deleteRequest));

        List<QueryRequest> queryRequests = Lists.newArrayList(new QueryRequest("GET", ""), new QueryRequest("DELETE", ""));
        Mockito.when(client.performRequestAsync(Mockito.any(Request.class), Mockito.any())).thenThrow(new RuntimeException());
        Assertions.assertThrows(ElasticSearchException.class, () -> elasticSearchService.postBatch(queryRequests));

        Mockito.when(client.performRequest(Mockito.any(Request.class))).thenThrow(new IOException("index_not_found_exception"));
        final Boolean existIndex = elasticSearchService.isExistIndex("/user", "info");
        log.info("existIndex:{}", existIndex);
        Assertions.assertFalse(existIndex);
    }
}
