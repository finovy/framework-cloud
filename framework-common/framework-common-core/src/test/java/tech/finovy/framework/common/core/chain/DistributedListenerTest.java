package tech.finovy.framework.common.core.chain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DistributedListenerTest {
    Map<String, Map<String, TestListenerInterface>> listeners;
    Map<String, TestListenerInterface> initListeners = new HashMap<>();

    @BeforeEach
    public void init() {
        final Test1ExecutorListener test1ExecutorListener = new Test1ExecutorListener();
        final Test2ExecutorListener test2ExecutorListener = new Test2ExecutorListener();
        final Test3ExecutorListener test3ExecutorListener = new Test3ExecutorListener();
        initListeners.put("test1ExecutorListener", test1ExecutorListener);
        initListeners.put("test2ExecutorListener", test2ExecutorListener);
        initListeners.put("test3ExecutorListener", test3ExecutorListener);
        listeners = ChainSortUtil.multiChainListenerSort(initListeners);
    }

    @Test
    public void testDisruptorListener() {
        boolean success = false;
        for (Map.Entry<String, Map<String, TestListenerInterface>> each : listeners.entrySet()) {
            for (Map.Entry<String, TestListenerInterface> entry : each.getValue().entrySet()) {
                log.info("Init {},{},{}", each.getKey(), entry.getKey(), entry.getValue().isEnable());
                success = true;
            }
        }
        Assertions.assertTrue(success);
        final Map<String, TestListenerInterface> singleDistributedListeners = ChainSortUtil.singleChainListenerSort(initListeners);
        Assertions.assertEquals(3, singleDistributedListeners.size());

    }

}
