package tech.finovy.framework.config.nacos.listener.entity;


public class ShardingEngineExecutionConstant {

    public static final String TCC_CALL_PREPARE = "prepare";
    public static final String TCC_CALL_COMMIT = "commit";
    public static final String TCC_CALL_ROLLBACK = "rollback";

    private ShardingEngineExecutionConstant() {
    }

}
