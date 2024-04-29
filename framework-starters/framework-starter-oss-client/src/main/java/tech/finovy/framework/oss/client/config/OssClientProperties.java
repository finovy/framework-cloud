package tech.finovy.framework.oss.client.config;

import lombok.Data;

@Data
public class OssClientProperties {

    public static final String PREFIX = "oss";

    private Nacos nacos = new Nacos();

    private boolean usePkcs8;

    private String publicX509Pem;

    private String privatePkcs8Pem;


    @Data
    public static class Nacos{
        private long dataTimeout = 10000;
        private String dataId = "framework-storage.json";
        private String dataGroup = "DEFAULT_GROUP";
    }

}
