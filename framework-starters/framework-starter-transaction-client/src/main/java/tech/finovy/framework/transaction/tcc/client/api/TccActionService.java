package tech.finovy.framework.transaction.tcc.client.api;

import tech.finovy.framework.transaction.tcc.client.api.result.ExecuteResult;

import java.util.Map;

public interface TccActionService {

    /**
     * 发起事务
     *
     * @param typeName 业务名称(与服务端配置一致)
     * @param headers  请求头，兼容多租户
     * @param t        透传参数
     * @return 是否成功
     */
    <T> boolean launch(String typeName, Map<String, String> headers, T t);


    /**
     * 发起事务
     *
     * @param typeName 业务名称(与服务端配置一致)
     * @param headers  请求头，兼容多租户
     * @param t        透传参数
     * @return 执行结果
     */
    <T> ExecuteResult launchTransaction(String typeName, Map<String, String> headers, T t);

}
