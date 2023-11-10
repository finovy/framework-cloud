package tech.finovy.framework.distributed.id.api;

public interface DistributedIdService {
    /**
     * 通过key获取自增id
     * @param key
     * @return 自增ID
     */
    long incrementAndGet(String key);
    void set(String key,long id);
}
