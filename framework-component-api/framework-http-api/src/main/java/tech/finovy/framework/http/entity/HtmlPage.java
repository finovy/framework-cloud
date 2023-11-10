package tech.finovy.framework.http.entity;

import java.util.List;
import java.util.Map;

public class HtmlPage implements java.io.Serializable{
	private static final long serialVersionUID = 7342852609611418396L;
	private int statusCode;
	private String errMsg;
	private String html;
	private String encode;
	private List<Map<String,String>> headers;

	public final List<Map<String, String>> getHeaders() {
		return headers;
	}
	public final void setHeaders(List<Map<String, String>> headers) {
		this.headers = headers;
	}
	public final boolean isOk() {
		return errMsg==null;
	}
	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

}
