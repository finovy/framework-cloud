package tech.finovy.framework.core.common.chain;


import org.springframework.stereotype.Service;

@Service
public class Test2EexecutorListener extends AbstractChainListener implements TestListenerInterface {
    @Override
    public String getType() {
        return "TEST2";
    }

    @Override
    public boolean isEnable() {
        return true;
    }
}
