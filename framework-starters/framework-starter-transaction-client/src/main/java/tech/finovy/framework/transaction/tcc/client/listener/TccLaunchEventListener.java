package tech.finovy.framework.transaction.tcc.client.listener;


import tech.finovy.framework.transaction.tcc.client.api.event.AbstractTransactionEventListener;
import tech.finovy.framework.transaction.tcc.client.api.event.TccLaunchEvent;
import tech.finovy.framework.transaction.tcc.client.context.TccClientContext;
import tech.finovy.framework.transaction.tcc.client.log.Log;

public class TccLaunchEventListener<T> extends AbstractTransactionEventListener<TccLaunchEvent<T>> {

    @Override
    public boolean onEvent(TccLaunchEvent<T> event) {
        // 1.获取txId
        final String txId;
        try {
            txId = transactionRemoteService.getTxId(event.getTypeName());
            event.getHeaders().put(TccClientContext.TX_ID, txId);
        } catch (Exception e) {
            Log.error("can not get txId : {}", e.getMessage(), e);
            return false;
        }
        // 2.发起事务
        return transactionRemoteService.launch(event.getTypeName(), txId, event.getHeaders(), event.getParam());
    }


}
