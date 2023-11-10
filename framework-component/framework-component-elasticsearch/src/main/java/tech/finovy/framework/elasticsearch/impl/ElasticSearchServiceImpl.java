package tech.finovy.framework.elasticsearch.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.finovy.framework.elasticsearch.api.ElasticSearchService;
import tech.finovy.framework.elasticsearch.entity.BulkRequest;
import tech.finovy.framework.elasticsearch.entity.DeleteRequest;
import tech.finovy.framework.elasticsearch.entity.QueryRequest;
import tech.finovy.framework.elasticsearch.entity.UpdateRequest;
import tech.finovy.framework.elasticsearch.exception.ElasticSearchException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ElasticSearchServiceImpl implements ElasticSearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchServiceImpl.class);


     private final ElasticSearchContext context =  ElasticSearchContextHolder.get();


    @Override
    public JSONObject index(String index) {
        return index(index, "put");
    }

    @Override
    public JSONObject deleteIndex(String index) {
        return index(index, "delete");
    }

    private JSONObject index(String index, String method) {
        try {
            Request request = new Request(method, index);
            Response response = context.getClient().performRequest(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            return JSON.parseObject(responseBody);
        } catch (IOException e) {
            LOGGER.warn("Exception {}", e.getMessage(), e);
            throw new ElasticSearchException(e);
        }
    }

    @Override
    public JSONObject get(QueryRequest queryRequest) {
        try {
            Request query = new Request(queryRequest.getMethod(), queryRequest.getEndpoint());
            if (queryRequest.getJsonEntity() != null && !"".equals(queryRequest.getJsonEntity())) {
                JSONObject queryJsonObjet = JSON.parseObject(queryRequest.getJsonEntity());
                if (!queryJsonObjet.containsKey(QueryRequest.FROM_INDEX) && queryRequest.getFrom() > 0) {
                    queryJsonObjet.put(QueryRequest.FROM_INDEX, queryRequest.getFrom());
                }
                if (!queryJsonObjet.containsKey(QueryRequest.RESULT_SIZE) && queryRequest.getSize() > 0) {
                    queryJsonObjet.put(QueryRequest.RESULT_SIZE, queryRequest.getSize());
                }
                if (!queryRequest.getFields().isEmpty()) {
                    queryJsonObjet.put(QueryRequest.SOURCE, queryRequest.getFields());
                }
                query.setJsonEntity(queryJsonObjet.toJSONString());
            }
            Response queryResponse = context.getClient().performRequest(query);
            return JSON.parseObject(EntityUtils.toString(queryResponse.getEntity()));
        } catch (IOException e) {
            LOGGER.warn("Exception {}", e.getMessage(), e);
            throw new ElasticSearchException(e);
        }
    }

    @Override
    public JSONObject update(UpdateRequest updateRequest) {
        try {
            String endpoint = updateRequest.getIndex() + "/" + updateRequest.getDocType() + "/" + updateRequest.getId() + "_update";
            Request request = new Request("put", endpoint);
            request.setJsonEntity(updateRequest.getJsonEntity());
            Response response = context.getClient().performRequest(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            return JSON.parseObject(responseBody);
        } catch (IOException e) {
            LOGGER.warn("Exception {}", e.getMessage(), e);
            throw new ElasticSearchException(e);
        }
    }

    @Override
    public JSONObject delete(DeleteRequest deleteRequest) {
        try {
            String endpoint = deleteRequest.getIndex() + "/" + deleteRequest.getDocType() + "/" + deleteRequest.getId();
            Request request = new Request(DeleteRequest.DELETE, endpoint);
            Response response = context.getClient().performRequest(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            return JSON.parseObject(responseBody);
        } catch (IOException e) {
            LOGGER.warn("Exception {}", e.getMessage(), e);
            throw new ElasticSearchException(e);
        }
    }

    @Override
    public List<QueryRequest> postBatch(List<QueryRequest> queryRequests) {
        // 失败的请求
        List<QueryRequest> failList = new ArrayList<>();
        try {
            CountDownLatch latch = new CountDownLatch(queryRequests.size());
            queryRequests.forEach(queryRequest -> {
                Request request = new Request(queryRequest.getMethod(), queryRequest.getEndpoint());
                request.setJsonEntity(queryRequest.getJsonEntity());
                context.getClient().performRequestAsync(
                        request,
                        new ResponseListener() {
                            @Override
                            public void onSuccess(Response response) {
                                latch.countDown();
                            }

                            @Override
                            public void onFailure(Exception exception) {
                                LOGGER.error("postBatch failure: {}", exception.getMessage());
                                failList.add(queryRequest);
                                latch.countDown();
                            }
                        }
                );
            });
            latch.await();
        } catch (InterruptedException e) {
            LOGGER.warn("PostBatch Interrupted! {}", e.getMessage(), e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            LOGGER.warn("Exception {}", e.getMessage(), e);
            throw new ElasticSearchException(e);
        }
        return failList;
    }

    @Override
    public Boolean isExistIndex(String index, String type) {
        boolean ret = true;
        String sql = "{\"from\" : 0, \"size\" : 1}";
        QueryRequest queryRequestExist = new QueryRequest("GET", index + "/" + type + "/_search");
        queryRequestExist.setJsonEntity(sql);
        try {
            this.get(queryRequestExist);
        } catch (ElasticSearchException e) {
            if (e.getMessage().contains("index_not_found_exception")) {
                ret = false;
            }
        }
        return ret;
    }

    @Override
    public String bulk(List<BulkRequest> bulkRequests) throws IOException {
        Request request = new Request("POST", "_bulk");
        StringBuilder stringBuilder = new StringBuilder();
        for (BulkRequest bulkRequest : bulkRequests) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("_index", bulkRequest.getIndex());
            jsonObject.put("_type", bulkRequest.getDocType());
            jsonObject.put("_id", bulkRequest.getId());
            JSONObject json = new JSONObject();
            json.put(bulkRequest.getOperationType(), jsonObject);
            stringBuilder.append(json.toJSONString());
            stringBuilder.append("\n");
            stringBuilder.append(bulkRequest.getJsonEntity());
            stringBuilder.append("\n");
        }
        request.setJsonEntity(stringBuilder.toString());
        Response response = context.getClient().performRequest(request);
        if (response.getStatusLine().getStatusCode() == 200) {
            return "success";
        } else {
            LOGGER.warn("bulk es StatusCode:{}, Entity:{}", response.getStatusLine().getStatusCode(), response.getEntity().toString());
            return response.getEntity().toString();
        }
    }


}
