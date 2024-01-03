package tech.finovy.framework.transaction.tcc.client.listener;

import lombok.SneakyThrows;
import tech.finovy.framework.transaction.tcc.client.api.event.AbstractTransactionEventListener;
import tech.finovy.framework.transaction.tcc.client.api.event.HeartBeatEvent;
import tech.finovy.framework.transaction.tcc.client.constant.Constants;
import tech.finovy.framework.transaction.tcc.client.loadbalance.Instance;
import tech.finovy.framework.transaction.tcc.client.loadbalance.InstanceTable;
import tech.finovy.framework.transaction.tcc.client.log.Log;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;


public class HeartBeatEventListener extends AbstractTransactionEventListener<HeartBeatEvent> {

    public static final ConcurrentHashMap<String, Semaphore> controls = new ConcurrentHashMap<>(2);

    @SneakyThrows
    @Override
    public boolean onEvent(HeartBeatEvent event) {
        for (Instance instance : InstanceTable.getAllInstance()) {
            CompletableFuture.runAsync(() -> {
                Semaphore semaphore = controls.putIfAbsent(instance.getName() + instance.getUrl(), new Semaphore(1));
                try {
                    if (semaphore.tryAcquire()) {
                        boolean isHealth = transactionRemoteService.healthcheck(instance.getUrl());
                        instance.setStatus(isHealth ? Constants.Status.UP : Constants.Status.DOWN);
                        Log.debug("{}/{} instance healthcheck:{}", instance.getName(), instance.getStatus(), InstanceTable.getAllInstance());
                    }
                } finally {
                    semaphore.release();
                }
            });
        }
        return true;
    }

}
