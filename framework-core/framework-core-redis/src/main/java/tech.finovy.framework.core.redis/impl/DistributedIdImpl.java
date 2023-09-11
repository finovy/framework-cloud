package tech.finovy.framework.core.redis.impl;

import tech.finovy.framework.core.redis.ShardingEngineRedisConext;
import tech.finovy.framework.core.redis.api.DistributedIdApi;
import tech.finovy.framework.core.redis.holder.ShardingEngineRedisConextHolder;
import org.redisson.api.RAtomicLong;
import org.springframework.stereotype.Service;

/**
 * @author Dtype.huang
 */
@Service
public class DistributedIdImpl implements DistributedIdApi {
    private final ShardingEngineRedisConext conext = ShardingEngineRedisConextHolder.get();

    @Override
    public long incrementAndGet(String key) {
        RAtomicLong atomicLong = conext.getClient().getAtomicLong(String.join(":", "ID", key));
        return atomicLong.getAndIncrement();
    }

    @Override
    public void set(String key, long id) {
        RAtomicLong atomicLong = conext.getClient().getAtomicLong(String.join(":", "ID", key));
        atomicLong.set(id);
    }
}
