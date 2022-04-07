import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

public class Producer {

//    private static final String TOPIC_NAME = "search_word";
//
//    public static void main(String[] args) throws InterruptedException {
//        Random random = new Random();
//        Properties prop = new Properties();
//        prop.put("bootstrap.servers", "localhost:9092"); // server, kafka host
//        prop.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        prop.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        prop.put("acks", "all");
//        prop.put("block.on.buffer.full", "true");
//
//        String message = null;
//
//        @SuppressWarnings("resource")
//        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(prop);
//
//        while(true) {
//
//            message = Integer.toString(random.nextInt(100));
//            producer.send(new ProducerRecord<String,String>(TOPIC_NAME, message));
//            Thread.sleep(1000); // 1초
//
//        }
//    }
    private static final String TOPIC_NAME = "keywordList";

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

        for(int i=0;i<10;i++) {
            message = test();
            System.out.println(message);
            producer.send(new ProducerRecord<String, JSONObject>(TOPIC_NAME, message));
        }
    }

    public static JSONObject test() throws IOException {
        // 현재 시간 기준 15분 이내의 기사만 가져오기
        Random rand = new Random();
        JSONObject data = new JSONObject();
        String[] word = new String[]{"apple", "banana", "berry", "lemon", "mango", "melon", "peach", "cherry", "kiwi", "grape"};

        data.put("label", rand.nextInt(4));
        ArrayList<String> al = new ArrayList<>();
        al.add(word[rand.nextInt(10)]);
        al.add(word[rand.nextInt(10)]);
        data.put("keyword", al);

        return data;
    }
}