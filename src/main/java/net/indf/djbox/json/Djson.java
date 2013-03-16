package net.indf.djbox.json;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;

import net.indf.djbox.json.exception.JsonParseExecption;
/******
 * JSON에 관련된 전반적인 업무를 담당한다.
 * 
 * @author 정민철   http://blog.indf.net
 *
 */
public class Djson {

	/**
	 * Var 객체를  Object 로 변경한다.
	 * @param var
	 * @return
	 */
	public static String toJson(Var var) {
		if (var == null)
			return null;
		
		return VarToJson.object(var.toObject());
	}
	

	/***
	 * url 의 결과를 읽어서 파싱한다.
	 * @param url
	 * @param charset :  "UTF-8"
	 * @return
	 * @throws IOException
	 */
	public static Var parse(URL url, String charset) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), charset));
		int c;
		CharArrayWriter cw = new CharArrayWriter();
		
		while ((c = in.read()) != -1)  {
			 cw.append((char)c);   
		}
		in.close();
		if (cw.size()<=0)
			return null;
		final CharTokenizer token = new CharTokenizer(cw.toCharArray());
		Var var = JsonParser.object(token);
		checkGarbage(token);		
		return var;
	}
	
	/***
	 * url 의 결과를 읽어서 파싱한다. 
	 * 단,  UTF8을 기본으로 한다.
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static Var parse(URL url) throws IOException {	 
		return parse(url, "UTF-8");
	 }
	
	
	/****
	 * JSON 문자열을  Var 객체로 변환한다.
	 * 
	 * @param json
	 * @return
	 */
	public static Var parse(String json) {
		final CharTokenizer token = new CharTokenizer(json);
		Var var = JsonParser.object(token);
		checkGarbage(token);
		return var;
	}
	
	/**
	 * 파일을 읽어서 파싱한다.
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static Var parse(File file) throws IOException {
		 return parse(file, "UTF-8");
	}

	public static Var parse(File file, String charset) throws IOException {
		if (file==null || file.exists()==false){
			throw new FileNotFoundException();
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),charset));
		int c=-1;
		CharArrayWriter cw = new CharArrayWriter();

		//BOM skip		
		if ((c = br.read()) != -1 && !Arrays.asList(0xEFBBBF,0xFEFF,0xFFFE,0x00FEFF,0xfffe00).contains(c)) {
			cw.append((char)c);
		}
	
		
		while( (c = br.read()) != -1) {			
			cw.append((char)c);
	    }
		br.close();
		if (cw.size()<=0)
			return null;
		
		final CharTokenizer token = new CharTokenizer(cw.toCharArray());
		Var var = JsonParser.object(token);
		checkGarbage(token);		
		return var;
	}
	
	/**
	 * 파싱이 완료되었는데, 그 뒤에 불필요한 글자가 있는지 체크한다.
	 * @param token
	 */
	private static void checkGarbage(final CharTokenizer token) {
		token.skipWhiteSpace();
		if (token.hasNext()) {
			throw new JsonParseExecption("Garbage data found !!!");
		}
	}
}
