package tech.finovy.framework.transaction.tcc.client.api.inner;


import java.util.Map;

public interface TransactionRemoteService {

    /**
     * 获取全局事务Id
     *
     * @param typeName – 业务名称(与服务端配置一致)
     * @return txId
     */
    String getTxId(String typeName);

    /**
     * typeName – 业务名称(与服务端配置一致)
     * txId- 全局事务id
     * body – 业务传递数据
     *
     * @return 发起是否成功
     */
    <T> boolean launch(String typeName, String txId, Map<String, String> headers, T body);

    /**
     * 健康检查
     *
     * @return 是否健康
     */
    boolean healthcheck(String host);

}
