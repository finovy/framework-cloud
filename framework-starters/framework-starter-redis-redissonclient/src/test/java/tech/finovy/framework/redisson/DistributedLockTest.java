package tech.finovy.framework.redisson;

import com.alibaba.cloud.nacos.NacosConfigAutoConfiguration;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tech.finovy.framework.config.nacos.ShardingEngineNacosConfigAutoConfiguration;
import tech.finovy.framework.disruptor.core.DisruptorEventConfiguration;
import tech.finovy.framework.distributed.lock.api.DistributedLockService;
import tech.finovy.framework.redis.entity.lock.entity.DistributedLock;
import tech.finovy.framework.redisson.autoconfigure.RedissonClientAutoConfiguration;
import tech.finovy.framework.redisson.holder.RedisContextHolder;

@Slf4j
@ContextConfiguration
@ExtendWith(SpringExtension.class)
@ImportAutoConfiguration({RedissonClientAutoConfiguration.class, ShardingEngineNacosConfigAutoConfiguration.class, NacosConfigAutoConfiguration.class, DisruptorEventConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,classes = DistributedLockTest.class)
public class DistributedLockTest {

    @Autowired
    private DistributedLockService distributedLockService;

    @Test
    @DisplayName("TestDistributedLockService")
    void distributedLockServiceTest() {
        final RedissonClient client = RedisContextHolder.get().getClient();
        Assertions.assertNotNull(client);
        DistributedLock lock=new DistributedLock();
        long time=System.currentTimeMillis();
        String key="TestLock"+time;
        lock.setKey(key);
        lock.setLeaseTime(0);
        DistributedLock lockStatus= distributedLockService.lock(lock);
        if(lockStatus.isExists()&&!lockStatus.isLocked()){
            lockStatus.setForce(true);
            distributedLockService.unlock(lockStatus);
        }
        lock= distributedLockService.lock(lock);
        Assertions.assertNull(lock.getErrMsg());
        Assertions.assertTrue(lock.isLocked());
        log.info("lockid:{}",lock.getId());
        //再次获取锁
        DistributedLock lock2=new DistributedLock();
        lock2.setKey(key);
        lock2.setLeaseTime(0);
        lock2= distributedLockService.lock(lock2);
        log.info("lock2-0 id:{}",lock2.getId());
        Assertions.assertNull(lock2.getErrMsg());
        Assertions.assertFalse(lock2.isLocked());

        lock2.setKey(key+"00");
        lock2= distributedLockService.unlock(lock2);
        log.info("lock2-1 id:{}",lock2.getId());
        Assertions.assertNull(lock2.getErrMsg());
        Assertions.assertFalse(lock2.isLocked());

        lock=distributedLockService.unlock(lock);
        Assertions.assertNull(lock.getErrMsg());

        DistributedLock lockLeaseTime=new DistributedLock();
        lockLeaseTime.setKey("LockLeaseTime");
        lockLeaseTime.setLeaseTime(0);

        DistributedLock lockLeaseTimeStatus= distributedLockService.lock(lockLeaseTime);
        if(lockLeaseTimeStatus.isExists()&&!lockLeaseTimeStatus.isLocked()){
            lockLeaseTimeStatus.setForce(true);
            distributedLockService.unlock(lockLeaseTimeStatus);
        }
        lockLeaseTime= distributedLockService.lock(lockLeaseTime);
        Assertions.assertNull(lockLeaseTime.getErrMsg());
        Assertions.assertTrue(lockLeaseTime.isLocked());
        log.info("lockid:{}",lockLeaseTime.getId());
    }

    @Test
    @DisplayName("TestFinished")
    void finishedTest() {
        DistributedLock lockLeaseTime=new DistributedLock();
        lockLeaseTime.setKey("TestFinished");
        lockLeaseTime.setLeaseTime(0);
        DistributedLock lockLeaseTimeStatus= distributedLockService.lock(lockLeaseTime);
        if(lockLeaseTimeStatus.isExists()&&!lockLeaseTimeStatus.isLocked()){
            lockLeaseTimeStatus.setForce(true);
            distributedLockService.unlock(lockLeaseTimeStatus);
        }

        lockLeaseTime= distributedLockService.lock(lockLeaseTime);
        Assertions.assertNull(lockLeaseTime.getErrMsg());
        Assertions.assertTrue(lockLeaseTime.isLocked());
        log.info("lock :{}",JSON.toJSONString(lockLeaseTime));

        lockLeaseTime= distributedLockService.finished(lockLeaseTime);
        Assertions.assertNull(lockLeaseTime.getErrMsg());
        Assertions.assertFalse(lockLeaseTime.isLocked());
        log.info("finished:{}",JSON.toJSONString(lockLeaseTime));

        lockLeaseTime=new DistributedLock();
        lockLeaseTime.setKey("TestFinished");
        lockLeaseTime.setLeaseTime(0);

        lockLeaseTime= distributedLockService.lock(lockLeaseTime);
        Assertions.assertNull(lockLeaseTime.getErrMsg());
        Assertions.assertFalse(lockLeaseTime.isLocked());
        log.info("try lock :{}",JSON.toJSONString(lockLeaseTime));

        lockLeaseTime=distributedLockService.unlock(lockLeaseTime);
        Assertions.assertNull(lockLeaseTime.getErrMsg());
    }

}
