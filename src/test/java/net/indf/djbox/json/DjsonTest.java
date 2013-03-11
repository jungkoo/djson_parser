package net.indf.djbox.json;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Ignore;
import org.junit.Test;
public class DjsonTest {
	
	@Test
	public void urlTest() throws IOException {
		URL url = new URL("http://apis.daum.net/socialpick/search?category=e&output=json");
		Var json = Djson.parse(url);
	}
	
	@Test
	 public void stringTest() {
		 Var var = Djson.parse("[ 123123, 9]");		 
		 assertThat(var.isType(), is(DataType.LIST));
		 assertThat(var.size(), is(2));
		 assertThat(var.get(0).toInt(), is(123123));
		 assertThat(var.get(1).toInt(), is(9));
	}
	
	@Test
	public void jsonTest() {
		String json = "[12, true, false, \"abc\", null, 34.56]";
		Var var = Djson.parse(json);
		assertThat(Djson.toJson(var), is(json));
	}
	
	@Test
	public void fileTest() throws IOException {
		//http://www.tistory.com/developer/apidoc/comment.php#comment-newest
		Var var = Djson.parse(new File("src/test/resources/json/sample.json"));
		
		assertThat(var.find("tistory.item.comments.comment").size(), is(3) );
		assertThat(var.find("tistory.item.comments.comment[1].comment").toString(), is("제 홈에 와서 구경해보세요^_^") );
		assertThat(var.find("tistory.item.comments.comment[1].open").toString(), is("N") );
		assertThat(var.find("tistory.item.comments.comment[1].id").toInt(), is(8176923) );
		assertThat(var.find("tistory.item.comments.comment[1].link").toString(), is("http://oauth.tistory.com/4#comment8176923") );
	}
	
	@Test
	public void longFileTest() throws IOException {
		//sample data is   : daum 소셜픽.
		Var var = Djson.parse(new File("src/test/resources/json/socialpick.json"));
		
		assertThat(var.find("socialpick.item").size(), is(10));
		/**
		 * {"rank":"4", "link":"http://search.daum.net/search?q=%EC%A0%95%EA%B8%80%EC%9D%98%EB%B2%95%EC%B9%99%20%EB%89%B4%EC%A7%88%EB%9E%9C%EB%93%9C&DA=DLVC&rtmaxcoll=DQP"
		 * , "keyword":"정글의법칙 뉴질랜드"
		 * ,"content":"정석원이 해병대 본능을 발휘했다. 8일 방송된 SBS '정글의 법칙'에서는 새롭게 구성된 병만족 멤버들과의 뉴질랜드 생존기가 첫 전파를 탔다. 병만 족장을 중심으로 기존멤버 리키김, 노우진, 박정철과 새 멤버 이필모, 정석원, 박보영으로"
		 * , "count":"48147", "quotation_cnt":"7", "comment_cnt":"938", "rank_diff":"2", "category":"e"}
		 */
		assertThat(var.find("socialpick.item[3].keyword").toString(), is("정글의법칙 뉴질랜드"));
		assertThat(var.find("socialpick.item[3].count").toInt(), is(48147));
		assertThat(var.find("socialpick.item[3].content").toString().startsWith("정석원이 해병대 본능을"), is(true));
		assertThat(var.find("socialpick.item[3].content").toString().endsWith(" 박보영으로"), is(true));
	}
}
