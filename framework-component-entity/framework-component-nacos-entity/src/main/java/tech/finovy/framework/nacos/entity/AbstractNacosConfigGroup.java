package tech.finovy.framework.nacos.entity;

import java.util.List;

public abstract class AbstractNacosConfigGroup<E extends AbstractNacosConfigEntity> extends NacosConfigDefaultItem {
    public abstract List<E> getEntity();

}
