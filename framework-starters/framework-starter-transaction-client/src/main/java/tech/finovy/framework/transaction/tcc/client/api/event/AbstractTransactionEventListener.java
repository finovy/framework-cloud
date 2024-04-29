package tech.finovy.framework.transaction.tcc.client.api.event;

import org.reflections.Reflections;
import tech.finovy.framework.transaction.tcc.client.api.inner.TransactionRemoteService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Set;

public abstract class AbstractTransactionEventListener<T extends BaseEvent> implements TransactionEventListener<T> {

    protected static TransactionRemoteService transactionRemoteService;

    static {
        Reflections reflections = new Reflections("tech.finovy.framework.transaction.tcc.client");
        Set<Class<? extends TransactionRemoteService>> classes = reflections.getSubTypesOf(TransactionRemoteService.class);
        transactionRemoteService = classes.stream()
                .filter(cls -> !Modifier.isAbstract(cls.getModifiers()))
                .findFirst()
                .map(cls -> {
                    try {
                        return cls.getConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                             NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElseThrow(() -> new RuntimeException("No implementation of TransactionRemoteService found."));
    }

    public AbstractTransactionEventListener() {
    }
}
