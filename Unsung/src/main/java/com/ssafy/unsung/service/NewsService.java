package com.ssafy.unsung.service;

import com.ssafy.unsung.dto.NewsDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
public class NewsService {

    @Transactional
    public ArrayList<NewsDto> crawling() throws IOException {
        ArrayList<NewsDto> list = new ArrayList<>();
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm");

        ZonedDateTime nowUTC = ZonedDateTime.now(ZoneId.of("UTC"));
        LocalDate now = nowUTC.withZoneSameInstant(ZoneId.of("Asia/Seoul")).toLocalDate();
        String today = String.join("", now.toString().split("-"));
        String URL = "https://news.naver.com/main/list.naver?mode=LSD&mid=sec&sid1=001&listType=summary&date=" + today;

        Document doc = Jsoup.connect(URL).get();
        Element element = doc.selectFirst("div[class=\"list_body newsflash_body\"] > ul[class=\"type06\"]");

        for (Element liEle : element.select("li")) {
            NewsDto newNewsDto = new NewsDto();

            String title = null;
            String photo = null;
            String press = null;
            String url = null;

            if(!liEle.getElementsByClass("photo").isEmpty()) {
                Element a = liEle.selectFirst("dl > dt[class=\"photo\"] > a");
                photo = a.select("img").attr("src");
                title = a.select("img").attr("alt");
                url = a.attr("href");
            } else {
                Element a = liEle.selectFirst("dl > dt > a");
                title = a.text();
                url = a.attr("href");
            }

            press = liEle.select("dd > span[class=\"writing\"]").text();


            newNewsDto.setImage(photo);
            newNewsDto.setTitle(title);
            newNewsDto.setPress(press);
            newNewsDto.setUrl(url);
            System.out.println(newNewsDto.toString());

            list.add(newNewsDto);
        }
        return list;
    }

    @Transactional
    public ArrayList<NewsDto> crawling(String keyword) throws IOException {
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
            //newsDto.setImage(photo);
            newsDto.setTitle(title);
            newsDto.setPress(press);
            newsDto.setUrl(url);
            System.out.println(newsDto.toString());

            list.add(newsDto);
        }
        return list;
    }

}
