package tech.finovy.framework.http.entity;

import com.alibaba.fastjson2.JSON;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.alibaba.fastjson2.util.TypeUtils.*;

public class JResultSet extends Message<List<Map<String, Object>>> {
	List<Map<String, Object>> data;
	private Set<String> keySet;
	private int size=-1;
	public JResultSet() {

	}
	public JResultSet(Message<List<Map<String, Object>>> simpleResultSet){
		super.setCallBack(simpleResultSet.getCallBack());
		super.setCode(simpleResultSet.getCode());
		super.setData(simpleResultSet.getData());
		super.setErrCode(simpleResultSet.getErrCode());
		super.setErrMsg(simpleResultSet.getErrMsg());
		super.setMsg(simpleResultSet.getMsg());
		this.data=simpleResultSet.getData();
		if(data==null) {
			size=(-1);
		}else {
			size=data.size();
		}
	}

	public Set<String>	keySet(){
		if(data==null) {
			data=	super.getData();
		}
		if(data!=null&&data.size()>0) {
			keySet=data.get(0).keySet();
		}else {
			keySet=new HashSet<>();
		}
		return	keySet;
	}
	/**
	 *
	 */
	private static final long serialVersionUID = -3977034270726968189L;
	final public int getInt(int index,String key){
		Object o= getObject(index, key);
		return toInteger(o);
	}

	final public float getFloat(int index,String key){
		Object o= getObject(index, key);
		return toFloat(o);
	}

	final public Date getDate(int index,String key){
		Object o= getObject(index, key);
		return toDate(o);
	}

	final public Boolean getBoolean(int index,String key){
		Object o= getObject(index, key);
		return toBoolean(o);
	}

	public Long getLong(int index,String key){
		Object o= getObject(index, key);
		return toLong(o);
	}

	final public BigDecimal getBigDecimal(int index,String key){
		Object o= getObject(index, key);
		return toBigDecimal(o);
	}

	public BigInteger getBigInteger(int index,String key){
		Object o= getObject(index, key);
		return toBigInteger(o);
	}

	final public Byte getByte(int index,String key){
		Object o= getObject(index, key);
		return toByte(o);
	}

	final public byte[] getBytes(int index,String key){
		Object o= getObject(index, key);
		String jsonString = JSON.toJSONString(o);
		return jsonString.getBytes(StandardCharsets.UTF_8);
	}

	final public Double getDouble(int index,String key){
		Object o= getObject(index, key);
		return toDouble(o);
	}

//    final public java.sql.Date getSqlDate(int index,String key){
//    	 Object o= getObject(index, key);
//         return castToSqlDate(o);
//    }
//
//    final public Timestamp getTimestamp(int index,String key){
//    	 Object o= getObject(index, key);
//         return castToTimestamp(o);
//    }

	final public Short getShort(int index, String key) {
		Object o= getObject(index, key);
		return toShort(o);
	}
	final public String getString(int index, String key) {
		Object o= getObject(index, key);
		if(o==null) {
			return null;
		}
		return o.toString();
	}

	final public Object getObject(int index,String key) {
		if(data==null) {
			data=	super.getData();
		}
		if(data==null) {
			return null;
		}
		if(data.size()<=index) {
			return null;
		}
		return data.get(index).get(key);
	}

	final public Map<String, Object> getRowData(int index){
		if(data==null) {
			data=	super.getData();
		}
		return data.get(index);
	}
	public int size() {
		if(data==null) {
			data=	super.getData();
		}
		if(size==-1&&data!=null) {
			size=data.size();
		}
		return size;
	}

}
