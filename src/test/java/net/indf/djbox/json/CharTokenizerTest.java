package net.indf.djbox.json;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

public class CharTokenizerTest {
	
	@Test
	public void emptyTest() {
		CharTokenizer token = new CharTokenizer("");
		assertThat(token.hasNext(), is(false));
	}
	
	@Test
	public void skipWhiteSpaceTest() {
		CharTokenizer token = new CharTokenizer(" \n \t \r asdf");
		token.skipWhiteSpace();
		assertThat(token.read(), is('a'));
		assertThat(token.next(), is('a'));
		assertThat(token.next(), is('s'));
		assertThat(token.next(), is('d'));
		assertThat(token.read(), is('f'));
		assertThat(token.hasNext(), is(true));
		assertThat(token.next(), is('f'));
		assertThat(token.hasNext(), is(false));
	}
	
	@Test
	public void transactionModeTest() {
		CharTokenizer token = new CharTokenizer("asdf");
		token.next(); //a
		token.transactionMode();
		token.next(); //s
		token.next(); //d
		token.rollback();		
		assertThat(token.next(), is('s'));
	}
	
	@Test (expected=RuntimeException.class)
	public void transactionModeNotStartErrorTest1() {
		CharTokenizer token = new CharTokenizer("asdf");
		token.next(); //a
		//-- none start : token.transactionMode();		
		token.rollback();		
		assertTrue(false);
	}
	
	@Test (expected=RuntimeException.class)
	public void transactionModeNotStartErrorTest2() {
		CharTokenizer token = new CharTokenizer("asdf");
		token.next(); //a
		//-- none start : token.transactionMode();		
		token.commit();		
		assertTrue(false);
	}
	
	@Test (expected=RuntimeException.class)
	public void transactionModeCollisionErrorTest() {
		CharTokenizer token = new CharTokenizer("asdf");		
		token.transactionMode();
		token.transactionMode();		
		assertTrue(false);
	}
	
	@Test
	public void indexTest() {
		CharTokenizer token = new CharTokenizer("asdf");		
		token.next();//index of 1.
		assertThat(token.index(), is(1));		
	}
}
