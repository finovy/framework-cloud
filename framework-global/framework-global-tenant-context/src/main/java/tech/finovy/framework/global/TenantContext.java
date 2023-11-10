package tech.finovy.framework.global;

import com.alibaba.ttl.TransmittableThreadLocal;


public class TenantContext {
    private TenantContext() {
    }

    private static final TransmittableThreadLocal<String> currentTenant = new TransmittableThreadLocal<>();
    private static final TransmittableThreadLocal<String> currentTraceId = new TransmittableThreadLocal<>();

    public static void setCurrentTenant(String appid) {
        if (appid == null) {
            return;
        }
        currentTenant.set(appid);
    }

    public static void setCurrentTraceId(String traceId) {
        if (traceId == null) {
            return;
        }
        currentTraceId.set(traceId);
    }

    public static String getCurrentTenant() {
        return currentTenant.get();
    }

    public static String getCurrentTraceId() {
        return currentTraceId.get();
    }

    public static void clear() {
        currentTenant.remove();
        currentTraceId.remove();
    }
}
