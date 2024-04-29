package tech.finovy.framework.common.core.exception;


public class DataAccessException extends StoreException {

    /**
     * constructor with framework error code
     *
     * @param err the framework error code
     */
    public DataAccessException(FrameworkErrorCode err) {
        super(err);
    }

    /**
     * constructor with msg
     *
     * @param msg the msg
     */
    public DataAccessException(String msg) {
        super(msg);
    }

    /**
     * constructor with cause
     *
     * @param cause the cause
     */
    public DataAccessException(Throwable cause) {
        super(cause);
    }

    /**
     * constructor with msg and framework error code
     *
     * @param msg     the msg
     * @param errCode the framework error code
     */
    public DataAccessException(String msg, FrameworkErrorCode errCode) {
        super(msg, errCode);
    }

    /**
     * constructor with cause and msg and framework error code
     *
     * @param cause   the throwable
     * @param msg     the msg
     * @param errCode the framework error code
     */
    public DataAccessException(Throwable cause, String msg, FrameworkErrorCode errCode) {
        super(cause, msg, errCode);
    }


}
