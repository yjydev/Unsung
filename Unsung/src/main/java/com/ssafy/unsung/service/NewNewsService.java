package com.ssafy.unsung.service;

import com.ssafy.unsung.dto.NewNewsDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
public class NewNewsService {

    @Transactional
    public ArrayList<NewNewsDto> crawling() throws IOException {
        ArrayList<NewNewsDto> list = new ArrayList<>();
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm");

        ZonedDateTime nowUTC = ZonedDateTime.now(ZoneId.of("UTC"));
        LocalDate now = nowUTC.withZoneSameInstant(ZoneId.of("Asia/Seoul")).toLocalDate();
        String today = String.join("", now.toString().split("-"));
        String URL = "https://news.naver.com/main/list.naver?mode=LSD&mid=sec&sid1=001&listType=summary&date=" + today;

        Document doc = Jsoup.connect(URL).get();
        Element element = doc.selectFirst("div[class=\"list_body newsflash_body\"] > ul[class=\"type06\"]");

        for (Element liEle : element.select("li")) {
            NewNewsDto newNewsDto = new NewNewsDto();

            Element photoEle = liEle.selectFirst("dl > dt[class=\"photo\"] > a");
            String photo = photoEle.select("img").attr("src");

            if(photo.equals("")){
                photo = "no image";
            }

            Element info = liEle.selectFirst("dl > dt > a");
            String title = info.text();
            String url = info.attr("href");
            String press = liEle.select("dd > span[class=\"writing\"]").text();

            //newNewsDto.setImage(photo);
            newNewsDto.setTitle(title);
            newNewsDto.setPress(press);
            newNewsDto.setUrl(url);
            System.out.println(newNewsDto.toString());

            list.add(newNewsDto);
        }
        return list;
    }

}
