import org.apache.kafka.clients.producer.*;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Properties;

public class Producer {
    private static final String TOPIC_NAME = "newsStream";

    public static void main(String[] args) throws InterruptedException, IOException {
        Properties prop = new Properties();
//        prop.put("bootstrap.servers", "{ip address}:9092,{ip address}:9093,{ip address}:9094"); // server, kafka host
        prop.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094"); // server, kafka host
        prop.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        prop.put("value.serializer", "org.springframework.kafka.support.serializer.JsonSerializer");
        prop.put("acks", "all");
        prop.put("max.request.size", "2049152");
        JSONObject message = null;

        // producer 생성
        @SuppressWarnings("resource")
        KafkaProducer<String, JSONObject> producer = new KafkaProducer<>(prop);

        ZonedDateTime nowUTC = ZonedDateTime.now(ZoneId.of("UTC"));
        LocalDateTime now = nowUTC.withZoneSameInstant(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        LocalDateTime ldt = LocalDateTime.of(now.getYear(),
                now.getMonth(), now.getDayOfMonth(), now.getHour(), now.getMinute(), 0);
        LocalDateTime newldt = ldt.minusMinutes(10);

        HashMap<String, String> press = new HashMap<>();
        press.put("001", "연합뉴스");
        press.put("052", "YTN");

        for (String oid : press.keySet()) {
            String today = String.join("", now.toString().split("T")[0].split("-"));
            DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm");

            String pageCnt = "https://news.naver.com/main/list.naver?mode=LPOD&mid=sec&oid=" + oid + "&listType=title&date=" + today + "&page=200";
            Document doc = Jsoup.connect(pageCnt).get();
            Elements elements = doc.select("div[class=\"paging\"] > strong");
            int max = Integer.parseInt(elements.text());

            outer:
            for (int j = 1; j <= max; j++) {
                String URL = "https://news.naver.com/main/list.naver?mode=LPOD&mid=sec&oid=" + oid + "&listType=title&date=" + today + "&page=" + j;
                doc = Jsoup.connect(URL).get();
                Elements element = doc.select("ul[class=\"type02\"]");

                for (Element ulEle : element.select("li")) {
                    Elements tag = ulEle.select("a");
                    for (Element e : tag) {
                        String newsUrl = e.attr("href");
                        Document subDoc = Jsoup.connect(newsUrl).get();

                        String title = null;
                        String content = null;
                        String time = null;

                        if (subDoc.getElementById("articleTitle") == null) {
                            if (subDoc.getElementsByClass("end_tit").isEmpty()) {
                                // 스포츠
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

                        if (timeSpl[1].equals("오후") && !timeSpl[2].substring(0, 2).equals("12")) {
                            newsDate = date.plusHours(12);
                        } else if (timeSpl[1].equals("오전") && timeSpl[2].substring(0, 2).equals("12")) {
                            newsDate = date.minusHours(12);
                        } else {
                            newsDate = date;
                        }

                        if ((newldt.isBefore(newsDate)|newldt.isEqual(newsDate)) && ldt.isAfter(newsDate)) {
                            JSONObject data = new JSONObject();
                            data.put("title", title);
                            data.put("content", content);
                            data.put("url", newsUrl);
                            data.put("time", String.valueOf(newsDate));
                            data.put("press", press.get(oid));
                            message = data;
//                            System.out.println(message);
                            try {
                                producer.send(new ProducerRecord<String, JSONObject>(TOPIC_NAME, message), new Callback() {
                                    @Override
                                    public void onCompletion(RecordMetadata recordMetadata, Exception exception) {
                                        if (exception!= null)
                                            exception.printStackTrace();
                                    }
                                });
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        } else {
                            break outer;
                        }
                    }
                }
            }
        }
    }
}
