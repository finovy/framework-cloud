package tech.finovy.framework.core.common.loader;

@LoadLevel(name = "ChineseHello", order = Integer.MIN_VALUE)
public class ChineseHello implements Hello {

    @Override
    public String say() {
        return "ni hao!";
    }
}
