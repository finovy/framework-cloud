package tech.finovy.framework.transaction.tcc.client.exception;

import java.io.Serializable;

public class TccClientException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 872810047788537741L;
    /**
     * 错误码
     */
    protected int errorCode;
    /**
     * 错误信息
     */
    protected String errorMsg;

    /**
     * 运行时异常
     */
    public TccClientException() {
        super();
    }

    /**
     * 简单异常
     *
     * @param errorCode
     * @param errorMsg
     */
    public TccClientException(int errorCode, String errorMsg) {
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "errorCode=" + errorCode + ",errorMsg=" + errorMsg;
    }
}
