package tech.finovy.framework.elasticsearch.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ESBulkRequest implements Serializable {
    private static final long serialVersionUID = -1113168142422035182L;

    private final String index;

    private final String docType = "_doc";

    private final String id;

    private String jsonEntity;
}
