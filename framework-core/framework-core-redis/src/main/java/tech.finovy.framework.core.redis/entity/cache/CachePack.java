package tech.finovy.framework.core.redis.entity.cache;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Dtype.huang
 */

@Getter
@Setter
public class CachePack<T> extends BaseCache implements Serializable {

    private static final long serialVersionUID = -4042573842408015192L;
    private T data;

    public CachePack(String key) {
        super(key, false);
    }

    public CachePack(String key, boolean skipPrefix) {
        super(key, skipPrefix);
    }

    public void setData(T data) {
        this.data = data;
        if (data != null) {
            this.setCacheType(data.getClass().getTypeName());
        }
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
