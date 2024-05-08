package tech.finovy.framework.mongodb.client;

import com.alibaba.fastjson2.JSON;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import tech.finovy.framework.config.nacos.listener.AbstractNacosConfigDefinitionListener;
import tech.finovy.framework.mongodb.client.autoconfigure.MongoDbClientProperties;
import tech.finovy.framework.mongodb.client.entity.MongoDbClientConfig;
import tech.finovy.framework.mongodb.client.entity.MongodbDatasourceConfigList;

import java.util.List;

public class MongoDbClientConfigListener extends AbstractNacosConfigDefinitionListener<MongodbDatasourceConfigList> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDbClientConfigListener.class);

    private final MongoDbClientProperties properties;
    private final ApplicationContext context;
    private final MongodbClientSourceMap clientSourceMap = new MongodbClientSourceMap();

    public MongoDbClientConfigListener(MongoDbClientProperties properties, ApplicationContext context) {
        super(MongodbDatasourceConfigList.class, properties.getDataId(), properties.getDataGroup());
        this.properties = properties;
        this.context = context;
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
    public void refresh(String dataId, String dataGroup, MongodbDatasourceConfigList config, int version) {
        init(config);
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) context;
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getAutowireCapableBeanFactory();
        if (!defaultListableBeanFactory.getBeansOfType(MongodbClientSourceMap.class).isEmpty()) {
            // refresh
            defaultListableBeanFactory.destroySingleton(defaultListableBeanFactory.getBeanNamesForType(MongodbClientSourceMap.class)[0]);
            defaultListableBeanFactory.registerSingleton("MongoDbClientSourceMap", clientSourceMap);
            return;
        }
        // register
        defaultListableBeanFactory.registerSingleton("MongoDbClientSourceMap", clientSourceMap);
    }

    private void init(MongodbDatasourceConfigList config) {
        List<MongoDbClientConfig> configList = config.getConfig();
        if (ObjectUtils.isEmpty(configList)) {
            return;
        }
        LOGGER.info("init Mongodb ClientSourceMap({}) ", JSON.toJSONString(configList));
        for (MongoDbClientConfig eachConfig : configList) {
            if (clientSourceMap.checkDatasourceExists(eachConfig)) {
                continue;
            }
            if (clientSourceMap.destroy(eachConfig.getDatabase())) {
                clientSourceMap.refreshMongodbSource(eachConfig);
            }
        }
    }
}
