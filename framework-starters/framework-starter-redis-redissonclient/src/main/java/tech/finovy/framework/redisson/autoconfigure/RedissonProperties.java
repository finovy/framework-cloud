package tech.finovy.framework.redisson.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties(prefix = RedissonProperties.PREFIX)
public class RedissonProperties {

    public static final String PREFIX = "redis";

    private String keyVersion = "0.1";
    private String keyPrefix = "core";
    private long keyDefaultTtl = 86400000;
    private int keyHashModSize = 30;
    private String nacosDataId = "framework-core-redis";
    private String nacosDataGroup = "DEFAULT_GROUP";
    private long nacosDataTimeoutMs = 10000;
    private boolean encrypt;
    private boolean debug;
    private long connectDelay = 10000;

}
