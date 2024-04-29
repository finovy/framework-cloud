package tech.finovy.framework.elasticsearch.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class QueryRequest implements Serializable {
    private static final long serialVersionUID = -2634921262175024974L;
    private final String method;
    private final String endpoint;
    private int from=0;
    private int size=0;
    private String jsonEntity;
    private List<String> fields=new ArrayList<>();
    public static final String FROM_INDEX="from";
    public static final String RESULT_SIZE="size";
    public static final String SOURCE="_source";

    public void setFields(String... field) {
        fields.clear();
        fields.addAll(Arrays.asList(field));
    }
}
