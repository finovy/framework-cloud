package tech.finovy.framework.transaction.tcc.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import tech.finovy.framework.transaction.tcc.client.constant.Constants;

@Data
@Configuration
@ConfigurationProperties(prefix = Constants.TCC_CLIENT_PREFIX)
public class TccClientProperties {
    private String group = "DEFAULT";

    private String secretKey;

    private String clusterDataId = "tcc-server-clusters.json";
    private String clusterGroup = "DEFAULT_GROUP";

    private int healthCheckInitialDelay = 10;
    private int healthCheckPeriod = 5;
}
