package tech.finovy.framework.distributed.lock.mock;

import tech.finovy.framework.distributed.lock.Constant;
import tech.finovy.framework.distributed.lock.api.DistributedLockService;
import tech.finovy.framework.redis.entity.lock.entity.DistributedLock;

public class DistributedLockServiceMockImpl implements DistributedLockService {

    @Override
    public DistributedLock lock(DistributedLock lock) {
        return DistributedLock.builder().mock(true).locked(false).errMsg(Constant.NOPROVIDER).build();
    }

    @Override
    public DistributedLock unlock(DistributedLock lock) {
        return DistributedLock.builder().mock(true).locked(false).errMsg(Constant.NOPROVIDER).build();
    }

    @Override
    public DistributedLock finished(DistributedLock lock) {
        return DistributedLock.builder().mock(true).locked(false).errMsg(Constant.NOPROVIDER).build();
    }
}
