package net.indf.djbox.json;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.indf.djbox.json.exception.JsonParseExecption;
import net.indf.djbox.json.exception.NotfoundDataException;



/***
 * Json 문자열을  파싱하여  Var 형데이터로 변경해준다.
 * 
 * @author 정민철, 	http://blog.indf.net
 *
 */
public final class JsonParser {
	
	// 투기성 파싱방법임.
	static Var object(CharTokenizer token) {
		token.skipWhiteSpace();
		Var var = null;
		
		if ((var = list(token)) != null) 
			return var;
		if ((var = map(token)) != null)
			return var;
		if ((var = number(token)) != null) 
			return var;
		if ((var = string(token)) != null) 
			return var;				
		if ((var = nill(token)) != null) 
			return var;
		if ((var = bool(token)) != null) 
			return var;
	
		throw new NotfoundDataException("Unknown Data Type. [support: String, Number, Boolean, List, Map, Null ].");
	}
	
	
	/***
	 * map의 key에 해당되는 파싱을 담당한다.
	 * 
	 * 리턴되는 token 의 경우는 :이후에 커서를 둔다(value를 바로 찾을수 있게)
	 * 
	 * @param token
	 * @see map(CharTokenizer token)
	 * @return
	 */
	static String mapKey(CharTokenizer token) {				
		//case1: 따옴표에 감싸진 문자열인경우
		if (token.read() == '"') {
			Var varKey = string(token);
			if (varKey == null) {
				throw new JsonParseExecption("Map's 'KEY' is Empty(). ("+token.debugText()+")");
				
			}
			token.skipWhiteSpace();
			if (token.next()==':') {
				return varKey.toString();
			}
			throw new JsonParseExecption("Map's 'VALUE' Parse Error. Not found ':' characterset. ("+token.debugText()+")");
			
		}
		
		//case2: 따옴표가 없으면 공백전이나 : 전까지
		StringBuilder key = new StringBuilder("");
		while(token.hasNext()) {			
			if (token.read()==':') {
				token.next();
				break;
			}
			if (CharTokenizer.isWhiteSpace(token.read())) {
				if (key.length()>0) {
					token.skipWhiteSpace();
					if(token.next() == ':') {
						break;					
					}else{
						throw new JsonParseExecption("Map's 'VALUE' Parse Error. Not found ':' characterset. ("+token.debugText()+")");
					}
				}
				
			}			
			key.append(token.next());			
		}
		
		if (key.length()>0) {
			return key.toString();
		}
		
		throw new JsonParseExecption("Map's 'KEY' is Empty(). ("+token.debugText()+")");
	}
	
	
	static Var map(CharTokenizer token) {		
		if (token.read() == '{') {
			token.next();
		}else{
			return null;
		}
		
		boolean isEmpty = true;
		Map<String,Object> map = new LinkedHashMap<String,Object>();
		
		while(token.hasNext()) {
			token.skipWhiteSpace();
			if (token.read()=='}') {//close
				token.next();
				return new Var(map);
			}
			
			
			if (isEmpty) {
				map.put(mapKey(token), object(token));
				isEmpty = false;
			}else{
				token.skipWhiteSpace();
				if (token.next() == ',') {
					token.skipWhiteSpace();
					map.put(mapKey(token), object(token));
				}else{
					throw new JsonParseExecption("Map Next Item Character is ',' expected. ("+token.debugText()+")"); // 맵은  {} 사이에 있어야한다.
				}
			}
		}
		
		throw new JsonParseExecption("Map close Character is '}' ("+token.debugText()+")"); // 맵은  {} 사이에 있어야한다.	
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static Var list(CharTokenizer token) {		
		if (token.read() == '[') {
			token.next();
		}else{
			return null;
		}
		
		boolean isEmpty = true;
		List list = new ArrayList();
		while(token.hasNext()) {
			token.skipWhiteSpace();
			if (token.read()==']') {
				token.next();
				return new Var(list);
			}			
			token.skipWhiteSpace();
			
			if (isEmpty) {
				list.add(object(token));
				isEmpty=false;
			}else{
				token.skipWhiteSpace();		
				if (token.next() != ',') {
					throw new JsonParseExecption("Array next Item Character is ',' ("+token.debugText()+")"); //  아이템 사이에는 ,  가 있어야 한다. 
				}
				token.skipWhiteSpace();
				list.add(object(token));				
			}
		}	
		
		throw new JsonParseExecption("Array close Character is ']' ("+token.debugText()+")"); // 배열은 [] 사이에 있어야한다.		
	}
	
	/**
	 * 문자열 데이터를 파싱한다. 
	 * 문자열은 " " 사이의 값을 의미한다. 이스케입처리는 ( \n \r \t \\ \" )
	 * @param token
	 * @return
	 */
	static Var string(CharTokenizer token) {
		token.transactionMode();
		
		if (token.next() != '"') {
			token.rollback();
			return null;
		}
		
		final StringBuilder sb = new StringBuilder("");		
		while(token.hasNext()) {
			if (token.read() == '"') { //문자열의 끝
				token.next();
				token.commit();
				return new Var(sb.toString());
			}
			if (token.read() == '\\') { //이스케입문자라면...				
				sb.append(escape(token));
				continue;
			}
			sb.append(token.next());
		}
		
		throw new JsonParseExecption("String close Character is '\"' ("+token.debugText()+")"); //  문자열은 " 로 닫혀야한다.	
	}
	
	/**
	 * 숫자형 데이터를 파싱한다.
	 * <br/>
	 * 
	 * 마지막 token 위치에서 숫자형 데이터인 부분을 찾는다.
	 * 이때, token의 커서는 자동으로 이동된다.
	 * 데이터가 없을경우 null을 리턴한다.
	 * 
	 * @param token
	 * @return
	 */
	static Var number(CharTokenizer token) {			
		Long temp = null;		
		if ( (temp=integer(token)) == null ){
			return null;			
		}
		
		//정수임
		if (token.hasNext()==false || ismoreToken(token.read()))
			return new Var(temp);
				
		StringBuilder num = new StringBuilder().append(temp);
		//소숫점형임
		if (token.next()=='.') {
			if (isNumber(token.read())==false) {
				throw new JsonParseExecption("Decimal-Point Next is not numberic.("+token.debugText()+")");
			}
			
			if ((temp = integer(token)) != null) {
				num.append(".").append(temp);
			}else{
				throw new JsonParseExecption("Decimal-Point next Not found numberic.("+token.debugText()+")");
			}
		}
		if (token.hasNext()==false|| ismoreToken(token.read()))
			return new Var(Double.parseDouble(num.toString()));

		//지수형표현
		if (token.read()=='e' || token.read()=='E') {
			token.next();
			if ((temp=integer(token)) != null) {
				return new Var(Double.parseDouble(num.append("e").append(temp).toString()));				
			}
		}
		
		throw new JsonParseExecption("Unknown Number Parse Error. ("+token.debugText()+")");		
	}
	
	
	/**
	 * 숫자형을 리턴한다.
	 * @param token
	 * @see number(CharTokenizer token)
	 * @return
	 */
	private static Long integer(CharTokenizer token) {
		token.transactionMode();				
		final StringBuffer number = new StringBuffer("");		
				
		//기호체크
		switch(token.read()) {
			case '+':
				token.next();//skip
				break;
			case '-':
				number.append(token.next());
				break;
		}
		
		while(token.hasNext() && isNumber(token.read())) {
			number.append(token.next());
		}
		
		//숫자아님
		if(number.length()<=0) {			
				token.rollback();
				return null;			
		}else{
			token.commit();
			return Long.parseLong(number.toString());
		}
	}
	
	
	/**
	 * null 객체인지 파싱한다. VAR.NIL 객체를 생성한다.
	 *  
	 * 실패하면 null
	 * 
	 * @param token
	 * @return
	 */
	static Var nill(CharTokenizer token) {		
		if (startWith(token, 'n','u','l','l')) {
			return Var.NIL;
		}
		return null;						
	}
	
	/**
	 * boolean 타입인지 확인후 VAR를 생성한다.
	 * 
	 * 실패하면 null
	 * @param token
	 * @return
	 */
	static Var bool(CharTokenizer token) {		
		if (startWith(token, 't','r','u','e')) {		
			return new Var(true);
		}
		if (startWith(token, 'f','a','l','s','e')) {		
			return new Var(false);
		}
		
		return null;	
	}
	
	/**
	 * token에서 순서대로 일치하는치 체크한다.
	 * @param token
	 * @param chars
	 * @return
	 */
	static boolean startWith(CharTokenizer token, Character ...chars) {
		token.transactionMode();		
		for(Character ch : chars) {
			if (token.next() != ch) {
				token.rollback();
				return false;
			}
		}
		token.commit();
		return true;

	}
	

	/**
	 * 숫자인지 체크한다.
	 * @param ch
	 * @return
	 */
	private static boolean isNumber(Character ch) {
		return ch=='0' || ch=='1' || ch=='2' || ch=='3' || ch=='4' || ch=='5' || ch=='6' || ch=='7' || ch=='8' || ch=='9' ;
	}
	
	/**
	 * 유니코드 표현에 대한 이스케입처리를 하며, token의 index관리도 한다.
	 * @param token
	 * @return
	 */
	private static Character escape(CharTokenizer token) {
		if (token.hasNext()==false || token.read() != '\\')
			return null;
		else {
			token.next();
			if (token.hasNext()==false)
				throw new JsonParseExecption("escape next token is empty.("+token.debugText()+")"); // 이스케입하고 뒤에 글자가 없다.
		}
		
		
		// default escape	
		Character ch = escapeChar(token.read());		
		if (ch!=null) {
			token.next();
			return ch;
		}
		
		if (token.next()!='u') {
			throw new JsonParseExecption("escape parse error. [expected= \\t, \\f, \\n, \\t, \\r, \\\\, \\\", \\u0000 ~ \\uFFFF] ("+token.debugText()+")"); //  escape이 잘못되었다.
		}
		
		// unicode escape		//(char)Integer.parseInt("0027", 16) // \\u0027
		StringBuilder sb = new StringBuilder("");
		for(int i=0; i<4; i++) {
			if (token.hasNext())
				sb.append(token.next());
			else
				throw new JsonParseExecption("unicode escape short length. (expected: 4digit number. \\u"+sb+", length="+ sb.length() +"/4)] ("+token.debugText()+")"); //  u형태의 유니코드표현의 숫자가 짧다.
		}
		
		try{
			return (char)Integer.parseInt(sb.toString(), 16);
		}catch(Exception e) {	// 16진수변환 실패.				
			throw new JsonParseExecption("unicode escape is not Number. ("+token.debugText()+")"); //  escape 숫자형값이 아님.			
		}
	}
	
	private static Character escapeChar(Character ch) {
		switch (ch) {//escape
			case '\\':
				return '\\';	
			case '/':
				return '/';
			case 'f':
				return (char)0x66;
			case 'n':
				return '\n';				
			case 't':
				return '\t';
			case 'r':
				return '\r';		
			case '"':
				return '"';					
		}
		return null;
	}
	
	// more item success character-set
	private static boolean ismoreToken(Character ch) {
		final List<Character> moreToken = Arrays.asList(']','}','\t','\r','\n',' ',',');
		return moreToken.contains(ch);
		
	}
}
