package tech.finovy.framework.core.common.loader;

@LoadLevel(name = "EnglishHello", order = 1)
public class EnglishHello implements Hello {

    @Override
    public String say() {
        return "hello!";
    }
}
