package tech.finovy.framework.mongodb.client.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Data
@RefreshScope
@ConfigurationProperties(prefix = MongoDbClientProperties.PREFIX)
public class MongoDbClientProperties {
    public static final String PREFIX = "mongdb.nacos";

    private long dataTimeout = 10000L;
    private String dataId = "framework-core-mongdb.json";
    private String dataGroup = "DEFAULT_GROUP";
}
