package tech.finovy.framework.distributed.lock;


import tech.finovy.framework.distributed.lock.api.DistributedLockService;
import tech.finovy.framework.distributed.lock.mock.DistributedLockServiceMockImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import tech.finovy.framework.redis.entity.lock.entity.DistributedLock;

@Slf4j
@RunWith(MockitoJUnitRunner.Silent.class)
class TestDistributedLockServiceMock {

private DistributedLockService distributedLockService;
    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        distributedLockService=new DistributedLockServiceMockImpl();

    }
    @Test
    @DisplayName("TestDistributedLockServiceMock")
    void distributedLockServiceMockTest(){
        DistributedLock lock= distributedLockService.lock(DistributedLock.builder().build());
        Assertions.assertFalse(lock.isLocked());
        lock= distributedLockService.unlock(DistributedLock.builder().build());
        Assertions.assertFalse(lock.isLocked());
        lock= distributedLockService.finished(DistributedLock.builder().build());
        Assertions.assertFalse(lock.isLocked());
    }
}
