package tech.finovy.framework.core.redis.entity.cache;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * @author: Dtype.Huang
 * @date: 2020/4/26 17:11
 */

@Getter
@Setter
@SuperBuilder
public class BaseCache extends CacheKey {
    private static final long serialVersionUID = -8459143510288453039L;
    private String errMsg;
    private boolean exists = false;
    private boolean mock = false;

    public BaseCache(String key, boolean skipPrefix) {
        super(key, skipPrefix);
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
