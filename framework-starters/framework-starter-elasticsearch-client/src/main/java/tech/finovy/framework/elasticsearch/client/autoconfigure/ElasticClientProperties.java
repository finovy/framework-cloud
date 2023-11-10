package tech.finovy.framework.elasticsearch.client.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Data
@RefreshScope
@ConfigurationProperties(prefix = ElasticClientProperties.PREFIX)
public class ElasticClientProperties {
    public static final String PREFIX = "elasticsearch.nacos";
    private String dataId = "framework-core-elasticsearch.yaml";
    private String dataGroup = "DEFAULT_GROUP";
    private boolean enableSniffer = false;
}
