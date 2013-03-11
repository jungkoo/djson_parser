package net.indf.djbox.json;

import java.util.List;
import java.util.Map;
/**
 * JSON을 흉내내기위한 타입을 정의한다.
 * <br/>
 * --------------------------------------------------------------------
 * MAP(OBJECT), ARRAY(LIST), VALUE(STRING, NUMBER, BOOLEAN, MAP, ARRAY)
 * 제약된 기능이므로 이 타입을 벗어나는 Object는 지원하지 않는다.
 * --------------------------------------------------------------------
 * @author 정민철  (http://blog.indf.net , deajang@gmail.com)
 *
 */
public enum DataType {
	BOOLEAN,
	STRING,
	MAP,
	LIST,
	NUMBER,
	VAR,
	UNKNOWN,
	NULL
	;
	
	/**
	 * 데이터 타입을 리턴 
	 * @param var
	 * @return
	 */
	public static DataType isType(Object var) {
		if (var == null)
			return NULL;
		else if (var instanceof Boolean)
			return BOOLEAN;
		else if (var instanceof String)
			return STRING;
		else if (var instanceof Map)
			return MAP;
		else if (var instanceof List)
			return LIST;
		else if (var instanceof Number) 
			return NUMBER;
		else if (var instanceof Var)
			return VAR;
		return UNKNOWN;
	}
}
