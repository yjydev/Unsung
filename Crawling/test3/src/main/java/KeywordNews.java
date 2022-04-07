import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class KeywordNews {
    public static void main(String[] args) throws IOException {
        ArrayList<NewsDto> list = crawling("농수산물도매시장");
    }

    private static ArrayList<NewsDto> crawling(String keyword) throws IOException {
        ArrayList<NewsDto> list = new ArrayList<>();

        String URL = "https://search.naver.com/search.naver?where=news&sm=tab_pge&query="+keyword+"&sort=0&photo=0&field=0&pd=0&ds=&de=&cluster_rank=38&mynews=0&office_type=0&office_section_code=0&news_office_checked=&nso=so:r,p:all,a:all&start=1";

        Document doc = Jsoup.connect(URL).get();
        System.out.println(URL);
        Element element = doc.selectFirst("div[class=\"group_news\"] > ul[class=\"list_news\"]");

        for (Element liEle : element.select("li[class=\"bx\"] > div[class=\"news_wrap api_ani_send\"]")) {
            Elements news_info = liEle.select("div[class=\"news_area\"]");
            String press = news_info.select("div[class=\"news_info\"] > div[class=\"info_group\"] > a[class=\"info press\"]").text();
            Elements titleAndUrl = news_info.select("a[class=\"news_tit\"]");
            String title = titleAndUrl.get(0).attr("title");
            String url = titleAndUrl.get(0).attr("href");
            String photo = liEle.select("a > img").attr("src");

            NewsDto newsDto = new NewsDto();
            newsDto.setImage(photo);
            newsDto.setTitle(title);
            newsDto.setPress(press);
            newsDto.setUrl(url);
            System.out.println(newsDto.toString());

            list.add(newsDto);
        }
        return list;
    }
}
