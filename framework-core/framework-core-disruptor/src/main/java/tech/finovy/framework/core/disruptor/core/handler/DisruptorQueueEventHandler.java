package tech.finovy.framework.core.disruptor.core.handler;

import com.lmax.disruptor.WorkHandler;
import tech.finovy.framework.core.disruptor.core.DisruptorEventConfiguration;
import tech.finovy.framework.core.disruptor.core.DisruptorEventConstant;
import tech.finovy.framework.core.disruptor.core.event.DisruptorEvent;
import tech.finovy.framework.core.disruptor.spi.ProcessInterface;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author dtype.huang
 */
@Slf4j
public class DisruptorQueueEventHandler implements WorkHandler<DisruptorEvent> {
    protected final Map<String, Map<String, ProcessInterface>> disruptorListenters;
    protected final int handlerId;
    private final DisruptorEventConfiguration configuration;

    public DisruptorQueueEventHandler(Map<String, Map<String, ProcessInterface>> disruptorListenters, DisruptorEventConfiguration configuration, int handlerId) {
        this.configuration = configuration;
        this.handlerId = handlerId;
        this.disruptorListenters = disruptorListenters;
    }

    private void doAction(String type, DisruptorEvent event) {
        Map<String, ProcessInterface> eventListeners = disruptorListenters.get(type);
        if (eventListeners != null) {
            for (Map.Entry<String, ProcessInterface> listener : eventListeners.entrySet()) {
                ProcessInterface each = listener.getValue();
                Set<String> exec = event.getExecuteListener();
                if (exec == null) {
                    exec = new HashSet<>(eventListeners.size());
                    event.setExecuteListener(exec);
                }
                if (exec.contains(each.getKey())) {
                    continue;
                }
                each.onEvent(event, handlerId);
                exec.add(each.getKey());
            }
        }
    }

    @Override
    public void onEvent(DisruptorEvent event) {
        doAction(DisruptorEventConstant.SYS_GLOBAL_EVENT_LISTENER_TYPE, event);
        doAction(event.getDisruptorEventType(), event);
    }
}
