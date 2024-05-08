package tech.finovy.framework.transaction.tcc.client.api;

import com.alibaba.fastjson2.TypeReference;

/**
 * 客户端需实现此接口，用于TCC调用
 *
 */
public interface TccClientService<T> {

    boolean prepare(T t);

    boolean commit(T t);

    boolean rollback(T t);

    String getTypeName();

    TypeReference<T> getType();

}
