package tech.finovy.framework.transaction.tcc.client;


import tech.finovy.framework.transaction.tcc.client.api.TccActionService;
import tech.finovy.framework.transaction.tcc.client.api.event.TccLaunchEvent;
import tech.finovy.framework.transaction.tcc.client.api.result.ExecuteResult;
import tech.finovy.framework.transaction.tcc.client.context.TccClientContext;
import tech.finovy.framework.transaction.tcc.client.listener.EventDispatcher;
import tech.finovy.framework.transaction.tcc.client.log.Log;

import java.util.Map;

public class TccActionServiceImpl implements TccActionService {

    @Override
    public <T> boolean launch(String typeName, Map<String, String> headers, T body) {
        final boolean isLaunch = EventDispatcher.dispatch(new TccLaunchEvent<>(typeName, headers, body));
        Log.info("tcc_launch:[{}] header:{} body:{}", isLaunch, headers, body);
        return isLaunch;
    }

    @Override
    public <T> ExecuteResult launchTransaction(String typeName, Map<String, String> headers, T t) {
        final long begin = System.currentTimeMillis();
        boolean result = this.launch(typeName, headers, t);
        String txId = headers.get(TccClientContext.TX_ID);
        return new ExecuteResult(txId, result, System.currentTimeMillis() - begin);
    }

}
