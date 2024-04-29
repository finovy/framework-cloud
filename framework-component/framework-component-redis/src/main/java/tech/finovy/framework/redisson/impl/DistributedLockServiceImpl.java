package tech.finovy.framework.redisson.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.finovy.framework.distributed.lock.api.DistributedLockService;
import tech.finovy.framework.redis.entity.lock.entity.DistributedLock;
import tech.finovy.framework.redisson.api.DistributedLockApi;

public class DistributedLockServiceImpl implements DistributedLockService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributedLockServiceImpl.class);

    private final DistributedLockApi distributedLockApi;

    public DistributedLockServiceImpl(DistributedLockApi distributedLockApi) {
        this.distributedLockApi = distributedLockApi;
    }

    @Override
    public DistributedLock lock(DistributedLock lock) {
        return distributedLockApi.lock(lock);
    }

    @Override
    public DistributedLock unlock(DistributedLock lock) {
        try {
            lock = distributedLockApi.unlock(lock);
        } catch (Exception e) {
            LOGGER.error("DistributedLock unlock,key={},id={} ERROR:{}", lock.getKey(), lock.getId(), e.toString());
            lock.setErrMsg(e.toString());
        }
        return lock;
    }

    @Override
    public DistributedLock finished(DistributedLock lock) {
        return distributedLockApi.finished(lock);
    }
}
