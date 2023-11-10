package tech.finovy.framework.global.filter.dubbo;

import tech.finovy.framework.global.Constant;
import tech.finovy.framework.global.TenantContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.slf4j.MDC;

@Slf4j
@Activate
public class DubboConsumerContextFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation)  {
        if(TenantContext.getCurrentTenant() != null) {
            MDC.put(Constant.APPID, TenantContext.getCurrentTenant());
            RpcContext.getContext().setAttachment(Constant.APPID, TenantContext.getCurrentTenant());
        }
        if(TenantContext.getCurrentTraceId() != null) {
            MDC.put(Constant.TRACEID, TenantContext.getCurrentTraceId());
            RpcContext.getContext().setAttachment(Constant.TRACEID, TenantContext.getCurrentTraceId());
        }
        return invoker.invoke(invocation);
    }
}
