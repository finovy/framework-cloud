package tech.finovy.framework.http.entity;

import java.util.HashMap;
import java.util.Map;

public class HttpValue implements java.io.Serializable{
	private static final long serialVersionUID = 7196565081539930216L;
	private String action;
	private Map<String, Object> paramater=null;
	private Object requestBody=null;
	public String getAction() {
		if(action==null) {
			return "";
		}
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Map<String, Object> getParamater() {		
		return paramater;
	}

	public void addParamater(String key,Object value){
		if(paramater==null) {
			paramater=new HashMap<>();
		}
		paramater.put(key, value);
	}
	public void setParamater(Map<String, Object> paramater) {
		this.paramater = paramater;
	}

	public String getRequestUrl() {
		if (paramater == null) {
			return action;
		}
		if (paramater.size() == 0) {
			return action;
		}
		StringBuffer params = new StringBuffer(action + "?");
		for (Map.Entry<String,Object> ei: paramater.entrySet()) {
			params.append(ei.getKey());
			params.append("=");
			params.append(ei.getValue());
			params.append("&");
		}
		if (params.length() > 0) {
			params.deleteCharAt(params.length() - 1);
		}
		return params.toString();
	}

	public Object getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(Object requestBody) {
		this.requestBody = requestBody;
	}
	
}
