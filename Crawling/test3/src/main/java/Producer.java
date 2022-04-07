import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Producer {

    public static void main(String[] args) throws InterruptedException, IOException {
        String[] oid = {"001", "025", "052", "055", "056", "437"};

        HashMap<String, String> press = new HashMap<>();
        press.put("001", "연합뉴스");
        press.put("025", "중앙일보");
        press.put("052", "YTN");
        press.put("055", "SBS"); // 범님거 대신 돌리기
        press.put("056", "KBS");
        press.put("437", "JTBC");

        for(int i=1;i<2;i++) {
            crawlingPage(oid[i], press.get(oid[i]), 20210101, 20210131);
            crawlingPage(oid[i], press.get(oid[i]), 20210201, 20210228);
            crawlingPage(oid[i], press.get(oid[i]), 20210301, 20210331);
            crawlingPage(oid[i], press.get(oid[i]), 20210401, 20210430);
            crawlingPage(oid[i], press.get(oid[i]), 20210501, 20210531);
            crawlingPage(oid[i], press.get(oid[i]), 20210601, 20210630);
            crawlingPage(oid[i], press.get(oid[i]), 20210701, 20210731);
            crawlingPage(oid[i], press.get(oid[i]), 20210801, 20210831);
            crawlingPage(oid[i], press.get(oid[i]), 20210901, 20210930);
            crawlingPage(oid[i], press.get(oid[i]), 20211001, 20211031);
            crawlingPage(oid[i], press.get(oid[i]), 20211101, 20211130);
            crawlingPage(oid[i], press.get(oid[i]), 20211201, 20211231);
        }
    }

    public static void crawlingPage(String oid, String pressName, int start, int end) throws IOException {
        File csv = new File("C:\\특화프로젝트\\DATA\\" + pressName+".csv");
        BufferedWriter bw = null;
        bw = new BufferedWriter(new FileWriter(csv, true));

//        bw = Files.newBufferedWriter(Paths.get("/home/hadoop/IdeaProjects/data/" + pressName + ".csv"), Charset.forName("UTF-8"));

        // 전체 페이지 정보 가져오기
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm");

        for (int i = start;i<=end;i++) {

            String pageCnt = "https://news.naver.com/main/list.naver?mode=LPOD&mid=sec&oid="+oid+"&listType=title&date=" + i;
            Document doc = Jsoup.connect(pageCnt).get();
            Elements elements = doc.select("div[class=\"paging\"] > a");
            int max = elements.size() + 1;

            for (int j = 1; j <= max; j++) {
//                System.out.println("page: " + j);
                String URL = "https://news.naver.com/main/list.naver?mode=LPOD&mid=sec&oid="+oid+"&listType=title&date=" + i + "&page=" + j;

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
//                                System.out.println("스포츠");
                                title = subDoc.selectFirst("div[class=\"news_headline\"] > h4[class=\"title\"]").text();
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

                        String[] timeSpl = time.split(" ");
                        if (timeSpl[2].length() == 4)
                            timeSpl[2] = "0" + timeSpl[2];
                        String dateTime = timeSpl[0] + " " + timeSpl[2];

                        LocalDateTime date = LocalDateTime.parse(dateTime, sdf);
                        LocalDateTime newsDate = null;

                        // am
                        if (timeSpl[1].equals("오후") && !timeSpl[2].substring(0, 2).equals("12")) {
                            newsDate = date.plusHours(12);
                        } else if(timeSpl[1].equals("오전") && timeSpl[2].substring(0, 2).equals("12")) {
                            newsDate = date.minusHours(12);
                        } else {
                            newsDate = date;
                        }

                        System.out.println("제목: " + title);
//                        System.out.println("내용: " + content);
                        System.out.println("시간: " + newsDate);
                        System.out.println("기사 링크: " + newsUrl);
//                        System.out.println();

//                        String info = title+"\t"+content+"\t"+newsDate+"\t"+newsUrl+"\t";
                        bw.append(title + "\t");
                        bw.append(content + "\t");
                        bw.append(newsDate + "\t");
                        bw.append(newsUrl + "\t");
                        bw.append("\n");
                    }
                }
            }
        }
        bw.flush();
        bw.close();
    }

}

