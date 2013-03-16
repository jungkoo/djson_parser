package net.indf.djbox.json;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import net.indf.djbox.json.exception.JsonParseExecption;
import net.indf.djbox.json.exception.NotfoundDataException;

import org.junit.Test;
public class JsonParserTest {
	
	@Test
	public void crazyTestMode() {
		String json = "{ tistory : " +
				"				{ " +
				"					  \"total\":123" +
				"					, date :\"2012.12.23\"" +
				"					, items:[" +
				"							{name :\"정민철\", age:32, isMen:true}" +
				"							,{name :\"안성현\", age:30, isMen:true}" +
				"							,{name :\"순\\t희\", age: 28, isMen:false}" +
				"							] " +
				"				}" +
				"		}";
		Var var = JsonParser.object(new CharTokenizer(json));
		
		assertThat(var.find("tistory").isType() 		,  is(DataType.MAP));
		assertThat(var.find("tistory").size() 			,  is(3));
		assertThat(var.find("tistory.total").toInt() 	,  is(123));
		assertThat(var.find("tistory.date").toString() 	,  is("2012.12.23"));
		assertThat(var.find("tistory.items").isType() 	,  is(DataType.LIST));
		assertThat(var.find("tistory.items[0]").isType() 	,  is(DataType.MAP));
		assertThat(var.find("tistory.items[1]").isType() 	,  is(DataType.MAP));
		assertThat(var.find("tistory.items[2]").isType() 	,  is(DataType.MAP));
		assertThat(var.find("tistory.items[0].name").toString() 	,  is("정민철"));
		assertThat(var.find("tistory.items[0].age").toInt() 		,  is(32));
		assertThat(var.find("tistory.items[0].isMen").toBoolean() 	,  is(true));
		assertThat(var.find("tistory.items[1].name").toString() 	,  is("안성현"));
		assertThat(var.find("tistory.items[1].age").toInt() 		,  is(30));
		assertThat(var.find("tistory.items[1].isMen").toBoolean() 	,  is(true));
		assertThat(var.find("tistory.items[2].name").toString() 	,  is("순\t희"));
		assertThat(var.find("tistory.items[2].age").toInt() 		,  is(28));
		assertThat(var.find("tistory.items[2].isMen").toBoolean() 	,  is(false));
		
	}
	
	
	@Test (expected=JsonParseExecption.class)//key is not : 
	public void mapKeyFindError() {
		Var var = JsonParser.object(new CharTokenizer("[{\"Product\":\"HDD\", \"Maker\":\"Western Digital\", \"Price\"156000}]"));
		assertThat(var.size(), is(1));
		assertThat(var.isType(), is(DataType.LIST));
		assertThat(var.get(0).isType(), is(DataType.MAP));
		assertThat(var.get(0).get("Product").toString(), is ("HDD"));
	}
	
	@Test (expected=JsonParseExecption.class)
	public void mapCommaErrorTest() {
		JsonParser.object(new CharTokenizer("{total: 3, msg:\"정상\"items:[]}"));
	}
	
	@Test (expected=JsonParseExecption.class)
	public void listCommaErrorTest() {
		JsonParser.object(new CharTokenizer("{3, 1123 d}"));
	}
	

	@Test (expected=NotfoundDataException.class)
	public void listCommaMoreErrorTest() {
		JsonParser.object(new CharTokenizer("[1,,22]"));
	}
	
	@Test (expected=NotfoundDataException.class)
	public void mapCommaMoreErrorTest() {
		JsonParser.object(new CharTokenizer("[k:1,]"));
	}
	
	
	@Test
	public void objectTest() {
		Var var = null;

		// int
		var = JsonParser.object(new CharTokenizer("123"));
		assertThat(var.isType(), is(DataType.NUMBER));
		assertThat(var.toInt(), is(123));		
		
		// double
		var = JsonParser.object(new CharTokenizer("123.456"));
		assertThat(var.isType(), is(DataType.NUMBER));
		assertThat(var.toDouble(), is(123.456));
		
		// string
		var = JsonParser.object(new CharTokenizer("\"hihi\""));
		assertThat(var.isType(), is(DataType.STRING));
		assertThat(var.toString(), is("hihi"));
		
		// null
		var = JsonParser.object(new CharTokenizer("null"));
		assertThat(var.isType(), is(DataType.NULL));
		assertThat(var , is(Var.NIL));
		
		// boolean true
		var = JsonParser.object(new CharTokenizer("true"));
		assertThat(var.isType(), is(DataType.BOOLEAN));
		assertThat(var.toBoolean() , is(true));
		
		// boolean false
		var = JsonParser.object(new CharTokenizer("false"));
		assertThat(var.isType(), is(DataType.BOOLEAN));
		assertThat(var.toBoolean() , is(false));
		
		// list
		var = JsonParser.object(new CharTokenizer("[1,3 ]"));
		assertThat(var.isType(), is(DataType.LIST));
		assertThat(var.size() , is(2));
		assertThat(var.get(0).toInt() , is(1));
		assertThat(var.get(1).toInt() , is(3));		
		
		// map
		var = JsonParser.object(new CharTokenizer("{key:11, key2:22, key3 : 55}"));
		assertThat(var.isType(), is(DataType.MAP));
		assertThat(var.size() , is(3));
		assertThat(var.get("key").toInt() , is(11));
		assertThat(var.get("key2").toInt() , is(22));
		assertThat(var.get("key3").toInt() , is(55));
		
	}
	
