package tech.finovy.framework.redis.entity.cache.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
public class SerialCache extends BaseCache {
    private static final long serialVersionUID = 8482475883121017246L;
    private String cacheData;
    public SerialCache(String key) {
        super(key, false);
        super.setCacheType(String.class.getTypeName());
    }
    public SerialCache(String key, boolean skipPrefix) {
        super(key, skipPrefix);
        super.setCacheType(String.class.getTypeName());
    }
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
