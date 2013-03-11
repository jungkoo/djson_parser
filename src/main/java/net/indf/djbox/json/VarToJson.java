package net.indf.djbox.json;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/***
 * Var 클래스를  JSON  문자열로 바꾸는 업무를 담당한다.
 * 
 * 사용자는 직접 호출할 일이 없는것으로 가 
 * @author 정민철  (http://blog.indf.net , deajang@gmail.com)
 * @see Djson
 */
public final class VarToJson {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static String object(Object obj) {
		if (obj==null)
			return nilJson();
		if (obj instanceof List)
			return listJson((List)obj);
		if (obj instanceof Number)
			return numberJson((Number)obj);
		if (obj instanceof Map)
			return mapJson((Map)obj);
		if (obj instanceof String)
			return stringJson((String)obj);
		if (obj instanceof Boolean)
			return booleanJson((Boolean)obj);
		if (obj instanceof Var) 
			return object(((Var) obj).toObject()); //Var 타입이면 기존 객체로 풀어야 한다.
		
		return null;
	}
	
	
	static String mapJson(Map<String,Object> map) {
		StringBuilder sb = new StringBuilder("{");
		
		boolean isFirst = true;
		for(Entry<String,Object> entry : map.entrySet()) {
			if(!isFirst) {
				sb.append(", ");
			}
			
			sb.append(stringJson(entry.getKey()));
			sb.append(":");
			sb.append(object(entry.getValue()));
			isFirst = false;
		}		
		return sb.append("}").toString();
	}
	
	@SuppressWarnings("rawtypes")
	static String listJson(List list) {
 		StringBuilder sb = new StringBuilder("[");
		
		boolean isFirst = true;
		for(Object obj : list) {
			if(!isFirst) {
				sb.append(", ");
			}
			
			sb.append(object(obj));
			
			isFirst = false;
		}		
		return sb.append("]").toString();
	}
 	
	static  String booleanJson(boolean bool) {
		return bool?"true":"false";
	}
	
	static String nilJson() {
		return "null";
	}
	
	static String numberJson(Number num) {
		return String.valueOf(num);
	}
	
	static String stringJson(String str) {
		if (str==null)
			return null;
		
		StringBuilder sb =  new StringBuilder("\"");
		char[] charArr = str.toCharArray();
		for(char ch : charArr) {
			switch(ch) {
				case '\t':
					sb.append("\\t");
					break;
				case '\r':
					sb.append("\\r");
					break;
				case '\n':
					sb.append("\\n");
					break;
				case '"':
					sb.append("\\\"");
					break;
				case '\\':
					sb.append("\\\\");
					break;
				default:
					sb.append(ch);				
			}
		}
		
		sb.append("\"");
		return sb.toString();
	}
}
