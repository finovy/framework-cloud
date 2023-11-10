package tech.finovy.framework.elasticsearch.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class QueryResult implements Serializable {
    private static final long serialVersionUID = 5929110477169331607L;
    private int took;
    private boolean timedout=false;
}
