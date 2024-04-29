package tech.finovy.framework.redis.entity.cache.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;


@Getter
@Setter
public class CacheBatchPack<T> extends BaseCache implements Serializable {
	private static final long serialVersionUID = 5930737870713572950L;
	private Map<String,T> data;
	public CacheBatchPack(Class<T> dataType,Map<String,T> data, boolean skipPrefix) {
		super("", skipPrefix);
		this.data = data;
		if (dataType != null) {
			this.setCacheType(dataType.getTypeName());
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
