package tech.finovy.framework.listener;


public abstract class AbstractDistributedListener implements DistributedListener {
    protected int order;
    protected String key;
    protected String index;

    @Override
    public void init() {

    }

    @Override
    public boolean isPrefixMatch() {
        return false;
    }

    @Override
    public boolean isEnable() {
        return true;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public String getKey() {
        return key != null && !key.isBlank() ? key : this.getClass().getSimpleName();
    }

    @Override
    public void setIndex(String index) {
        this.index = index;
    }

    @Override
    public String getIndex() {
        return index;
    }
}
