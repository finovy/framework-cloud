package tech.finovy.framework.datasource.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class DynamicResultSet implements Serializable {
    private static final long serialVersionUID = -4222391733665138004L;
    /**
     * 结果集合
     */
    private List<Map<String, Object>> result;

    private long updateCount = 0;

    private String errMsg;
}
