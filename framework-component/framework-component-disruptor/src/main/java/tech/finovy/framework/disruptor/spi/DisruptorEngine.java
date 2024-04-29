package tech.finovy.framework.disruptor.spi;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import tech.finovy.framework.disruptor.core.event.DisruptorEvent;

import java.util.List;

public interface DisruptorEngine {
    void post(DisruptorEvent event);

    void addProcessInterfaces(List<ProcessInterface> processInterfaces);

    void addProcessInterface(ProcessInterface processInterfaces);

    void start();

    void shutdown();

    default WaitStrategy switchWaitStrategy(int type) {
        return switch (type) {
            case 1 -> new SleepingWaitStrategy();
            case 2 -> new YieldingWaitStrategy();
            default -> new BlockingWaitStrategy();
        };
    }
}
