package tech.finovy.framework.global.filter.dubbo;

import tech.finovy.framework.global.Constant;
import tech.finovy.framework.global.TenantContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.slf4j.MDC;


@Slf4j
@Activate
public class DubboProviderContextFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) {
        if(RpcContext.getContext().getAttachment(Constant.APPID) != null) {
            MDC.put(Constant.APPID, RpcContext.getContext().getAttachment(Constant.APPID));
            TenantContext.setCurrentTenant(RpcContext.getContext().getAttachment(Constant.APPID));
        }
        if(RpcContext.getContext().getAttachment(Constant.TRACEID) != null) {
            MDC.put(Constant.TRACEID, RpcContext.getContext().getAttachment(Constant.TRACEID));
            TenantContext.setCurrentTraceId(RpcContext.getContext().getAttachment(Constant.TRACEID));
        }
        return invoker.invoke(invocation);
    }
}
