package tech.finovy.framework.core.common.chain;


public interface ChainListener {
    int getOrder();

    String getType();

    String getKey();

    String getIndex();

    void setIndex(String index);

    boolean isEnable();
    boolean isAsync();

    void init();
}
