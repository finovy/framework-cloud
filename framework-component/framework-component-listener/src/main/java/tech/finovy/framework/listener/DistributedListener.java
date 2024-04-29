package tech.finovy.framework.listener;



public interface DistributedListener {
    int getOrder();
    String getType();
    String getKey();
    void setIndex(String index);
    String getIndex();
    boolean isEnable();
    boolean isPrefixMatch();
    void init();
}
