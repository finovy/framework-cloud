package tech.finovy.framework.http.entity;




public class Message<T> implements java.io.Serializable{
	private static final long serialVersionUID = 3602668965933196466L;
	private String code;
	private String msg;
	private String callBack;
	private String errMsg;
	private String errCode;
	private T data;
	final public boolean isOk() {
		return errMsg==null;
	}
	final public String getCallBack() {
		return callBack;
	}
	
	final public void setCallBack(String callBack) {
		this.callBack = callBack;
	}	

	final public String getCode() {
		return code;
	}

	final public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	final public String getErrMsg() {
		return errMsg;
	}

	final public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public void setData(T data) {
		this.data = data;		
	}

	final public String getErrCode() {
		return errCode;
	}

	final public void setErrCode(String errCode) {
		this.errCode = errCode;
	}	
}
