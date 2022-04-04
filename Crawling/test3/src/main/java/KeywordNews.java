import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class KeywordNews {
    public static void main(String[] args) throws IOException {
        ArrayList<NewsDto> list = crawling("김연경");
    }

    private static ArrayList<NewsDto> crawling(String keyword) throws IOException {
        ArrayList<NewsDto> list = new ArrayList<>();
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm");

        ZonedDateTime nowUTC = ZonedDateTime.now(ZoneId.of("UTC"));
        LocalDate now = nowUTC.withZoneSameInstant(ZoneId.of("Asia/Seoul")).toLocalDate();
        String today = String.join("", now.toString().split("-"));
        System.out.println(today);
        String URL = "https://search.naver.com/search.naver?where=news&sm=tab_pge&query="+keyword+"&sort=0&photo=0&field=0&pd=0&ds=&de=&cluster_rank=38&mynews=0&office_type=0&office_section_code=0&news_office_checked=&nso=so:r,p:all,a:all&start=1";

        Document doc = Jsoup.connect(URL).get();
        System.out.println(URL);
        Element element = doc.selectFirst("div[class=\"list_body newsflash_body\"] > ul[class=\"type06_headline\"]");

        for (Element liEle : element.select("li")) {
            NewsDto newsDto = new NewsDto();
            String photo = liEle.select("dl > dt[class=\"photo\"] > a > img").attr("src");
            String title = liEle.select("dl > dt > a").text();
            String press = liEle.select("dd > span[class=\"writing\"]").text();
            newsDto.setImage(photo);
            newsDto.setTitle(title);
            newsDto.setPress(press);
            System.out.println(newsDto.toString());

            list.add(newsDto);
        }
        return list;
    }
}
