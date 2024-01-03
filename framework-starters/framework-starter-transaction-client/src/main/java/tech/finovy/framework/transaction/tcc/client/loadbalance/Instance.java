package tech.finovy.framework.transaction.tcc.client.loadbalance;


import tech.finovy.framework.transaction.tcc.client.constant.Constants;

public class Instance implements Comparable<Instance> {
    /**
     * 实例名称
     */
    private String name;
    /**
     * 实例url
     */
    private String url;
    /**
     * 实例状态
     */
    private String status = Constants.Status.UP;
    /**
     * 实例优先级
     */
    private int order;

    @Override
    public int compareTo(Instance instance) {
        return order - instance.getOrder();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
