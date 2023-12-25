package tech.finovy.framework.transaction.tcc.client.api.event;

public interface TransactionEventListener<T extends BaseEvent> {

    boolean onEvent(T event);

}
