package tech.finovy.framework.common.core;

import tech.finovy.framework.common.core.loader.LoadLevel;
import tech.finovy.framework.common.core.loader.Scope;

@LoadLevel(name = "person", order = 1, scope = Scope.SINGLETON)
public class Person {

    String name;
    int age;
}
