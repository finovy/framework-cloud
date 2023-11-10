package tech.finovy.framework.distributed.id.api;


import tech.finovy.framework.redis.entity.id.entity.DistributedIdResult;

public interface SnowflakeService{

    DistributedIdResult getId(final String key, final int scale);
    DistributedIdResult getId(final String key);
}
