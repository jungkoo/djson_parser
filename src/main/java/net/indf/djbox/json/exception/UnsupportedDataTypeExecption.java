package net.indf.djbox.json.exception;

/**
 * 지원되지 않는 타입을 사용했을 경우.
 * @author 정민철 
 * @see Var
 */
public class UnsupportedDataTypeExecption extends RuntimeException {

	private static final long serialVersionUID = 6772490185669757338L;
	private String msg;
	private static final String defaultMsg = "Unsupport data type. (Map, List, String, Boolean, Number, Null, Var";
	
	public UnsupportedDataTypeExecption() {
	}

	public UnsupportedDataTypeExecption(String msg) {
		this.msg = msg;
	}
	
   @Override
   public String getMessage() {
	   return msg==null?defaultMsg:msg;
   }

}
