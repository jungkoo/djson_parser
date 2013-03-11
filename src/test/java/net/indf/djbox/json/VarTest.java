package net.indf.djbox.json;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.indf.djbox.json.DataType;
import net.indf.djbox.json.Var;
import net.indf.djbox.json.exception.NotfoundDataException;

import org.junit.Test;


import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class VarTest {
	
	@Test
	public void findListTest() {
		Var var = new Var(Arrays.asList("aa","bb"));
		assertThat(var.isType() , is(DataType.LIST));
		assertThat(var.size(), is(2));
		assertThat(var.get(0).toString(), is ("aa"));
		assertThat(var.get(1).toString(), is("bb"));
	}
	
	@Test
	public void findTest() {
		Var var = new Var(createFindSampleData());
		
		assertThat(var.isType() , is(DataType.MAP));
		
		assertThat(var.find("api").toString(), is("testapi"));
		assertThat(var.find("total").toInt(), is(10));
		assertThat(var.find("row").toInt(), is(3));
		
		assertThat(var.find("items[0].name").toString(), is("jmc"));
		assertThat(var.find("items[0].age").toInt(), is(32));
		
		assertThat(var.find("items[1].name").toString(), is("ash"));
		assertThat(var.find("items[1].age").toInt(), is(30));
	}
	
	@Test (expected=NotfoundDataException.class)
	public void findIndexErrorTest() {
		Var var = new Var(createFindSampleData());
		
		// array index of Bound... error  (arraysize = 2)
		assertThat(var.find("items[3].name").toString(), is("jmc"));
	}
	
	//
	@Test (expected=NotfoundDataException.class)
	public void findNotError() {
		Var var = new Var(createFindSampleData());
		// no data
		assertThat(var.find("nonono").toInt(), is(30));
	}
	
	
	/*****
	 * 
	 * { 
	 * 	api : testapi,
	 *  total : 10,
	 *  row : 3,
	 *  items : [{name:jmc, age:32}, {name:ash, age:30]
	 * }
	 * 
	 * @return
	 */
	private Map createFindSampleData() {
		Map root = new LinkedHashMap();
		root.put("api", "testapi");
		root.put("total", 10);
		root.put("row", 3);
		
		Map jmc = new HashMap();
		jmc.put("name", "jmc");
		jmc.put("age", 32);

		Map ash = new HashMap();
		ash.put("name", "ash");
		ash.put("age", 30);
		
		root.put("items", Arrays.asList(jmc, ash));
		return root;
	}
	
	
	@Test
	public void stringTypeTest() {
		Var var = new Var("hello string");
		assertThat(var.isType(), is(DataType.STRING));
		assertThat(var.toString(), is("hello string"));
	}
	
	
	@Test
	public void mapTypeTest() {
		Map map = new HashMap();
		map.put("key1", "val1");
		map.put("key2", "val2");
		
		Var var = new Var(map);
		assertThat(var.isType(), is(DataType.MAP) );
		assertThat(var.get("key1").isType(), is(DataType.STRING) );
		assertThat(var.get("key1").toString(), is("val1") );
	}
	
	@Test
	public void listTypeTest() {
		List list = Arrays.asList("aa","bb","cc");
		Var var = new Var(list);
		assertThat(var.isType(), is(DataType.LIST) );
		assertThat(var.get(2).toString(), is("cc"));	
	}
	
	/***
	 * 정수형 
	 */
	@Test
	public void integerTypeTest() {
		Var varInt = new Var(1234);
		assertThat(varInt.isType(), is(DataType.NUMBER) );
		assertThat(varInt.toString(), is("1234") );
		assertThat(varInt.toInt(), is(1234) );
		assertThat(varInt.toLong(), is(1234l) );
		
		Var varLong = new Var(4321l);
		assertThat(varLong.isType(), is(DataType.NUMBER) );
		assertThat(varLong.toString(), is("4321") );
		assertThat(varLong.toInt(), is(4321) );
		assertThat(varLong.toLong(), is(4321l) );	
	}
	
	/***
	 *  소숫점형 
	 */
	@Test
	public void fractionTypeTest() {
		Var varDouble = new Var(1234.987);
		assertThat(varDouble.isType(), is(DataType.NUMBER) );
		assertThat(varDouble.toString(), is("1234.987") );
		assertThat(varDouble.toDouble(), is(1234.987) );

		
		Var varFloat = new Var(4321.987f);
		assertThat(varFloat.isType(), is(DataType.NUMBER) );
		assertThat(varFloat.toString(), is("4321.987") );
		assertThat(varFloat.toDouble(), is(4321.987) );

	}
	
	
}
