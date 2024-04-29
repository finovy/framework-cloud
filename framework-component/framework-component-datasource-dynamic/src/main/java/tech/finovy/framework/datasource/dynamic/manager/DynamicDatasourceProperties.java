package tech.finovy.framework.datasource.dynamic.manager;

import lombok.Data;

@Data
public class DynamicDatasourceProperties {

    public static final String PREFIX = "dynamic-datasource";
    private String dataId = "framework-dynamic-datasource";
    private String dataGroup = "DEFAULT_GROUP";
    private String secret;
    private String iv;

}
