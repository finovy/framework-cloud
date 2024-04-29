package tech.finovy.framework.transaction.tcc.client.core;

import tech.finovy.framework.transaction.tcc.client.api.TccClientService;
import tech.finovy.framework.transaction.tcc.client.log.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceContainer<T> {
    public final Map<String, TccClientService<T>> serviceMap = new HashMap<>();

    public ServiceContainer(List<TccClientService<T>> services) {
        if (services == null) {
            return;
        }
        for (TccClientService<T> service : services) {
            serviceMap.put(service.getTypeName(), service);
        }
        Log.info("Services has loaded! [{}]", serviceMap.keySet());
    }

}
