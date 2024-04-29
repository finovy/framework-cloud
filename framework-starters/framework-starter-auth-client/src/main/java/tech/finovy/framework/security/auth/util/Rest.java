package tech.finovy.framework.security.auth.util;

/**
 * The interface Rest.
 */
public interface Rest<T> {
    /**
     * 状态码 .
     *
     * @param code the http status
     */
    void setCode(int code);

    /**
     * 数据载体.
     *
     * @param data the data
     */
    void setData(T data);

    /**
     * 提示信息.
     *
     * @param msg the msg
     */
    void setMsg(String msg);

    /**
     * Sets identifier.
     *
     * @param identifier 标识
     */
    void setIdentifier(String identifier);
}
