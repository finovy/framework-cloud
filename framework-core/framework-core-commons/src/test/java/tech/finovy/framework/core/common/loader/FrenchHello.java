package tech.finovy.framework.core.common.loader;


@LoadLevel(name = "FrenchHello", order = 2)
public class FrenchHello implements Hello {

    @Override
    public String say() {
        return "Bonjour";
    }
}
