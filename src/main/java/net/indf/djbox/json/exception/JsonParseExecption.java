package net.indf.djbox.json.exception;

/**
 * 파싱에러 관련 익셉션 클래스이다.
 * @author 정민철 
 * @see 
 */
public class JsonParseExecption extends RuntimeException {
	
	private static final long serialVersionUID = 8139975914884586021L;
	private String msg;
	private static final String defaultMsg = "Parse Error.";
	
	public JsonParseExecption() {
	}

	public JsonParseExecption(String msg) {
		this.msg = msg;
	}
	
   @Override
   public String getMessage() {
	   return msg==null?defaultMsg:msg;
   }

}
