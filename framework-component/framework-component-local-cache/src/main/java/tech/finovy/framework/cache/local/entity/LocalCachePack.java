package tech.finovy.framework.cache.local.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class LocalCachePack<T> extends LocalCacheKey implements Serializable {

    @Serial
    private static final long serialVersionUID = -4042573842408015192L;
    private String errMsg;
    private T data;

    public LocalCachePack(String key) {
        super(key);
    }

    public <U> LocalCachePack(Class<U> cacheType, String key) {
        super(cacheType, key);
    }

    public void setData(T data) {
        this.data = data;
        if (data != null) {
            this.setCacheType(data.getClass().getTypeName());
        }
    }

    public boolean isExists() {
        return data != null;
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
