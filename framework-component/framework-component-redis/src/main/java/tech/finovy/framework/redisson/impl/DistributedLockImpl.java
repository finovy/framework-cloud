package tech.finovy.framework.redisson.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.finovy.framework.redis.entity.lock.entity.DistributedLock;
import tech.finovy.framework.redis.entity.lock.exception.DistributedLockException;
import tech.finovy.framework.redisson.api.DistributedLockApi;
import tech.finovy.framework.redisson.holder.RedisContext;
import tech.finovy.framework.redisson.holder.RedisContextHolder;

import java.util.concurrent.TimeUnit;

public class DistributedLockImpl implements DistributedLockApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributedLockImpl.class);

    private final RedisContext context = RedisContextHolder.get();


    @Override
    public DistributedLock lock(DistributedLock lock) {
        final RedissonClient client = context.getClient();
        DistributedLock vLock = verifyLock(lock);
        if (vLock.getErrMsg() != null) {
            return vLock;
        }
        String key = context.createKey(lock.getKey(), DistributedLock.class.getSimpleName(), false);
        RBucket<DistributedLock> lockCache = client.getBucket(key, JsonJacksonCodec.INSTANCE);
        boolean lockStatus;
        if (lock.getId() <= 0L) {
            lock.setId(Long.parseLong(RandomStringUtils.randomNumeric(16)));
        }
        if (lock.getLeaseTime() > 0L) {
            lockStatus = lockCache.trySet(lock, lock.getLeaseTime(), TimeUnit.MILLISECONDS);
        } else {
            lockStatus = lockCache.trySet(lock);
        }
        LOGGER.debug("Lock:{},ID:{},LeaseTime:{},Locked:{}", key, lock.getId(), lock.getLeaseTime(), lockStatus);
        if (lockStatus) {
            lock.setLocked(true);
            lock.setExists(true);
            if (lock.getLeaseTime() > 0L) {
                lockCache.expireAsync(lock.getLeaseTime(), TimeUnit.MILLISECONDS);
                LOGGER.debug("Lock:{},ID:{},SetExpire:{}", key, lock.getId(), lock.getLeaseTime());
            }
            return lock;
        }
        DistributedLock existsLock = lockCache.get();
        //重入
        if (existsLock != null && existsLock.getId() == lock.getId()) {
            LOGGER.debug("Lock:{},ID:{} equals", key, lock.getId());
            if (lock.getLeaseTime() > 0L) {
                lockCache.set(lock, lock.getLeaseTime(), TimeUnit.MILLISECONDS);
                LOGGER.debug("Lock:{},update lock with expire :{}", key, lock.getLeaseTime());
            } else {
                lockCache.set(lock);
                LOGGER.debug("Lock:{},update lock", key);
            }
            if (lock.getLeaseTime() > 0L) {
                lockCache.expireAsync(lock.getLeaseTime(), TimeUnit.MILLISECONDS);
                LOGGER.debug("Lock:{},update expire:{}", key, lock.getLeaseTime());
            }
            lock.setLocked(true);
            lock.setExists(true);
            lock.setRemainTimeToLive(lockCache.remainTimeToLive());
            lock.setId(existsLock.getId());
            return lock;
        }
        if (existsLock == null) {
            existsLock = new DistributedLock();
        }
        existsLock.setRemainTimeToLive(lockCache.remainTimeToLive());
        existsLock.setKey(lock.getKey());
        existsLock.setId(0L);
        existsLock.setLocked(false);
        existsLock.setExists(true);
        return existsLock;
    }


    @Override
    public DistributedLock unlock(DistributedLock lock) {
        final RedissonClient client = context.getClient();
        DistributedLock vLock = verifyLock(lock);
        if (vLock.getErrMsg() != null) {
            return vLock;
        }
        String key = context.createKey(lock.getKey(), DistributedLock.class.getSimpleName(), false);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Unlock key:{}", key);
        }
        RBucket<DistributedLock> lockCache = client.getBucket(key, JsonJacksonCodec.INSTANCE);
        if (!lockCache.isExists()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Lock:{} not exists", key);
            }
            return lock;
        }
        DistributedLock locked = lockCache.get();
        if (locked == null) {
            lock.setLocked(false);
            lock.setExists(false);
            return lock;
        }
        if (locked.getId() == lock.getId() || lock.isForce()) {
            lockCache.delete();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Delete lock:{},force:{}", key, lock.isForce());
            }
            lock.setLocked(false);
            lock.setExists(true);
            return lock;
        }

        return lock;
    }

    @Override
    public DistributedLock finished(DistributedLock lock) {
        final RedissonClient client = context.getClient();
        if (StringUtils.isEmpty(lock.getKey())) {
            throw new DistributedLockException();
        }
        String key = context.createKey(lock.getKey(), DistributedLock.class.getSimpleName(), false);
        RBucket<DistributedLock> bucket = client.getBucket(key, JsonJacksonCodec.INSTANCE);
        if (bucket.isExists()) {
            DistributedLock locked = bucket.get();
            if (locked != null && (locked.getId() == lock.getId() || lock.isForce())) {
                lock.setFinished(true);
                long time = lock.getFinishedTimeToLive();
                if (lock.getLeaseTime() > 0 && time == 0) {
                    time = lock.getLeaseTime();
                }
                if (time > 0) {
                    bucket.set(lock, time, TimeUnit.MILLISECONDS);
                } else {
                    bucket.set(lock);
                }
                lock.setLocked(false);
                lock.setExists(true);
                return lock;
            }
        }
        return lock;
    }

    private DistributedLock verifyLock(DistributedLock lock) {
        if (lock == null) {
            DistributedLock errLock = new DistributedLock();
            errLock.setErrMsg("Lock IS NULL");
            return errLock;
        }
        if (StringUtils.isEmpty(lock.getKey())) {
            DistributedLock errLock = new DistributedLock();
            errLock.setErrMsg("LockKey IS NULL");
            return errLock;
        }
        return lock;
    }
}
