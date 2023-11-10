package tech.finovy.framework.http.api;

import tech.finovy.framework.http.entity.*;

import java.util.Map;



public interface RetrofitUtilService {
	JResultSet getJResultSet(HttpHost httpHost,HttpValue httvalue);
	JResultSet postJResultSet(HttpHost httpHost,HttpValue httvalue);
	Message<Map<String, Object>> getMessage(HttpHost httpHost,HttpValue httvalue);
	Message<Map<String, Object>> postMessage(HttpHost httpHost,HttpValue httvalue);
	<T> HttpPack<T> getPack(Class<T> dataType,HttpHost httpHost,HttpValue httvalue);
	<T> HttpPack<T> postPack(Class<T> dataType,HttpHost httpHost,HttpValue httvalue);
}
