package net.indf.djbox.json.exception;

/**
 * 찾아도 존재하지 않는 데이터일  예외처리 클래스 
 * @author 정민철 
 * @see Var
 */
public class NotfoundDataException extends RuntimeException {

	private static final long serialVersionUID = -584957285622159574L;
	
	private String msg;
	private static final String defaultMsg = "Not found Data";
	
	public NotfoundDataException() {
	}

	public NotfoundDataException(String msg) {
		this.msg = msg;
	}
	
   @Override
   public String getMessage() {
	   return msg==null?defaultMsg:msg;
   }

}
