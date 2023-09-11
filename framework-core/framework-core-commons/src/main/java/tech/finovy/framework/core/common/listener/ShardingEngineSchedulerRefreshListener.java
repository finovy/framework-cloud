package tech.finovy.framework.core.common.listener;


import tech.finovy.framework.core.common.chain.ChainListener;

public interface ShardingEngineSchedulerRefreshListener extends ChainListener {
    void trigger(long trigger);

    void startup();
}
