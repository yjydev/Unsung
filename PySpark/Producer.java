import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
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
import java.util.ArrayList;
import java.util.Properties;

public class Producer {
    private static final String TOPIC_NAME = "test6";

    public static void main(String[] args) throws InterruptedException, IOException {
        Properties prop = new Properties();
        prop.put("bootstrap.servers", "localhost:9092"); // server, kafka host
        prop.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        prop.put("value.serializer", "org.springframework.kafka.support.serializer.JsonSerializer");
        prop.put("acks", "all");
        prop.put("block.on.buffer.full", "true");

        JSONObject message = null;

        // producer 생성
        @SuppressWarnings("resource")
        KafkaProducer<String, JSONObject> producer = new KafkaProducer<>(prop);
        // message 전달
        while(true) {
            ZonedDateTime nowUTC = ZonedDateTime.now(ZoneId.of("UTC"));
            LocalDateTime now = nowUTC.withZoneSameInstant(ZoneId.of("Asia/Seoul")).toLocalDateTime();
            now = now.minusMinutes(300);

            ArrayList<JSONObject> list = crawlingTime(now);
            for (int i=0;i<list.size();i++) {
                message = list.get(i);
                producer.send(new ProducerRecord<String, JSONObject>(TOPIC_NAME, message));
            }
            System.out.println("stop ------------------------------");
            Thread.sleep(60000); // 1초 - 1000
        }
    }

    public static ArrayList<JSONObject> crawlingTime(LocalDateTime initTime) throws IOException {
        // 현재 시간 기준 15분 이내의 기사만 가져오기
        ArrayList<JSONObject> list = new ArrayList<>();

        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm");

        String URL = "https://news.naver.com/main/list.naver?mode=LPOD&mid=sec&oid=025&listType=title&date=20220330&page=" + 1;

        Document doc = Jsoup.connect(URL).get();
        Elements elements = doc.select("div[class=\"paging\"] > a");

        Elements element = doc.select("ul[class=\"type02\"]");


        outer: for (Element ulEle : element.select("li")) {
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
                String[] timeSpl = time.split(" ");
                if (timeSpl[2].length() == 4)
                    timeSpl[2] = "0" + timeSpl[2];
                String dateTime = timeSpl[0] + " " + timeSpl[2];

                LocalDateTime date = LocalDateTime.parse(dateTime, sdf);
                LocalDateTime newsDate = null;

                if (timeSpl[1].equals("오후") && !timeSpl[2].substring(0, 2).equals("12")) {
                    newsDate = date.plusHours(12);
                } else {
                    newsDate = date;
                }

                if (initTime.isBefore(newsDate)) {
                    JSONObject data = new JSONObject();
                    data.put("title", title);
                    data.put("content", content);
                    data.put("url", newsUrl);
                    data.put("time", String.valueOf(newsDate));
                    System.out.println(data);
                    list.add(data);
                } else {
                    break outer;
                }
            }
        }
        return list;
    }
}