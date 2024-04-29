package tech.finovy.framework.common.core.chain;

public class Test1ExecutorListener extends AbstractChainListener implements TestListenerInterface {
    @Override
    public String getType() {
        return "TestListener1;TEST1";
    }

    @Override
    public boolean isEnable() {
        return true;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
