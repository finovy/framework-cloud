package tech.finovy.framework.distributed.id.api;


import tech.finovy.framework.redis.entity.id.entity.DistributedIdResult;

public interface SegmentService {

    DistributedIdResult getId(final String key, final int scale);
    DistributedIdResult getId(final String key);
}
