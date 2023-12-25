package tech.finovy.framework.transaction.tcc.client.core;

import org.springframework.stereotype.Component;

@Component
public class InnerDispatchHandler extends AbstractDispatchHandler {

    public InnerDispatchHandler(ServiceContainer container) {
        super(container);
    }
}
