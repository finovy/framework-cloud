package tech.finovy.framework.http.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public class HttpPack<T> implements java.io.Serializable {
	private static final long serialVersionUID = -72725385119041300L;
	private T data;
	private String errMsg;
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
}
