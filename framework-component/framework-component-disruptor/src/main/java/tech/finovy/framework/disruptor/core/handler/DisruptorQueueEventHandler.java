package tech.finovy.framework.disruptor.core.handler;

import com.lmax.disruptor.WorkHandler;
import tech.finovy.framework.disruptor.core.DisruptorEventConstant;
import tech.finovy.framework.disruptor.core.event.DisruptorEvent;
import tech.finovy.framework.disruptor.spi.ProcessInterface;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DisruptorQueueEventHandler implements WorkHandler<DisruptorEvent> {
    protected final Map<String, Map<String, ProcessInterface>> disruptorListeners;
    protected final int handlerId;

    public DisruptorQueueEventHandler(Map<String, Map<String, ProcessInterface>> disruptorListeners, int handlerId) {
        this.handlerId = handlerId;
        this.disruptorListeners = disruptorListeners;
    }

    private void doAction(String type, DisruptorEvent event) {
        Map<String, ProcessInterface> eventListeners = disruptorListeners.get(type);
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
