package tech.finovy.framework.config.nacos.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TestBeanListener2 extends AbstractNacosConfigDefinitionListener<TestItem> {
    @Value("${spring.cloud.nacos.config.namespace}")
    private String namespace;
    @Value("${spring.cloud.nacos.config.group-id}")
    private String dataGroup;

    protected TestBeanListener2() {
        super(TestItem.class);
    }

    @Override
    public String getDataId() {
        return "test2.yaml";
    }

    @Override
    public String getDataGroup() {
        return dataGroup;
    }

    @Override
    public String getNameSpace() {
        return namespace;
    }

    @Override
    public void refresh(String dataId, String dataGroup, TestItem config, int version) {
        log.info("{},{}-------============================-------{},{}", dataId, dataGroup, config.getA(), config.getB());
    }
}
