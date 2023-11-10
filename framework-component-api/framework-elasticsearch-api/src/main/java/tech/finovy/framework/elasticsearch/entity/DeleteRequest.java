package tech.finovy.framework.elasticsearch.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeleteRequest implements Serializable {
    private static final long serialVersionUID = -5858095651103325738L;
    private final String index;
    private final String docType;
    private final String id;
    public static final String DELETE="delete";
}
