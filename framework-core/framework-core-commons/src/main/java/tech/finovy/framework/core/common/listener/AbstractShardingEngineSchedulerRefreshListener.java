package tech.finovy.framework.core.common.listener;

public abstract class AbstractShardingEngineSchedulerRefreshListener implements ShardingEngineSchedulerRefreshListener {
    protected String index;

    @Override
    public void startup() {

    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getType() {
        return "AbstractShardingEngineConfigurationRefreshListener";
    }

    @Override
    public String getKey() {
        return getClass().getSimpleName();
    }

    @Override
    public String getIndex() {
        return index;
    }

    @Override
    public void setIndex(String index) {
        this.index = index;
    }

    @Override
    public boolean isEnable() {
        return true;
    }

    @Override
    public void init() {

    }
}
