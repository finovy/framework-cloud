package tech.finovy.framework.distributed.lock.stub;

import tech.finovy.framework.distributed.lock.api.DistributedLockService;
import tech.finovy.framework.redis.entity.lock.entity.DistributedLock;

public class DistributedLockServiceStubImpl implements DistributedLockService {

    private DistributedLockService distributedLockService;
    public DistributedLockServiceStubImpl(DistributedLockService distributedLockService){
        this.distributedLockService=distributedLockService;
    }
    @Override
    public DistributedLock lock(DistributedLock lock) {
        return distributedLockService.lock(lock);
    }

    @Override
    public DistributedLock unlock(DistributedLock lock) {
        return distributedLockService.unlock(lock);
    }

    @Override
    public DistributedLock finished(DistributedLock lock) {
        return distributedLockService.finished(lock);
    }
}
