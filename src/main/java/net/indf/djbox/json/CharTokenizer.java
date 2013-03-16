package net.indf.djbox.json;
import java.util.Iterator;

/***
 * 문자열을 캐릭터 단위로 읽기위한 기능을 담당한다.
 * <br/>
 * ------------------------------------------------------------------------
 * String을 문자단위(Character)로 순방향으로 읽는기능을 한다.
 * 주요 기능은, 마지막까지 읽은 인덱스값을 유지하고, 
 * 특정상황의 인덱스로 다시 롤백하는 하여, 문자열 파싱의 조건에 따라 읽기 편하게 함을 목적으로 한다.
 * ------------------------------------------------------------------------
 * @author 정민철  (http://blog.indf.net , deajang@gmail.com)
 *
 */
public class CharTokenizer implements Iterator<Character> {
	private char[] charArray;
	private int length;
	private int currentIndex;	
	private int transactionIndex = -1;
	
	
	
	public CharTokenizer(char[] charArray) {
		init(charArray);
	}
	
	public CharTokenizer(String str) {
		init(str);
	}
	
	public void init(String str) {
		if (str==null)
			throw new NullPointerException();
		
		init(str.toCharArray());
	}
	
	public void init(char[] charArray) {
		if (charArray==null)
			throw new NullPointerException();
				
		this.charArray 	= charArray;
		this.length		= charArray.length;
		this.currentIndex= 0;
		this.transactionIndex = -1;
	}

	/**
	 * 현재의 인덱스를 리턴한다.
	 * @return
	 */
	protected int index() {
		return currentIndex;
	}
	
	/***
	 * 읽어올 데이터가 있는지를 의미한다.
	 * 
	 * @return 
	 */
	public boolean hasNext() {		
		return currentIndex+1<=length;
	}

	/**
	 * 공백문자들은 skip한다.
	 * 만약, 현재 위치에 공백문자가 없을경우 별다른 동작을 하지 않는다.
	 * @return
	 */
	public boolean skipWhiteSpace() {		
		while(hasNext()) {
			if (isWhiteSpace(read())) {
				currentIndex++;
				continue;
			}
			return true;
		}			
			
		return false;		
		
	}

	/**
	 * 읽기만 하고 index는 유지한다.
	 * @return
	 */
	public Character read() {
		return charArray[currentIndex];
	}
	

	public Character next() {
		return charArray[currentIndex++];
	}

	@Deprecated
	public void remove() {
		throw new UnsupportedOperationException();		
	}
	
	
	public boolean isTransactionMode() {
		return transactionIndex!=-1;	//-1:off , x:on
	}

	
	public CharTokenizer transactionMode() {
		if (isTransactionMode()) {
			throw new RuntimeException("Collision transaction Mode.");
		}
		transactionIndex = currentIndex;
		return this;
	}
	
	/**
	 * 트랜잭션 모드를 해제한다.
	 * @return
	 */
	public CharTokenizer commit() {
		if (isTransactionMode()==false)
			throw new RuntimeException("Transaction Mode not started.");
		
		transactionIndex = -1;
		return this;
	}
	
	/**
	 * 트랜잭션 모드 이전의 index로 복구한다.
	 * @return
	 */
	public CharTokenizer rollback() {
		if (isTransactionMode()==false)
			throw new RuntimeException("Transaction Mode not started.");
		
		currentIndex = transactionIndex;
		transactionIndex = -1;
		return this;
	}
	
	/**
	 * 공백문자열인지 체크한다.
	 * 
	 * @param ch
	 * @return
	 */
	public static boolean isWhiteSpace(Character ch) {
		return ch==' ' || ch=='\r' || ch=='\t' || ch=='\n';
	} 
	
	@Override
	public String toString() {		
		return String.format("[index=%d, length=%d, transaction=%b, %s]", index(), length, isTransactionMode() , charArray);
	}
	
	
	/**
	 *  파싱오류 났을때 디버깅을 위해 출력하는 문자열을 의미한다.
	 * @return
	 */
	protected String debugText() {
		StringBuilder sb = new StringBuilder("");
		
		 int startIdx = index()-15;
		 startIdx = startIdx<0?0:startIdx;
		 int endIdx = index()+15;
		 endIdx = endIdx<length?endIdx:length-1;
		 
		 for(int i=startIdx; i<=endIdx; i++) {
			 sb.append(charArray[i]);
		 }
		
		return String.format("[index=%d, length=%d, transaction=%b, debugtext=...%s...]", index(), length, isTransactionMode(), sb);
	}
	
}
