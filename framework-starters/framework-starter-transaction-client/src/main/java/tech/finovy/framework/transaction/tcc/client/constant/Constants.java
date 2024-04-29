package tech.finovy.framework.transaction.tcc.client.constant;

public class Constants {

    public static final String SECRET_KEY = "secretKey";
    public static final String FENCE_LOG_ENABLE = "FENCE_LOG_ENABLE";
    public static final String DEFAULT_TCC_FENCE_LOG_TABLE_NAME = "tcc_fence_log";

    public static final String RPC_CONTEXT_INFO = "rpc_context_info";

    public static final String TCC_CLIENT_PREFIX = "sharding-engine.tcc.client";

    public static final String TENANT_SIGN = "X-Auth-Appid";
    public static final String TENANT_APPID = "appid";

    public static final String RPC_GROUP_KEY = "sharding-engine.tcc.client.group";

    public static class Status {
        public static final String UP = "UP";
        public static final String DOWN = "DOWN";
    }
}
