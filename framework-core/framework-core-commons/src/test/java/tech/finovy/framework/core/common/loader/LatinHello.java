package tech.finovy.framework.core.common.loader;

@LoadLevel(name = "LatinHello", order = 3, scope = Scope.PROTOTYPE)
public class LatinHello implements Hello {
    @Override
    public String say() {
        return "123";
    }
}
