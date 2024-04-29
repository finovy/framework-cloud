package tech.finovy.framework.transaction.tcc.client.listener;

import org.reflections.Reflections;
import tech.finovy.framework.transaction.tcc.client.api.event.BaseEvent;
import tech.finovy.framework.transaction.tcc.client.api.event.TransactionEventListener;

import java.lang.reflect.*;
import java.util.*;

public class EventDispatcher {


    private static final Map<Type, TransactionEventListener<BaseEvent>> PROCESSORS = new HashMap<>();

    static {
        List<TransactionEventListener<BaseEvent>> LISTENERS = new ArrayList<>();
        // listener init
        Reflections reflections = new Reflections(EventDispatcher.class.getPackage().getName());
        Set<Class<? extends TransactionEventListener>> classes = reflections.getSubTypesOf(TransactionEventListener.class);
        classes.stream()
                .filter(cls -> !Modifier.isAbstract(cls.getModifiers()))
                .map(cls -> {
                    try {
                        final Constructor<? extends TransactionEventListener> constructor = cls.getConstructor();
                        return constructor.newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                             NoSuchMethodException e) {
                        throw new RuntimeException("Failed to instantiate listener", e);
                    }
                })
                .forEach(LISTENERS::add);
        // handler init
        for (TransactionEventListener<BaseEvent> listener : LISTENERS) {
            // 获取listener的泛型类型参数
            Type type = listener.getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type[] typeArguments = parameterizedType.getActualTypeArguments();
                if (typeArguments.length > 0) {
                    Type typeArgument;
                    if (typeArguments[0] instanceof ParameterizedType) {
                        ParameterizedType argument = (ParameterizedType) typeArguments[0];
                        typeArgument = argument.getRawType();
                    } else {
                        typeArgument = typeArguments[0];
                    }
                    PROCESSORS.put(typeArgument, listener);
                }
            }
        }
    }

    public static <T extends BaseEvent> boolean dispatch(T event) {
        return PROCESSORS.get(event.getClass()).onEvent(event);
    }
}





