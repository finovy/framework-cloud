package tech.finovy.framework.transaction.tcc.client.core;

import com.alibaba.fastjson2.JSON;
import org.apache.dubbo.rpc.RpcContext;
import tech.finovy.framework.transaction.tcc.client.auth.SecurityContent;
import tech.finovy.framework.transaction.tcc.client.constant.Constants;
import tech.finovy.framework.transaction.tcc.client.context.TccClientContext;

public class RpcDispatchHandler extends AbstractDispatchHandler {

    public RpcDispatchHandler(ServiceContainer container) {
        super(container);
    }

    @Override
    public boolean dispatch(SecurityContent content, String typeName, String methodName) {
        try {
            final TccClientContext.TxInfo txInfo = JSON.parseObject(RpcContext.getContext().getAttachment(Constants.RPC_CONTEXT_INFO), TccClientContext.TxInfo.class);
            TccClientContext.setTx(txInfo);
            return super.dispatch(content, typeName, methodName);
        } finally {
            RpcContext.getContext().clearAttachments();
            TccClientContext.clear();
        }
    }
}
