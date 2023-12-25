package tech.finovy.framework.transaction.tcc.client.api;

import com.alibaba.fastjson.TypeReference;

/**
 * 客户端需实现此接口，用于TCC调用
 *
 * @Author: Ryan Luo
 * @Date: 2023/2/24 9:44
 */
public interface TccClientService<T> {

    boolean prepare(T t);

    boolean commit(T t);

    boolean rollback(T t);

    String getTypeName();

    TypeReference<T> getType();

}
