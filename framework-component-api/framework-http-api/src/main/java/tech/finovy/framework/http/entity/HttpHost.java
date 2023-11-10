package tech.finovy.framework.http.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
@Data
@NoArgsConstructor
public class HttpHost implements java.io.Serializable{
	private static final long serialVersionUID = 8230021584840133911L;
	private String host;
	private int apiType;
	private String encode;
	private Map<String,String> headers;
	public void addHeader(String key,String value) {
		if(headers==null) {
			headers=new HashMap<>(10);
		}
		headers.put(key, value);
	}
	public void setEncode(String encode) {
		if(encode!=null) {
		this.encode = encode.toLowerCase();
		}else {
			this.encode=null;
		}
	}
	
}
