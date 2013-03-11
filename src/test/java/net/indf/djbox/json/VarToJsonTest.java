package net.indf.djbox.json;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
public class VarToJsonTest {

	@Test
	public void booleanTest() {
		assertThat(VarToJson.booleanJson(true), is("true"));
		assertThat(VarToJson.booleanJson(false), is("false"));
	}

	@Test
	public void stringTest() {
		assertThat(VarToJson.stringJson("asdf"), is("\"asdf\""));
		assertThat(VarToJson.stringJson("asd\tf"), is("\"asd\\tf\""));
	}
	
	@Test
	public void numberTest() {
		assertThat(VarToJson.numberJson(123), is("123"));
		assertThat(VarToJson.numberJson(123.655), is("123.655"));
	}
	
	@Test
	public void nilTest() {
		assertThat(VarToJson.nilJson(), is("null"));
	}
	
	@Test
	public void mapTest() {
		Map map = new LinkedHashMap();
		map.put("key1", 11);
		map.put("key2", "str");
		map.put("key3", false);
		map.put("key4", true);
		map.put("key5", null);
		map.put("key6", 123.456);
		
		assertThat(VarToJson.mapJson(map), is("{\"key1\":11, \"key2\":\"str\", \"key3\":false, \"key4\":true, \"key5\":null, \"key6\":123.456}"));
		
	}
	@Test
	public void listTest() {
		List list = new ArrayList();
		list.add("1233");
		list.add("663");
		list.add(null);
		list.add(4);
		assertThat(VarToJson.listJson(list), is("[\"1233\", \"663\", null, 4]") );
	}
	
}