	@Test 
	public void mapTest() {
		CharTokenizer token = null;
		Var var = null;
		
		token = new CharTokenizer("{key1:123}");		
		var = JsonParser.map(token);
		assertThat(var.isType(), is(DataType.MAP));
		assertThat(var.size(), is(1));
		assertThat(var.get("key1").toInt(), is(123));
		
		token = new CharTokenizer("{key1:123 , key2  : 567, \"key3\":89,\"key4\" :101}");		
		var = JsonParser.map(token);
		assertThat(var.isType(), is(DataType.MAP));
		assertThat(var.size(), is(4));
		assertThat(var.get("key1").toInt(), is(123));
		assertThat(var.get("key2").toInt(), is(567));
		assertThat(var.get("key3").toInt(), is(89));
		assertThat(var.get("key4").toInt(), is(101));
		
		
	}
	
	@Test
	public void mapKeyTest() {
		CharTokenizer token = null;
		String key = null;
		
		// none "" -- :
		token = new CharTokenizer("key1:value");		
		key = JsonParser.mapKey(token);
		assertThat(key, is("key1"));
		assertThat(token.read(), is('v'));
		
		// none "" -- space
		token = new CharTokenizer("key2 :value");
		key = JsonParser.mapKey(token);
		assertThat(key, is("key2"));
		assertThat(token.read(), is('v'));
				
		// ""
		token = new CharTokenizer("\"key3\":value");
		key = JsonParser.mapKey(token);
		assertThat(key, is("key3"));
		assertThat(token.read(), is('v'));
		
	
		
	}
	
	@Test
	public void simpleListTest() {
		String str = "[1,\"a\",null,false,true,1.1]"; 		
		CharTokenizer token = new CharTokenizer(str);
		Var var = JsonParser.list(token);
		
		assertThat(var.isType(), is(DataType.LIST));
		assertThat(var.size(), is(6));
		assertThat(var.toList().size(), is(6));
		
		assertThat(var.get(0).toString(), is("1"));
		assertThat(var.get(1).toString(), is("a"));
		assertThat(var.get(2).toString(), is("null"));
		assertThat(var.get(3).toString(), is("false"));
		assertThat(var.get(4).toString(), is("true"));
		assertThat(var.get(5).toString(), is("1.1"));
	}
	
	@Test
	public void escapeLastStringtest() {
		String str = "\"들은 \\\"아이돌 특집 은근히 재미있다\\\"\"";
		CharTokenizer token = new CharTokenizer(str);
		Var var = JsonParser.string(token);
		assertThat(var.isType(), is(DataType.STRING));
		assertThat(var.toString(), is("들은 \"아이돌 특집 은근히 재미있다\""));		
		assertThat(token.hasNext(), is(false));
	}
	
	@Test
	public void escapeUnicodeStringTest() {
		String str = "\"ash84\\u0027sinbal blog'\"";
		CharTokenizer token = new CharTokenizer(str);
		Var var = JsonParser.string(token);
		assertThat(var.isType(), is(DataType.STRING));
		assertThat(var.toString(), is("ash84'sinbal blog'"));	
		assertThat(token.hasNext(), is(false));
	}
	
	
	
	@Test
	public void escapeOffsetStringTest() {
		String str = "\"hi~\\tmy\\nname\\\"12 \","; 		
		CharTokenizer token = new CharTokenizer(str);
		Var var = JsonParser.string(token);
		assertThat(var.isType(), is(DataType.STRING));
		assertThat(var.toString(), is("hi~\tmy\nname\"12 "));		
		assertThat(token.next(), is(','));
	}
	
