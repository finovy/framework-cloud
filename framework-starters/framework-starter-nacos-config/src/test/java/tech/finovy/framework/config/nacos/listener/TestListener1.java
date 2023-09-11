package tech.finovy.framework.config.nacos.listener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestListener1 extends AbstractNacosConfigDefinitionListener<TestItem> {
    protected TestListener1(String dataId, String dataGroup, String namespace) {
        super(TestItem.class, dataId, dataGroup, namespace);
    }

    @Override
    public String getDataId() {
        return dataId;
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
        log.info("{},{}--------------{},{}", dataId, dataGroup, config.getA(), config.getB());
    }
}
