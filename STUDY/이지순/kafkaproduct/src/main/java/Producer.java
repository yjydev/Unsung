import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;

public class Producer {
    private static final String TOPIC_NAME = "search_word";

    public static void main(String[] args) throws InterruptedException, IOException {
        Random random = new Random();
        Properties prop = new Properties();
        prop.put("bootstrap.servers", "localhost:9092"); // server, kafka host
        prop.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        prop.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        prop.put("acks", "all");
        prop.put("block.on.buffer.full", "true");

        String message = null;

        // producer 생성
        @SuppressWarnings("resource")
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(prop);

        // message 전달
        while(true) {
            ArrayList<HashMap<String, String>> list = crawling();
            for (int i=0;i<list.size();i++) {
                message = String.valueOf(list.get(i));
//            message = Integer.toString(random.nextInt(100)); // 1~100 중 랜덤숫자
                producer.send(new ProducerRecord<String, String>(TOPIC_NAME, message));
            }
            System.out.println("stop ------------------------------");
            Thread.sleep(60000); // 1초 - 1000
        }
    }
    
    public static ArrayList<HashMap<String, String>> crawling() throws IOException {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        String[] oid = {"020", "025"};
        HashMap<String, String> press = new HashMap<>();
        press.put("020", "동아일보");
        press.put("025", "중앙일보");

        String oriURL = "https://news.naver.com/main/list.naver?mode=LPOD&mid=sec&listType=title&date=20220308&page=1&oid=";

        for(int i=0;i<oid.length;i++) {
            String URL = oriURL + oid[i];
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

                    HashMap<String, String> map = new HashMap<>();
                    map.put("newspaper", press.get(oid[i]));
                    map.put("title", title);
                    map.put("content", content);
                    map.put("url", newsUrl);
                    System.out.println(map);
                    list.add(new HashMap<>(map));
                }
            }
        }
        return list;
    }
}
