package tech.finovy.framework.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.rpc.RpcContext;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import tech.finovy.framework.global.Constant;
import tech.finovy.framework.global.TenantContext;

@Slf4j
public abstract class AbstractTenantInterceptor implements HandlerInterceptor {
    private final TenantConfiguration tenantConfiguration;

    protected AbstractTenantInterceptor(TenantConfiguration tenantConfiguration) {
        this.tenantConfiguration = tenantConfiguration;
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        if (tenantConfiguration.isEnable()) {
            String traceId = httpServletRequest.getHeader(tenantConfiguration.getTraceIdKey());
            if (StringUtils.isNotBlank(traceId)) {
                RpcContext.getContext().setAttachment(tenantConfiguration.getTraceIdKey(), traceId);
                TenantContext.setCurrentTraceId(traceId);
                MDC.put(tenantConfiguration.getTraceIdKey(), traceId);
            }
        }
        String appId = httpServletRequest.getHeader(tenantConfiguration.getAppIdKey());
        if (StringUtils.isNotBlank(appId)) {
            MDC.put(tenantConfiguration.getAppIdKey(), appId);
            TenantContext.setCurrentTenant(appId);
            RpcContext.getContext().setAttachment(Constant.APPID, appId);
        }
        if (tenantConfiguration.isTraceDebug()) {
            log.info("TenantInterceptor {}:{}", tenantConfiguration.getTraceIdKey(), TenantContext.getCurrentTraceId());
            log.info("TenantInterceptor {}:{}", tenantConfiguration.getAppIdKey(), appId);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) {
        // clear Thread variable
        TenantContext.clear();
        MDC.clear();
    }
}
