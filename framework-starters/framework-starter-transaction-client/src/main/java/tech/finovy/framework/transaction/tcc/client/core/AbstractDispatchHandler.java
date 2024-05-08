package tech.finovy.framework.transaction.tcc.client.core;

import com.alibaba.fastjson2.TypeReference;
import lombok.SneakyThrows;
import tech.finovy.framework.transaction.tcc.client.api.TccClientService;
import tech.finovy.framework.transaction.tcc.client.auth.SecurityContent;
import tech.finovy.framework.transaction.tcc.client.auth.SecurityHelper;
import tech.finovy.framework.transaction.tcc.client.log.Log;

import static tech.finovy.framework.transaction.tcc.client.constant.Constants.SECRET_KEY;


public abstract  class AbstractDispatchHandler<T> implements DispatchHandler {

    private final ServiceContainer<T> container;


    public AbstractDispatchHandler(ServiceContainer<T> container) {
        this.container = container;
    }

    @SneakyThrows
    public boolean dispatch(SecurityContent content, String typeName, String methodName) {
        final TccClientService<T> service = container.serviceMap.get(typeName);
        if (service == null) {
            Log.warn("Please check your code,because this [{}] handler is Null!", typeName);
            return false;
        }
        final TypeReference<T> type = service.getType();
        T t = SecurityHelper.decrypt(typeName, System.getenv(SECRET_KEY), content, type);
        // 参数解密
        return TccFenceServiceProxy.newInstance(service).execute(methodName, t);
    }
}
