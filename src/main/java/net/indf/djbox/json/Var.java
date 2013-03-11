package net.indf.djbox.json;

import java.util.List;
import java.util.Map;

import net.indf.djbox.json.exception.NotfoundDataException;
import net.indf.djbox.json.exception.UnsupportedDataTypeExecption;
import static net.indf.djbox.json.DataType.*;

/**
 * javascript 의  var 타입을 흉내낸 클래스이다. <br/>
 * 
 * @author 정민철 (http://blog.indf.net , deajang@gmail.com)
 *
 */
public class Var {
	private Object var;
	private DataType type = UNKNOWN;
	public static final Var NIL = new Var(null);
	
	
	public Var() {
	}
	
	public Var(Object var) {
		if (var instanceof Var) {
			set(((Var)var).toObject());
		}else{
			set(var);
		}
	}
	
	private Var set(Object var) {
		this.var  = var;
		type = DataType.isType(var);
		
		if (type == UNKNOWN)
			throw new UnsupportedDataTypeExecption();
		
		return this;
	}
	
	
	@SuppressWarnings("rawtypes")
	public Var get(int index) {
		if (type != LIST)
			throw new UnsupportedDataTypeExecption(String.format("get(%d) is supported type is 'LIST' (but=%s)", index, type.toString()));
		
		final List list = toList();
		if (list.size() <= index) 
			throw new NotfoundDataException("Index Out of Bounds. Index:"+ index+", Size:" + list.size());
		
		Object obj = list.get(index);
		if (obj==null)
			throw new NotfoundDataException();
		
		return new Var(obj);		
	}
	
	public Var get(String key) {
		if (type != MAP)
			throw new UnsupportedDataTypeExecption(String.format("get(\"%s\") is supported type is 'MAP' (but=%s)", key, type.toString()));
		
		Object obj = toMap().get(key);
		if (obj==null)
			throw new NotfoundDataException("Not found. MAP["+key+"]");
		
		return new Var(obj);
	}
	
	/***
	 * 문자열로 데이터 접근을 하고 싶을때 사용한다.  <br/>
	 * 형식은 javascript에서의 접근법과 동일하다.
	 * 
	 * <code>
	 * {tistory : {items : [11,22,33,44]}} <br/>
	 * <br/>
	 * this.find("tistory.item[0]"); // output : 11<br/>
	 * this.find("tistory.item[1]"); // output : 22<br/>
	 * this.find("tistory.item[2]"); // output : 33<br/>
	 * this.find("tistory.item[2]"); // output : 44<br/>
	 * </code>
	 * @param path
	 * @return
	 */
	public Var find(String path) {
		if (path==null)
			return this;
		
		StringBuilder token = new StringBuilder("");
		char[] chArr = path.toCharArray();
		final int loopSize = chArr.length;
		Var cusor = this;
		for(int i=0; i<=loopSize; i++) {
			//is Map
			if (i>=loopSize || chArr[i] == '.') {
				//-- find map token --
				cusor = cusor.get(token.toString());
				//System.out.println(token);
				//--------------------				
				token.setLength(0);
				continue;
			}
			//is List
			else if (i>=loopSize || chArr[i] == '[') {
				
				//find token
				//System.out.println(token);
				//find index
				StringBuilder index = new StringBuilder("");
				for(i=i+1;i<loopSize;i++) {
					if (chArr[i] == ']') {
						//-- find list token -----
						if (token.length()<=0 && cusor.isType() == DataType.LIST) {
							cusor = cusor.get(Integer.parseInt(index.toString()));
						}else{
							cusor = cusor.get(token.toString()).get(Integer.parseInt(index.toString()));
						}
						
						//------------------------
						i++;
						break;
					}
					index.append(chArr[i]);
				}
				token.setLength(0);
				continue;
			}
			token.append(chArr[i]);
			
		}
		return cusor;
	}
	
	@SuppressWarnings("rawtypes")
	public Map toMap() {	
		if (var instanceof Map) {
			return Map.class.cast(var);
		}
		throw new UnsupportedOperationException(String.format("'%s' type is Unsupported 'toMap()'", this.type) );
	}
	
	
	@SuppressWarnings("rawtypes")
	public List toList() {
		if (var instanceof List) {
			return List.class.cast(var);
		}
		throw new UnsupportedOperationException(String.format("'%s' type is Unsupported 'toList()'", this.type) );
	}
	
	public int toInt() {
		if (var instanceof Integer) {
			 return Integer.class.cast(var);
		}else if (var instanceof Long) {
			return Integer.parseInt(String.valueOf(var));
		}else if (var instanceof String) {
			return Integer.parseInt(String.class.cast(var));
		}
		throw new UnsupportedOperationException(String.format("'%s' type is Unsupported 'toInt()' , value=%s", this.type, var) );		 
	}
	
	public long toLong() {
		if (var instanceof Integer) {
			 return Integer.class.cast(var);
		}else if (var instanceof Long) {
			return Long.class.cast(var);
		}else if (var instanceof String) {
			return Integer.parseInt(String.class.cast(var));
		}
		throw new UnsupportedOperationException(String.format("'%s' type Unsupported 'toInt()'", this.type) );
	}
	
	public double toDouble() {
		if (var instanceof Double) {
			return Double.class.cast(var);
		}else if (var instanceof Float) {
			//Float 타입을  cast해서  Double 로 넘기면 소숫점 오차가 생긴다.
			return Double.parseDouble(this.toString());
		}else if (var instanceof String) {
			return Double.parseDouble(this.toString());
		}
		throw new UnsupportedOperationException(String.format("'%s' type is Unsupported 'toDouble()'", this.type) ); 
	}
	
	
	@Override
	public String toString() {
		if (this.type == NULL)
			return "null".intern();
		
		return var.toString();
	}
	
	public boolean toBoolean() {
		if (type == DataType.BOOLEAN) {
			return Boolean.class.cast(var);
		}else if (type == DataType.STRING) {
			return Boolean.parseBoolean(this.toString());
		}
		throw new UnsupportedOperationException(String.format("'%s' type is Unsupported 'toBoolean()'", this.type) );
	}
	
	public Object toObject() {
		return var;
	}
	
	public DataType isType() {
		return type;
	}
	
	/**
	 * MAP, LIST 일 경우에 크기를 리턴한다.
	 * 타입이 지원되지 않을경우  Exception 을 발생한다.
	 * @return
	 */
	public int size() {
		if (type==DataType.LIST)
			return toList().size();
		if (type==DataType.MAP)
			return toMap().size();
		
		throw new UnsupportedOperationException(String.format("'%s' type is unsupported 'size()' (supported Type is 'MAP' or 'LIST')", type)); 
	}
	
	/**
	 *  문자열의 길이를 리턴한다.
	 *  타입이 지원되지 않을경우  Exception 을 발생한다.
	 * @return
	 */
	public int length() {
		if (type==DataType.STRING)
			return toString().length();
		
		throw new UnsupportedOperationException(String.format("'%s' type is unsupported 'length()' (supported Type is 'STRING')", type));
	}
}
