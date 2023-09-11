package tech.finovy.framework.core.common.loader.spi;

import tech.finovy.framework.core.common.loader.LoadLevel;
import tech.finovy.framework.core.common.loader.Scope;

@LoadLevel(name = "PingHello", order = 1, scope = Scope.SINGLETON)
public class PingHello implements Echo {
    private String hello;

    public PingHello() {
    }

    public PingHello(String hello) {
        this.hello = hello;
    }

    @Override
    public String say() {
        return hello;
    }
}
