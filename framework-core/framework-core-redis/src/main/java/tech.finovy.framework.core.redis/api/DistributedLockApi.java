package tech.finovy.framework.core.redis.api;


import tech.finovy.framework.core.redis.entity.lock.entity.DistributedLock;

/**
 * @author Dtype.huang
 */
public interface DistributedLockApi {
    /**
     * 加锁
     *
     * @param lock
     * @return DistributedLock
     */
    DistributedLock lock(DistributedLock lock);

    /**
     * 释放锁
     *
     * @param lock
     * @return DistributedLock
     */
    DistributedLock unlock(DistributedLock lock);

    /**
     * 设置锁为完成状态
     *
     * @param lock
     * @return DistributedLock
     */
    DistributedLock finished(DistributedLock lock);
}
