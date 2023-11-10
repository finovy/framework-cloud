package tech.finovy.framework.global.interceptor;


import tech.finovy.framework.result.IResultCode;

public class MicroException extends RuntimeException {

    private static final long serialVersionUID = -8503564562180075863L;
    /**
     * 错误码
     */
    protected int errorCode;
    /**
     * 错误信息
     */
    protected String errorMsg;

    public MicroException() {
        super();
    }

    public MicroException(String errorMsg) {
        super(errorMsg);
    }

    public MicroException(int errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public MicroException(String message, Throwable cause) {
        super(message, cause);
    }

    public MicroException(Throwable cause) {
        super(cause);
    }

    public MicroException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * 枚举异常
     *
     * @param resultCode
     */
    public MicroException(IResultCode resultCode) {
        super(resultCode.getMessage());
        this.errorMsg = resultCode.getMessage();
        this.errorCode = resultCode.getCode();
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
}
