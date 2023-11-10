package tech.finovy.framework.distributed.lock;


import tech.finovy.framework.distributed.lock.api.DistributedLockService;
import tech.finovy.framework.distributed.lock.stub.DistributedLockServiceStubImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import tech.finovy.framework.redis.entity.lock.entity.DistributedLock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@Slf4j
@RunWith(MockitoJUnitRunner.Silent.class)
class TestDistributedLockServiceStub {
    @Mock
    private DistributedLockService distributedLockService;
    private DistributedLockServiceStubImpl distributedLockServiceStub;
    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        DistributedLock distributedLock=DistributedLock.builder().build();
        distributedLockServiceStub =new DistributedLockServiceStubImpl(distributedLockService);
        doReturn(distributedLock).when(distributedLockService).lock(any(DistributedLock.class));
        doReturn(distributedLock).when(distributedLockService).unlock(any(DistributedLock.class));
        doReturn(distributedLock).when(distributedLockService).finished(any(DistributedLock.class));
    }
    @Test
    @DisplayName("Testunlock")
    void unlockTest(){
        DistributedLock distributedLock=DistributedLock.builder().build();
        distributedLock= distributedLockServiceStub.unlock(distributedLock);
        Assertions.assertFalse(distributedLock.isLocked());
    }
    @Test
    @DisplayName("TestFinish")
    void finishTest(){
        DistributedLock distributedLock=DistributedLock.builder().build();
        distributedLock= distributedLockServiceStub.finished(distributedLock);
        Assertions.assertFalse(distributedLock.isLocked());
    }
}
