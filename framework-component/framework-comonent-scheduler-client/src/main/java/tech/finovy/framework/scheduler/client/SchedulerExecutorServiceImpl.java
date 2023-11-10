package tech.finovy.framework.scheduler.client;


import lombok.extern.slf4j.Slf4j;
import tech.finovy.framework.scheduler.entity.RemoteJobExecuteConfig;
import tech.finovy.framework.scheduler.entity.RemoteJobExecuteResult;
import tech.finovy.framework.scheduler.entity.RemoteJobProcessConfig;
import tech.finovy.framework.scheduler.listener.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
public class SchedulerExecutorServiceImpl<T> implements SchedulerExecutorService<T> {


    private final Map<String, Map<String, SchedulerExecutorListener>> listeners;
    private final Map<String, Map<String, SchedulerFlowListener<T>>> flowListeners;

    public SchedulerExecutorServiceImpl(Map<String, Map<String, SchedulerExecutorListener>> listeners,Map<String, Map<String, SchedulerFlowListener<T>>> flowListeners){
        this.listeners = listeners;
        this.flowListeners = flowListeners;
    }
    @Override
    public RemoteJobExecuteResult trigger(RemoteJobExecuteConfig config) {
        Map<String, SchedulerExecutorListener> listenerMap = listeners.get(config.getJobKey());
        if (listenerMap == null) {
            listenerMap = listeners.get(SchedulerExecutorNotExistsListener.NOT_EXISTS_SCHEDULER_EXECUTOR_LISTENER);
        }
        Map<String, SchedulerExecutorListener> globalListener=  listeners.get(AbstractSchedulerExecutorGlobalListener.SCHEDULER_EXECUTOR_GLOBAL_LISTENER);
        RemoteJobExecuteResult r = new RemoteJobExecuteResult();
        if(globalListener!=null){
            for (Map.Entry<String, SchedulerExecutorListener> each : listenerMap.entrySet()) {
                SchedulerExecutorListener listener = each.getValue();
                if (!listener.isEnable()) {
                    continue;
                }
                r = listener.trigger(config);
                if (!r.isSuccess()) {
                    return r;
                }
            }
        }
        for (Map.Entry<String, SchedulerExecutorListener> each : listenerMap.entrySet()) {
            SchedulerExecutorListener listener = each.getValue();
            if (!listener.isEnable()) {
                continue;
            }
            r = listener.trigger(config);
            if (!r.isSuccess()) {
                return r;
            }
        }
        return r;
    }

    @Override
    public List<T> fetch(RemoteJobExecuteConfig config) {
        Map<String, SchedulerFlowListener<T>> listenerMap = flowListeners.get(config.getJobKey());
        if (listenerMap == null) {
            listenerMap = flowListeners.get(SchedulerFlowNotExistsListener.NOT_EXISTS_SCHEDULER_FLOW_LISTENER);
        }
        List<T> lis = new ArrayList<>();
        for (Map.Entry<String, SchedulerFlowListener<T>> each : listenerMap.entrySet()) {
            SchedulerFlowListener<T> listener = each.getValue();
            if (!listener.isEnable()) {
                continue;
            }
            List<T> list = listener.fetch(config);
            if (list != null && !list.isEmpty()) {
                lis.addAll(list);
            }
        }
        return lis;
    }

    @Override
    public RemoteJobExecuteResult process(RemoteJobProcessConfig<T> config) {
        Map<String, SchedulerFlowListener<T>> listenerMap = flowListeners.get(config.getConfig().getJobKey());
        if (listenerMap == null) {
            listenerMap = flowListeners.get(SchedulerFlowNotExistsListener.NOT_EXISTS_SCHEDULER_FLOW_LISTENER);
        }
        RemoteJobExecuteResult list = new RemoteJobExecuteResult();
        for (Map.Entry<String, SchedulerFlowListener<T>> each : listenerMap.entrySet()) {
            SchedulerFlowListener<T> listener = each.getValue();
            if (!listener.isEnable()) {
                continue;
            }
            list = listener.process(config);
            if (list != null && !list.isSuccess()) {
                return list;
            }
        }
        return list;
    }

}
