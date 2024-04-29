package tech.finovy.framework.common.core.chain;


public class Test3ExecutorListener extends AbstractChainListener implements TestListenerInterface {
    @Override
    public String getType() {
        return "TEST2";
    }

    @Override
    public boolean isEnable() {
        return true;
    }

    @Override
    public int getOrder() {
        return 3;
    }
}
