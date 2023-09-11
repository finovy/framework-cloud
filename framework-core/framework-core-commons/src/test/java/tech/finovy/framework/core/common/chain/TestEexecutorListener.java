package tech.finovy.framework.core.common.chain;

import org.springframework.stereotype.Service;

@Service
public class TestEexecutorListener extends AbstractChainListener implements TestListenerInterface {
    @Override
    public String getType() {
        return "TestListener1;TEST1";
    }

    @Override
    public boolean isEnable() {
        return true;
    }
}
