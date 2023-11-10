package tech.finovy.framework.redisson.api;

@Deprecated(since = "1.0.0", forRemoval = true)
public interface DistributedIdApi {

    /**
     * get increment-id by key
     *
     * @param key a unique key for id
     * @return increment-id
     */
    long incrementAndGet(String key);

    /**
     * set current id for business key
     *
     * @param key a unique key for id
     * @param id  current id
     */
    void set(String key, long id);
}
