package tech.finovy.framework.oss.client.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import tech.finovy.framework.common.SecurityEncryption;
import tech.finovy.framework.config.nacos.listener.AbstractNacosConfigDefinitionListener;
import tech.finovy.framework.oss.client.OssClientMap;

public class OssConfigurationListener extends AbstractNacosConfigDefinitionListener<OssClientConfig> {
    private static final Logger LOGGER = LoggerFactory.getLogger(OssConfigurationListener.class);

    private final OssClientProperties properties;
    private final SecurityProperties securityProperties;
    private final OssClientMap ossClientMap;

    public OssConfigurationListener(OssClientProperties properties, SecurityProperties securityProperties, OssClientMap ossClientMap) {
        super(OssClientConfig.class, properties.getNacos().getDataId(), properties.getNacos().getDataGroup());
        this.properties = properties;
        this.securityProperties = securityProperties;
        this.ossClientMap = ossClientMap;
    }

    @Override
    public String getDataId() {
        return properties.getNacos().getDataId();
    }

    @Override
    public String getDataGroup() {
        return properties.getNacos().getDataGroup();
    }

    @Override
    public void refresh(String dataId, String dataGroup, OssClientConfig config, int version) {
        init(config);
    }

    public void init(OssClientConfig config) {
        config.getConfig().forEach(c -> {
            if (StringUtils.isNotBlank(config.getAccessKeyId())) {
                c.setAccessKeyId(config.getAccessKeyId());
            }
            if (StringUtils.isNotBlank(config.getAccessKeySecret())) {
                c.setAccessKeySecret(config.getAccessKeySecret());
            }
            if (StringUtils.isNotBlank(config.getEndpoint())) {
                c.setEndpoint(config.getEndpoint());
            }
        });
        String dconfSecret = System.getenv("DCONF_SECRET");
        String dconfIv = System.getenv("DCONF_IV");
        config.getConfig().forEach(eachConfig -> {
            if (eachConfig.isEncrypt()) {
                try {
                    if (ObjectUtils.isEmpty(dconfSecret) || ObjectUtils.isEmpty(dconfIv)) {
                        eachConfig.setEndpoint(SecurityEncryption.decrypt(eachConfig.getEndpoint(), securityProperties.getDatasourceSecret(), securityProperties.getDatasourceIv()));
                        eachConfig.setAccessKeyId(SecurityEncryption.decrypt(eachConfig.getAccessKeyId(), securityProperties.getDatasourceSecret(), securityProperties.getDatasourceIv()));
                        eachConfig.setAccessKeySecret(SecurityEncryption.decrypt(eachConfig.getAccessKeySecret(), securityProperties.getDatasourceSecret(), securityProperties.getDatasourceIv()));
                        eachConfig.setBucketName(SecurityEncryption.decrypt(eachConfig.getBucketName(), securityProperties.getDatasourceSecret(), securityProperties.getDatasourceIv()));
                    } else {
                        eachConfig.setEndpoint(SecurityEncryption.decrypt(eachConfig.getEndpoint(), dconfSecret, dconfIv));
                        eachConfig.setAccessKeyId(SecurityEncryption.decrypt(eachConfig.getAccessKeyId(), dconfSecret, dconfIv));
                        eachConfig.setAccessKeySecret(SecurityEncryption.decrypt(eachConfig.getAccessKeySecret(), dconfSecret, dconfIv));
                        eachConfig.setBucketName(SecurityEncryption.decrypt(eachConfig.getBucketName(), dconfSecret, dconfIv));
                    }
                } catch (Exception e) {
                    LOGGER.warn("Exception : {}", e.getMessage(), e);
                }
            }
        });
        ossClientMap.refreshClientMap(config, properties.getPublicX509Pem(), properties.getPrivatePkcs8Pem());
    }
}
