package tech.finovy.framework.core.redis.api;

/**
 * @author Dtype.huang
 */
public interface DistributedIdApi {
    /**
     * 通过key获取自增id
     *
     * @param key
     * @return 自增ID
     */
    long incrementAndGet(String key);

    void set(String key, long id);
}
