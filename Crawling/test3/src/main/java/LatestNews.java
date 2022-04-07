import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class LatestNews {
    public static void main(String[] args) throws IOException {
        ArrayList<NewsDto> list = crawling();
    }

    private static ArrayList<NewsDto> crawling() throws IOException {
        ArrayList<NewsDto> list = new ArrayList<>();
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm");

        ZonedDateTime nowUTC = ZonedDateTime.now(ZoneId.of("UTC"));
        LocalDate now = nowUTC.withZoneSameInstant(ZoneId.of("Asia/Seoul")).toLocalDate();
        String today = String.join("", now.toString().split("-"));
        String URL = "https://news.naver.com/main/list.naver?mode=LSD&mid=sec&sid1=001&listType=summary&date=" + today;

        Document doc = Jsoup.connect(URL).get();
        Element element = doc.selectFirst("div[class=\"list_body newsflash_body\"] > ul[class=\"type06\"]");

        for (Element liEle : element.select("li")) {
            NewsDto newsDto = new NewsDto();

            Element photoEle = liEle.selectFirst("dl > dt[class=\"photo\"] > a");
            String photo = photoEle.select("img").attr("src");

            Element info = liEle.selectFirst("dl > dt > a");
            String title = info.text();
            String url = info.attr("href");
            String press = liEle.select("dd > span[class=\"writing\"]").text();

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
