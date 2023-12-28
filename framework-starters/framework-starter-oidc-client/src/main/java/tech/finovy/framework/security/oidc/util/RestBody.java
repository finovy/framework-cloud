package tech.finovy.framework.security.oidc.util;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RestBody<T> implements Rest<T>, Serializable {

    @Serial
    private static final long serialVersionUID = -7616216747521482608L;
    private int code = 0;
    private T data;
    private String msg = "";
    private String identifier = "";

    /**
     * Ok rest.
     *
     * @return the rest
     */
    public static Rest ok() {
        return new RestBody();
    }

    /**
     * Ok rest.
     *
     * @param msg the msg
     * @return the rest
     */
    public static Rest ok(String msg) {
        Rest restBody = new RestBody();
        restBody.setMsg(msg);
        return restBody;
    }

    /**
     * Ok data rest.
     *
     * @param <T> the type parameter
     * @return the rest
     */
    public static <T> Rest<T> okData(T data) {
        Rest<T> restBody = new RestBody<>();
        restBody.setData(data);
        return restBody;
    }

    public static <T> Rest<T> okData(T data, String msg) {
        Rest<T> restBody = new RestBody<>();
        restBody.setData(data);
        restBody.setMsg(msg);
        return restBody;
    }

    /**
     * Build rest.
     *
     * @param <T>        the type parameter
     * @param code       the http status
     * @param data       the data
     * @param msg        the msg
     * @param identifier the identifier
     * @return the rest
     */
    public static <T> Rest<T> build(int code, T data, String msg, String identifier) {
        Rest<T> restBody = new RestBody<>();
        restBody.setCode(code);
        restBody.setData(data);
        restBody.setMsg(msg);
        restBody.setIdentifier(identifier);
        return restBody;
    }


    /**
     * Failure rest.
     *
     * @param msg the msg
     * @return the rest
     */
    public static Rest failure(int code, String msg) {
        Rest restBody = new RestBody();
        restBody.setCode(code);
        restBody.setMsg(msg);
        restBody.setIdentifier("-9999");
        return restBody;
    }

    /**
     * Failure data rest.
     *
     * @param <T>  the type parameter
     * @param data the data
     * @param msg  the msg
     * @return the rest
     */
    public static <T> Rest<T> failureData(T data, String msg, String identifier) {
        Rest<T> restBody = new RestBody<>();
        restBody.setIdentifier(identifier);
        restBody.setData(data);
        restBody.setMsg(msg);
        return restBody;
    }

    @Override
    public String toString() {
        return "{" +
                "code:" + code +
                ", data:" + data +
                ", msg:" + msg +
                ", identifier:" + identifier +
                '}';
    }
}
