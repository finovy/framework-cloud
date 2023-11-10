package tech.finovy.framework.listener;

import lombok.extern.slf4j.Slf4j;
import tech.finovy.framework.listener.exception.DistributedListenerException;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class DistributedListenerFactoryManager {

    public static <T extends DistributedListener> Map<String, Map<String, T>> multiDistributedListenerSort(Map<String, T> filters) {
        Map<String, Map<String, T>> distributedListeners = new LinkedHashMap<>();
        filters.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(new DistributedListenerComparator()))
                .forEachOrdered(x -> initListenterMap(distributedListeners, x.getValue()));
        for (Map.Entry<String, Map<String, T>> type : distributedListeners.entrySet()) {
            Map<String, T> m = type.getValue();
            int index = 0;
            int size = m.size();
            for (Map.Entry<String, T> item : m.entrySet()) {
                T listener = item.getValue();
                listener.setIndex((++index) + "/" + size);
                listener.init();
                log.info("Init DistributedListener type:{},key:{},Order:{},Index:{}", type.getKey(), listener.getKey(), listener.getOrder(), listener.getIndex());
            }
        }

        return distributedListeners;
    }

    private static <T extends DistributedListener> void initListenterMap(Map<String, Map<String, T>> distributedListeners, T listener) {
        if (listener.getType() == null) {
            log.error("DistributedListener Type IS NULL");
            throw new DistributedListenerException("DistributedListener Type IS NULL");
        }
        String listenerType = listener.getType().replaceAll(",", ";");
        String[] types = listenerType.split(";");
        for (String type : types) {
            Map<String, T> listenerMap = distributedListeners.computeIfAbsent(type, k -> new LinkedHashMap<>());
            listenerMap.put(listener.getKey(), listener);
        }
    }

    public static <T extends DistributedListener> Map<String, T> singleDistributedListenerSort(Map<String, T> schedulerListeners) {
        Map<String, T> distributed = new LinkedHashMap<>();
        schedulerListeners.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(new DistributedListenerComparator()))
                .forEachOrdered(x -> distributed.put(x.getKey(), x.getValue()));
        int index = 0;
        int size = distributed.size();
        for (Map.Entry<String, T> item : distributed.entrySet()) {
            T listener = item.getValue();
            listener.setIndex((++index) + "/" + size);
            listener.init();
            log.info("Init DistributedListener:{},index:{}", listener.getKey(), listener.getIndex());
        }
        return distributed;
    }
}
