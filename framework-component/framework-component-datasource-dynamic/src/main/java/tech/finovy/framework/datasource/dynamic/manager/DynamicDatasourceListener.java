package tech.finovy.framework.datasource.dynamic.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import tech.finovy.framework.common.SecurityEncryption;
import tech.finovy.framework.config.nacos.listener.AbstractNacosConfigDefinitionListener;
import tech.finovy.framework.datasource.dynamic.entity.DynamicDatasourceConfig;
import tech.finovy.framework.datasource.dynamic.entity.DynamicDatasourceConfigList;
import tech.finovy.framework.datasource.dynamic.pools.DynamicDataSourceMap;

import java.sql.SQLException;
import java.util.List;

public class DynamicDatasourceListener extends AbstractNacosConfigDefinitionListener<DynamicDatasourceConfigList> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDatasourceListener.class);

    private final DynamicDataSourceMap dynamicDataSourceMap = new DynamicDataSourceMap();

    private final DynamicDatasourceProperties properties;

    public DynamicDatasourceListener(DynamicDatasourceProperties properties) {
        super(DynamicDatasourceConfigList.class);
        this.properties = properties;
    }

    @Override
    public String getDataId() {
        return properties.getDataId();
    }

    @Override
    public String getDataGroup() {
        return properties.getDataGroup();
    }

    @Override
    public void refresh(String dataId, String dataGroup, DynamicDatasourceConfigList config, int version) {
        List<DynamicDatasourceConfig> configList = config.getConfig();
        if (ObjectUtils.isEmpty(configList)) {
            LOGGER.warn("DynamicDatasourceConfig[{}] is Empty", getDataId());
            return;
        }
        LOGGER.info("DynamicDatasourceConfig[{}] size:{}", getDataId(), configList.size());
        for (DynamicDatasourceConfig eachConfig : configList) {
            try {
                decrypt(eachConfig);
                if (dynamicDataSourceMap.checkDatasourceExists(eachConfig)) {
                    LOGGER.warn("DynamicDatasourceConfig[{}] Datasource exists:{}", getDataId(), eachConfig.getKey());
                    continue;
                }
                if (destroyDatasource(eachConfig.getKey())) {
                    dynamicDataSourceMap.refreshDatasource(eachConfig);
                }
            } catch (Exception e) {
                LOGGER.error(e.toString());
            }
        }
    }

    private void decrypt(DynamicDatasourceConfig eachConfig) {
        String dconfSecret = System.getenv("DCONF_SECRET");
        String dconfIv = System.getenv("DCONF_IV");
        if (ObjectUtils.isEmpty(dconfSecret) || ObjectUtils.isEmpty(dconfIv)) {
            dconfSecret = properties.getSecret();
            dconfIv = properties.getIv();
        }
        if (ObjectUtils.isEmpty(dconfSecret) || ObjectUtils.isEmpty(dconfIv)) {
            LOGGER.info("Decrypt with Nacos database config:{}", eachConfig.getKey());
            return;
        }
        try {
            LOGGER.info("Decrypt with SECRET Config database config:{}", eachConfig.getKey());
            eachConfig.setUrl(SecurityEncryption.decrypt(eachConfig.getUrl(), dconfSecret, dconfIv));
            eachConfig.setUsername(SecurityEncryption.decrypt(eachConfig.getUsername(), dconfSecret, dconfIv));
            eachConfig.setPassword(SecurityEncryption.decrypt(eachConfig.getPassword(), dconfSecret, dconfIv));
        } catch (Exception e) {
            LOGGER.error(e.toString());
        }
    }

    public boolean createDatasource(String key) {
        return false;
    }

    public boolean destroyDatasource(String key) {
        return dynamicDataSourceMap.destroy(key);
    }
}
