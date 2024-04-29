package tech.finovy.framework.common.core.listener;


import tech.finovy.framework.common.core.chain.ChainListener;

public interface ShardingEngineSchedulerRefreshListener extends ChainListener {
    void trigger(long trigger);

    void startup();
}
