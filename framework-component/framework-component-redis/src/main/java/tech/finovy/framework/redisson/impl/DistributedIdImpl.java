package tech.finovy.framework.redisson.impl;

import org.redisson.api.RAtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.finovy.framework.redisson.api.DistributedIdApi;
import tech.finovy.framework.redisson.client.RedissonClientInterface;
import tech.finovy.framework.redisson.holder.RedisContext;
import tech.finovy.framework.redisson.holder.RedisContextHolder;

public class DistributedIdImpl implements DistributedIdApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributedIdImpl.class);

    private final RedisContext context = RedisContextHolder.get();



    @Override
    public long incrementAndGet(String key) {
        final RedissonClientInterface client = context.getClient();
        RAtomicLong atomicLong = client.getAtomicLong(client.createKey(key, "ID"));
        return atomicLong.getAndIncrement();
    }

    @Override
    public void set(String key, long id) {
        final RedissonClientInterface client = context.getClient();
        RAtomicLong atomicLong = client.getAtomicLong(client.createKey(key, "ID"));
        atomicLong.set(id);
    }
}
