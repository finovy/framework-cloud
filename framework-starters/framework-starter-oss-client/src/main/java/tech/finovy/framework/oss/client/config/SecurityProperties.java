package tech.finovy.framework.oss.client.config;

import lombok.Data;

@Data
public class SecurityProperties {

    public static final String PREFIX = "dynamic-datasource";

    private String datasourceSecret;

    private String datasourceIv;
}
