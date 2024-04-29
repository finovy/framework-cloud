package tech.finovy.framework.datasource.dynamic.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class DynamicDatasourceConfigList implements Serializable {
    @Serial
    private static final long serialVersionUID = -2942600603649542259L;
    private boolean encrypt = false;
    private List<DynamicDatasourceConfig> config;
}
