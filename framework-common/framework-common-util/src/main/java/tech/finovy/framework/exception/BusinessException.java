package tech.finovy.framework.exception;


import tech.finovy.framework.result.IResultCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 自定义业务异常
 */
public class BusinessException extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = 872810047788537740L;
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
    public BusinessException() {
        super();
    }

    /**
     * 简单异常
     *
     * @param errorCode
     * @param errorMsg
     */
    public BusinessException(int errorCode, String errorMsg) {
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
    }

    /**
     * 枚举异常
     *
     * @param exEnum
     */
    public BusinessException(CommonExEnum exEnum) {
        this.errorMsg = exEnum.getMessage();
        this.errorCode = exEnum.getCode();
    }

    /**
     * 枚举异常
     *
     * @param resultCode
     */
    public BusinessException(IResultCode resultCode) {
        this.errorMsg = resultCode.getMessage();
        this.errorCode = resultCode.getCode();
    }

    /**
     * 自定义异常错误信息（ 相同code不同异常信息）
     *
     * @param exEnum
     * @param errorMsg
     */
    public BusinessException(CommonExEnum exEnum, String errorMsg) {
        this.errorMsg = errorMsg;
        this.errorCode = exEnum.getCode();
    }

    public BusinessException(String errorMsg) {
        this.errorMsg = errorMsg;
        this.errorCode = 500;
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
