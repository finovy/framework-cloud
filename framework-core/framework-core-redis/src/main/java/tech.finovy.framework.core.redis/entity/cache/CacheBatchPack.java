package tech.finovy.framework.core.redis.entity.cache;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Dtype.huang
 */

@Getter
@Setter
public class CacheBatchPack<T> extends BaseCache implements Serializable {
    private static final long serialVersionUID = 5930737870713572950L;
    private Map<String, T> data;

    public CacheBatchPack(Class<T> dataType, Map<String, T> data, boolean skipPrefix) {
        super("", skipPrefix);
        this.data = data;
        this.setCacheType(dataType.getTypeName());
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
