import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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

        crawlingPage();
    }

    public static void crawlingTime() throws IOException {
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy.mm.dd. HH:mm");

        String pageCnt = "https://news.naver.com/main/list.naver?mode=LPOD&mid=sec&oid=020&listType=title&date=20220322&page=1";
        Document doc = Jsoup.connect(pageCnt).get();
        Elements elements = doc.select("div[class=\"paging\"] > a");
        int max = elements.size()+1;

        for(int i=1;i<=max;i++) {
            System.out.println("page: "+i);
            String URL = "https://news.naver.com/main/list.naver?mode=LPOD&mid=sec&oid=020&listType=title&date=20220322&page=" + i;

            doc = Jsoup.connect(URL).get();
            elements = doc.select("div[class=\"paging\"] > a");

            Elements element = doc.select("ul[class=\"type02\"]");


            for (Element ulEle : element.select("li")) {
                Elements tag = ulEle.select("a");

                outer:
                for (Element e : tag) {
                    String newsUrl = e.attr("href");
                    Document subDoc = Jsoup.connect(newsUrl).get();

                    String title = null;
                    String content = null;
                    String time = null;

                    if (subDoc.getElementById("articleTitle") == null) {
                        if (subDoc.getElementsByClass("end_tit").isEmpty()) {
                            // 스포츠
                            title = subDoc.getElementsByClass("title").text();
                            content = subDoc.getElementById("newsEndContents").text();
                            time = subDoc.selectFirst("div.info > span").text();
                            time = time.replace("기사입력 ", "");
                        } else {
                            // 연예
                            title = subDoc.getElementsByClass("end_tit").text();
                            content = subDoc.getElementsByClass("end_body_wrp").text();
                            time = subDoc.select("span.author > em").text();
                        }
                    } else {
                        title = subDoc.getElementById("articleTitle").text();
                        content = subDoc.getElementById("articleBodyContents").text();
                        time = subDoc.selectFirst("div.sponsor > span[class=\"t11\"]").text();
                    }

                    System.out.println("제목: " + title);
                    System.out.println("내용: " + content);
                    System.out.println("시간: " + time);
                    System.out.println("기사 링크: " + newsUrl);
                    System.out.println();
                }
            }
        }
    }


    public static void crawlingPage() throws IOException {
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy.mm.dd. HH:mm");

        String pageCnt = "https://news.naver.com/main/list.naver?mode=LPOD&mid=sec&oid=020&listType=title&date=20220322&page=1";
        Document doc = Jsoup.connect(pageCnt).get();
        Elements elements = doc.select("div[class=\"paging\"] > a");
        int max = elements.size()+1;

        for(int i=1;i<=max;i++) {
            System.out.println("page: "+i);
            String URL = "https://news.naver.com/main/list.naver?mode=LPOD&mid=sec&oid=020&listType=title&date=20220322&page=" + i;

            doc = Jsoup.connect(URL).get();
            elements = doc.select("div[class=\"paging\"] > a");

            Elements element = doc.select("ul[class=\"type02\"]");


            for (Element ulEle : element.select("li")) {
                Elements tag = ulEle.select("a");

//            Element dateInfo = null;
//            if(ulEle.selectFirst("span.date.is_new") != null) {
//                dateInfo = ulEle.selectFirst("span.date.is_new");
//            } else {
//                dateInfo = ulEle.selectFirst("span.date.is_outdated");
//            }
//            System.out.println(dateInfo.text());

                outer:
                for (Element e : tag) {
                    String newsUrl = e.attr("href");
                    Document subDoc = Jsoup.connect(newsUrl).get();

                    String title = null;
                    String content = null;
                    String time = null;

                    if (subDoc.getElementById("articleTitle") == null) {
                        if (subDoc.getElementsByClass("end_tit").isEmpty()) {
                            // 스포츠
                            title = subDoc.getElementsByClass("title").text();
                            content = subDoc.getElementById("newsEndContents").text();
                            time = subDoc.selectFirst("div.info > span").text();
                            time = time.replace("기사입력 ", "");
                        } else {
                            // 연예
                            title = subDoc.getElementsByClass("end_tit").text();
                            content = subDoc.getElementsByClass("end_body_wrp").text();
                            time = subDoc.select("span.author > em").text();
                        }
                    } else {
                        title = subDoc.getElementById("articleTitle").text();
                        content = subDoc.getElementById("articleBodyContents").text();
                        time = subDoc.selectFirst("div.sponsor > span[class=\"t11\"]").text();
                    }


//                String[] timeSpl = time.split(" ");
//                if(timeSpl[2].length()==4)
//                    timeSpl[2] = "0"+timeSpl[2];
//                String dateTime = timeSpl[0] +" "+ timeSpl[2];
//
//                LocalDateTime date = LocalDateTime.parse(dateTime);
//                LocalDateTime newsDate = null;
//
//                if(timeSpl[1] == "오후") {
//                    newsDate = date.plusHours(12);
//                }

                    System.out.println("제목: " + title);
                    System.out.println("내용: " + content);
                    System.out.println("시간: " + time);
//                System.out.println("기사입력: " + newsDate);
                    System.out.println("기사 링크: " + newsUrl);
                    System.out.println();
                }
            }
        }
    }

    public static void crawlingTitle(String oid, String pressName) throws IOException {
        // 타이틀만 가져오기
        BufferedWriter bw = null;
        bw = Files.newBufferedWriter(Paths.get("C:\\특화프로젝트\\DATA\\"+pressName+".csv"), Charset.forName("UTF-8"));
        String oriURL = "https://news.naver.com/main/list.naver?mode=LPOD&mid=sec&listType=title" + "&oid="+oid+"&date=";

        for(int i=20220201;i<20220228;i++) {
            String URL = oriURL + i;
            Document doc = Jsoup.connect(URL).get();
            Elements element = doc.select("ul[class=\"type02\"]");

            for (Element ulEle : element.select("li > a")) {
                bw.append(ulEle.text());
                bw.append("\t");
                bw.append("\n");
            }
        }
        bw.flush();
        bw.close();
    }
}
