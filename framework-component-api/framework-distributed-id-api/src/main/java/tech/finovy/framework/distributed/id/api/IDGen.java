package tech.finovy.framework.distributed.id.api;


import tech.finovy.framework.redis.entity.id.entity.DistributedIdResult;

public interface IDGen {
    DistributedIdResult get(String key);
    boolean init();
}
