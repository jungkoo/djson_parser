## Welcome to "DJson"##
**DJson** is a very-simple JSON Parser. (for JAVA)
[http://blog.indf.net](http://blog.indf.net)

### 1. Preview ###
primitive type friendlily. (ex. String, Double, Integer, Boolean, Map, List)



### 2. Basic Code ###
####a) Number :####

* source:

      Var integer = Djson.parse("123");
    	System.out.println("TYPE : " + integer.isType());
    	System.out.println("VALUE: " + integer.toInt());

    	System.out.println();		

    	Var dotNum = Djson.parse("456.78");
    	System.out.println("TYPE : " + dotNum.isType());
    	System.out.println("VALUE: " + dotNum.toDouble());
    
* result:

    	TYPE : NUMBER
    	VALUE: 123
     
    	TYPE : NUMBER
    	VALUE: 456.78


####b) String, Boolean, Null####

* source:

    	Var string = Djson.parse("\"Hello!\"");
    	System.out.println("TYPE : " + string.isType());
    	System.out.println("VALUE: " + string.toString());
    		
   		Var bool = Djson.parse("false");
    	System.out.println("TYPE : " + bool.isType());
    	System.out.println("VALUE: " + bool.toBoolean());
    		
    	Var nil = Djson.parse("null");
    	System.out.println("TYPE : " + nil.isType());
    	System.out.println("TYPE : " + nil.toString());

* result:

		TYPE : STRING
    	VALUE: Hello!

    	TYPE : BOOLEAN
    	VALUE: false

    	TYPE : NULL
    	TYPE : null


####c) MAP, LIST####

* source:
		
		Var list = Djson.parse("[10,11,13]");
		System.out.println("TYPE: " + list.isType());
		System.out.println("SIZE: " + list.size());
		System.out.println("get(0)-TYPE : " + list.get(0).isType());
		System.out.println("get(0)-VALUE: " + list.get(0).toInt());
		
		Var map = Djson.parse("{name:\"chul\", age:12}");
		System.out.println("TYPE: " + map.isType());
		System.out.println("SIZE: " + map.size());
		System.out.println("get(name)-TYPE  : " + map.get("name").isType());
		System.out.println("get(name)-VALUE : " + map.get("name"));
		
* result:
    
		TYPE: LIST
	    SIZE: 3
	    get(0)-TYPE : NUMBER
	    get(0)-VALUE: 10

	    TYPE: MAP
	    SIZE: 2
	    get(name)-TYPE  : STRING
	    get(name)-VALUE : chul




### 3. Sample Code ###
####a) hard case (basic) ####

* source:

		Var var = Djson.parse("{indf : { total: 10, row:2 , items : [{name: \"chul\", age: 10} ,{name :\"ahn\", age:8}] }}");
		
		System.out.println("total :  " + var.get("indf").get("total"));
		System.out.println("row   :  " + var.get("indf").get("row"));
		System.out.println("items :  " + var.get("indf").get("items").size());
		
		System.out.println("--- items ---");
		for (int i=0; i<var.get("indf").get("items").size(); i++) {
			Var item = var.get("indf").get("items").get(i);
			System.out.println(i+") name: " + item.get("name") + ", age: " + item.get("age"));
		}

* result:
		    
	    total :  10
	    row   :  2
	    items :  2

	    --- items ---
	    0) name: chul, age: 10
	    1) name: ahn, age: 8


####b) hard case ... find() ####

* source:

		Var var = Djson.parse("{indf : { total: 10, row:2 , items : [{name: \"chul\", age: 10} ,{name :\"ahn\", age:8}] }}");
		
		System.out.println("total :  " + var.find("indf.total"));
		System.out.println("row   :  " + var.find("indf.row"));
		System.out.println("items :  " + var.find("indf.items").size());
		
		System.out.println("--- items ---");		
		for (int i=0; i<var.find("indf.items").size(); i++) {
			Var items = var.find("indf.items").get(i);
			System.out.println(i+") name: " + items.get("name") + ", age: " + items.get("age"));
		}
		
* result:

	    total :  10
	    row   :  2
	    items :  2

	    --- items ---
	    0) name: chul, age: 10
	    1) name: ahn, age: 8


####c) MAP > MAP > LIST ... find() ####

* souce:

		Var var = Djson.parse("{indf : { total: 10, row:2 , items : [{name: \"chul\", age: 10} ,{name :\"ahn\", age:8}] }}");
		
		System.out.println(var.find("indf.items[1].name"));
		System.out.println(var.find("indf.items[1].age"));

* result:

		ahn
		8


####d) LIST > LIST ... find() ####

* souce:

		Var var = Djson.parse("[ [3,4,5], [7,8,9,10 ]]");
		
		System.out.println("ARRAY SIZE 1 : " + var.find("[0]").size());
		System.out.println("ARRAY VALUE1 : " + var.find("[0].[2]"));
		
		System.out.println("ARRAY SIZE 2 : " + var.find("[1]").size());
		System.out.println("ARRAY VALUE2 : " + var.find("[1].[3]"));

* result:

		ARRAY SIZE 1 : 3
		ARRAY VALUE1 : 5
		ARRAY SIZE 2 : 4
		ARRAY VALUE2 : 10


### 4. END ###
