package tech.finovy.framework.gloabl.interceptor.util;

import tech.finovy.framework.global.TenantContext;

import java.util.Map;
import java.util.concurrent.Callable;

import static tech.finovy.framework.global.Constant.APPID;

public class TenantUtils {

    /**
     * 使用指定租户，执行对应的逻辑
     * <p>
     * 注意，如果当前是忽略租户的情况下，会被强制设置成不忽略租户
     * 当然，执行完成后，还是会恢复回去
     *
     * @param tenantId 租户编号
     * @param runnable 逻辑
     */
    public static void execute(String tenantId, Runnable runnable) {
        executeInner(tenantId, () -> {
            runnable.run();
            return null;
        });
    }

    /**
     * 使用指定租户，执行对应的逻辑
     * <p>
     * 注意，如果当前是忽略租户的情况下，会被强制设置成不忽略租户
     * 当然，执行完成后，还是会恢复回去
     *
     * @param tenantId 租户编号
     * @param callable 逻辑
     */
    public static <V> V execute(String tenantId, Callable<V> callable) {
        return executeInner(tenantId, callable);
    }

    /**
     * 忽略租户，执行对应的逻辑
     *
     * @param runnable 逻辑
     */
    public static void executeIgnore(Runnable runnable) {
        executeInner(() -> {
            runnable.run();
            return null;
        });
    }

    public static <V> V executeIgnore(Callable<V> callable) {
        return executeInner(callable);
    }

    // -----------------------------------------

    private static <V> V executeInner(String tenantId, Callable<V> callable) {
        String tenantIdSnap = TenantContext.getCurrentTenant();
        boolean ignoreSnap = TenantContext.isIgnoreTenant();
        try {
            TenantContext.setCurrentTenant(tenantId);
            TenantContext.setIgnoreTenant(false);
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            TenantContext.setCurrentTenant(tenantIdSnap);
            TenantContext.setIgnoreTenant(ignoreSnap);
        }
    }

    private static <V> V executeInner(Callable<V> callable) {
        boolean ignoreSnap = TenantContext.isIgnoreTenant();
        try {
            TenantContext.setIgnoreTenant(true);
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            TenantContext.setIgnoreTenant(ignoreSnap);
        }
    }

    /**
     * 将多租户编号，添加到 header 中
     *
     * @param headers  HTTP 请求 headers
     * @param tenantId 租户编号
     */
    public static void addTenantHeader(Map<String, String> headers, Long tenantId) {
        if (tenantId != null) {
            headers.put(APPID, tenantId.toString());
        }
    }
}
