package tech.finovy.framework.transaction.tcc.client.core;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import tech.finovy.framework.transaction.tcc.client.api.TccClientService;
import tech.finovy.framework.transaction.tcc.client.constant.Constants;
import tech.finovy.framework.transaction.tcc.client.context.TccClientContext;
import tech.finovy.framework.transaction.tcc.client.fence.TCCFenceHandler;

public class TccFenceServiceProxy<T> {

    private final TccClientService<T> service;

    private TccFenceServiceProxy(TccClientService<T> service) {
        this.service = service;
    }

    public static <T> TccFenceServiceProxy<T> newInstance(TccClientService<T> service) {
        return new TccFenceServiceProxy<>(service);
    }

    public static boolean isEnableFence() {
        final String enable = System.getenv(Constants.FENCE_LOG_ENABLE);
        if (StringUtils.isNotEmpty(enable)) {
            return Boolean.parseBoolean(enable);
        }
        return true;
    }

    @SneakyThrows
    public boolean execute(String methodName, T t) {
        return switch (methodName) {
            case "prepare" ->
                    TCCFenceHandler.prepareFence(TccClientContext.getTxId(), TccClientContext.getBranchId(), TccClientContext.getTxName(), service, t);
            case "commit" ->
                    TCCFenceHandler.commitFence(TccClientContext.getTxId(), TccClientContext.getBranchId(), service, t);
            default ->
                // rollback
                    TCCFenceHandler.rollbackFence(TccClientContext.getTxId(), TccClientContext.getBranchId(), TccClientContext.getTxName(), service, t);
        };
    }
}
