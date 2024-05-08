package tech.finovy.framework.elasticsearch.api;

import com.alibaba.fastjson2.JSONObject;
import tech.finovy.framework.elasticsearch.entity.BulkRequest;
import tech.finovy.framework.elasticsearch.entity.DeleteRequest;
import tech.finovy.framework.elasticsearch.entity.QueryRequest;
import tech.finovy.framework.elasticsearch.entity.UpdateRequest;

import java.io.IOException;
import java.util.List;

public interface ElasticSearchService {
    /**
     * common
     *
     * @param index index
     * @return es response
     */
    JSONObject index(String index);

    /**
     * delete
     *
     * @param index index
     * @return es response
     */
    JSONObject deleteIndex(String index);

    /**
     * get
     *
     * @param queryRequest request obj
     * @return es response
     */
    JSONObject get(QueryRequest queryRequest);

    /**
     * update
     *
     * @param updateRequest request obj
     * @return es response
     */
    JSONObject update(UpdateRequest updateRequest);

    /**
     * delete
     *
     * @param deleteRequest request
     * @return es response
     */
    JSONObject delete(DeleteRequest deleteRequest);

    /**
     * batch request
     *
     * @param queryRequests request
     * @return fail requests
     */
    List<QueryRequest> postBatch(List<QueryRequest> queryRequests);

    /**
     * Check if this index exists.
     *
     * @param index index
     * @param type  type
     * @return response
     */
    Boolean isExistIndex(String index, String type);

    /**
     * batch request
     *
     * @param bulkRequests request
     * @return request status
     * @throws IOException request exception
     */
    String bulk(List<BulkRequest> bulkRequests) throws IOException;
}
