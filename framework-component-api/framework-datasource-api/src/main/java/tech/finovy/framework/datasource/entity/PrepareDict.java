package tech.finovy.framework.datasource.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author PC
 */
@Data
public class PrepareDict implements Serializable {
    private static final long serialVersionUID = 6181406230795341735L;
    private String columnName;
    private int columnIndex;
    private String columnType;
    private boolean mustInput;
    private Object defaultValue;


    public PrepareDict() {
    }
    public PrepareDict(String columnName, int columnIndex, String columnType) {
        this.columnName = columnName;
        this.columnIndex = columnIndex;
        this.columnType = columnType;
        this.mustInput = false;
        this.defaultValue = null;
    }

    public PrepareDict(String columnName, int columnIndex, String columnType, boolean mustInput, Object defaultValue) {
        this.columnName = columnName;
        this.columnIndex = columnIndex;
        this.columnType = columnType;
        this.mustInput = mustInput;
        this.defaultValue = defaultValue;
    }

}
