package tech.finovy.framework.config.nacos.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class TestBeanListener1 extends AbstractNacosConfigDefinitionListener<TestItem> {
    @Value("${spring.cloud.nacos.config.namespace}")
    private String namespace;
    @Value("${spring.cloud.nacos.config.group-id}")
    private String dataGroup;

    @PostConstruct
    public void init() {
        System.out.println("TestBeanListener1 init");
    }
    protected TestBeanListener1() {
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
        log.info("{},{}-------==========================-------{},{}", dataId, dataGroup, config.getA(), config.getB());
    }
}
