package tech.finovy.framework.elasticsearch.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateRequest implements Serializable {
    private static final long serialVersionUID = -1113168149822035182L;
    private final String index;
    private final String docType;
    private final String id;
    private String jsonEntity;
}
