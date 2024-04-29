package tech.finovy.framework.distributed.lock.api;


import tech.finovy.framework.redis.entity.lock.entity.DistributedLock;

public interface DistributedLockService {
    /** 加锁
     * @param lock
     * @return DistributedLock
     */
    DistributedLock lock(DistributedLock lock);

    /** 释放锁
     * @param lock
     * @return DistributedLock
     */
    DistributedLock unlock(DistributedLock lock);

    /** 设置锁为完成状态
     * @param lock
     * @return DistributedLock
     */
    DistributedLock finished(DistributedLock lock);
}
