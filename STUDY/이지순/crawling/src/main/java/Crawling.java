import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;

public class Crawling {
    public static void main(String[] args) throws IOException {
        String[] oid = {"020", "025", "023", "028", "001", "009"};

        HashMap<String, String> press = new HashMap<>();
        press.put("020", "동아일보");
        press.put("025", "중앙일보");
        press.put("023", "조선일보");
        press.put("028", "한겨래");
        press.put("001", "연합뉴스");
        press.put("009", "매일경제");

        String oriURL = "https://news.naver.com/main/list.naver?mode=LPOD&mid=sec&listType=title&date=20220308&page=1&oid=";

        for(int i=0;i<oid.length;i++) {
            System.out.println(press.get(oid[i]));
            System.out.println("------------------------------------------------------------");
            String URL = oriURL + oid[i];
            System.out.println(URL);
            Document doc = Jsoup.connect(URL).get();
            Elements element = doc.select("div[class=\"list_body newsflash_body\"]");

            for (Element ulEle : element.select("li")) {
                Elements tag = ulEle.select("a");
                for (Element e : tag) {
                    String newsUrl = e.attr("href");

                    Document subDoc = Jsoup.connect(newsUrl).get();

                    if (subDoc.getElementById("articleTitle") == null)
                        continue;

                    String title = subDoc.getElementById("articleTitle").text();

                    Element contentElement = subDoc.getElementById("articleBodyContents");
                    String content = contentElement.text();

                    System.out.println("제목: " + title);
                    System.out.println("내용: " + content);
                    System.out.println("기사 링크: "+newsUrl);
                    System.out.println();
                }
            }
        }
    }
}