	@Test
	public void escapeStringTest() {
		String str = "\"hi~\\tmy\\nname\\\"12 \""; 		// json_string = "hi~\tmy\nname\"12 "
		CharTokenizer token = new CharTokenizer(str);
		Var var = JsonParser.string(token);
		assertThat(var.isType(), is(DataType.STRING));
		assertThat(var.toString(), is("hi~\tmy\nname\"12 "));		
		assertThat(token.hasNext(), is(false));
	}
	
	@Test
	public void simpleStringTest() {
		CharTokenizer token = new CharTokenizer("\"hi~\",123");
		Var var = JsonParser.string(token);		
		assertThat(var.isType(), is(DataType.STRING));
		assertThat(var.toString(), is("hi~"));
		assertThat(token.read(), is(','));
	}
	
	@Test
	public void booleanOfNULLTest() {
		CharTokenizer token = new CharTokenizer("null,");
		
		Var var = JsonParser.nill(token);
		assertThat(var.isType(), is(DataType.NULL));
		assertThat(var, is(Var.NIL));
		assertThat(token.read(), is(','));
	}
	
	
	@Test
	public void booleanOfFalseTest() {
		CharTokenizer token = new CharTokenizer("false,");
		
		Var var = JsonParser.bool(token);
		assertThat(var.isType(), is(DataType.BOOLEAN));
		assertThat(var.toBoolean(), is(false));
		assertThat(token.read(), is(','));
	}
	
	@Test
	public void booleanOfTrueTest() {
		CharTokenizer token = new CharTokenizer("true,");
		
		Var var = JsonParser.bool(token);
		assertThat(var.isType(), is(DataType.BOOLEAN));
		assertThat(var.toBoolean(), is(true));
		assertThat(token.read(), is(','));
	}
	
	@Test
	public void numberOfDoubleTest() {
		CharTokenizer token = new CharTokenizer("1234.56,789");
		
		Var var = JsonParser.number(token);
		assertThat(var.isType(), is(DataType.NUMBER));
		assertThat(var.toDouble(), is(1234.56));
		assertThat(var.toString(), is("1234.56"));
		assertThat(token.read(), is(','));		
	}
	
	
	@Test
	public void numberOfIntegerTest() {
		CharTokenizer token = new CharTokenizer("123456,789");
		
		Var var = JsonParser.number(token);
		assertThat(var.isType(), is(DataType.NUMBER));
		assertThat(var.toInt(), is(123456));
		assertThat(var.toString(), is("123456"));
		assertThat(token.read(), is(','));		
	}
	
	@Test
	public void numberOfMinusIntegerTest() {
		CharTokenizer token = new CharTokenizer("-123");
		Var var = JsonParser.number(token);
		assertThat(var.isType(), is(DataType.NUMBER));
		assertThat(var.toInt(), is(-123));
	}
	
	@Test
	public void numberOfPlusIntegerTest() {
		CharTokenizer token = new CharTokenizer("+423");
		Var var = JsonParser.number(token);
		assertThat(var.isType(), is(DataType.NUMBER));
		assertThat(var.toInt(), is(423));
	}
	
	@Test(expected=JsonParseExecption.class)
	public void numberErrorTest() {
		CharTokenizer token = new CharTokenizer("123a1");
		JsonParser.number(token);
		// parse error
	}
	
	//지수형 숫자 테스트
	@Test
	public void exponentialNumberTest() {
		CharTokenizer token = new CharTokenizer("1.12e2");
		Var var = JsonParser.number(token);
		assertThat(var.isType(), is(DataType.NUMBER));
		assertThat(var.toDouble(), is(1.12e2));		
	}
	
	@Test
	public void numberOfMinusDoubleTest() {
		CharTokenizer token = new CharTokenizer("-123.567");
		Var var = JsonParser.number(token);
		assertThat(var.isType(), is(DataType.NUMBER));
		assertThat(var.toDouble(), is(-123.567));
	}
	
	@Test
	public void startWithTest() {
		CharTokenizer token = new CharTokenizer(" 123,null,test");
		token.skipWhiteSpace();
		assertThat(JsonParser.startWith(token, '1','2','3'), is(true));		
		assertThat(token.read(), is(','));
		assertThat(token.index(), is(4));
		assertThat(JsonParser.startWith(token, 'n','u','l','l'), is(false)); //rollback
		assertThat(token.read(), is(','));
		assertThat(token.index(), is(4));
	}

}
