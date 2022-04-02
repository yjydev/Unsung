import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import scala.Tuple2;

import java.time.Duration;
import java.util.*;

public class SparkStreamingTest {

    static HashMap<String, Integer> positive;
    static HashMap<String, Integer> negative;
    static HashMap<String, Integer> neutrality;
    static HashMap<String, Integer> unclassified;

    @SuppressWarnings("serial")
    public static void main(String[] args) throws InterruptedException, ParseException {
        SparkConf conf = new SparkConf().setAppName("kafka-spark").setMaster("local[2]").set("spark.driver.allowMultipleContexts", "true");
        JavaStreamingContext ssc = new JavaStreamingContext(conf, Durations.seconds(10));

        Map<String, Object> kafkaParams = new HashMap<String, Object>();
        kafkaParams.put("bootstrap.servers", "localhost:9092");
        kafkaParams.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaParams.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaParams.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaParams.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaParams.put("group.id", "spark_id");
        kafkaParams.put("auto.offset.reset", "latest");
        kafkaParams.put("enable.auto.commit", false);


//        Collection<String> topics = Arrays.asList("keywordList"); // topic name
//
//        JavaInputDStream<ConsumerRecord<String,String>> stream = KafkaUtils.createDirectStream(
//                ssc, LocationStrategies.PreferConsistent(), ConsumerStrategies.<String,String>Subscribe(topics,kafkaParams));

        //create consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(kafkaParams);
        consumer.subscribe(Arrays.asList("keywordList"));

        positive = new HashMap<String, Integer>();
        negative = new HashMap<String, Integer>();
        neutrality = new HashMap<String, Integer>();
        unclassified = new HashMap<String, Integer>();

        JSONParser parser = new JSONParser();

        while (true){
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(3));

            for (ConsumerRecord<String, String> record : records){
                String data = record.value();

                JSONObject jsonObject = (JSONObject) parser.parse(data);
                String label = jsonObject.get("label").toString();

                try {
                    JSONArray keywordList = (JSONArray) jsonObject.get("keyword");
                    for (Object keyword : keywordList){
                        count(keyword.toString(), label);
                    }
                }
                catch (Exception e){

                }
                System.out.println(jsonObject.get("label") + " , " + jsonObject.get("keyword"));
            }
        }

//      Ty  stream.mapToPair(
//                new PairFunction<ConsumerRecord<String,Object>, String, String>(){
//                    public Tuple2<String,String> call(ConsumerRecord<String,Object> record){
//                        return new Tuple2<String,String>(record.key(), record.value().toString());
//                    }
//                });


//        stream.map(raw->raw.value()).print();

//        //possible!!!!
//        JavaDStream<String> line = stream.map(raw -> raw.value());
//        line.print();


//        JavaDStream<String> line_data = line.flatMap(new FlatMapFunction<String, String>() {
//           private  static final long serialVersionUID = 1L;
//
//            @Override
//            public Iterator<String> call(String s) {
//                List<String> data = new ArrayList<String>();
//                JsonObject jsonObject = new JsonParser().parse(s).getAsJsonObject();
//
//                try {
//                    String label = jsonObject.get("label").getAsString();
//                    String keyword = jsonObject.get("keyword").getAsString();
//
//                    JsonArray jsonArray = new JsonParser().parse(jsonObject.get("data").toString()).getAsJsonArray();
//
//                    for(int count = 0 ; count < jsonArray.size() ; count ++){
//                        JsonObject temp = new JsonObject();
//
//                        temp.addProperty("label", label);
//                        temp.addProperty("keyword", keyword);
//
//                        data.add(temp.toString());
//                    }
//                }
//                catch (Exception e){
//                    System.out.println("validation!");
//                }
//
//                return (Iterator<String>) data;
//            }
//        });
//
//        line_data.print();
//
//        ssc.start(); //streaming app activate
//        ssc.awaitTermination(); // 강제 종료 or stop()코드 발견 전까지 실행 유지

    }

    static void count(String keyword, String label){
        int cnt = - 1;
        if(label.equals("0")){
            if(negative.containsKey(keyword)){
                cnt = negative.get(keyword);
                negative.put(keyword, cnt + 1);
            }
            else {
                negative.put(keyword, 1);
            }
        }
        else if(label.equals("1")){
            if(positive.containsKey(keyword)){
                cnt = positive.get(keyword);
                positive.put(keyword, cnt + 1);
            }
            else {
                positive.put(keyword, 1);
            }
        }
        else if(label.equals("2")){
            if(neutrality.containsKey(keyword)){
                cnt = neutrality.get(keyword);
                neutrality.put(keyword, cnt + 1);
            }
            else {
                neutrality.put(keyword, 1);
            }
        }
        else {
            if(unclassified.containsKey(keyword)){
                cnt = unclassified.get(keyword);
                unclassified.put(keyword, cnt + 1);
            }
            else {
                unclassified.put(keyword, 1);
            }
        }

        System.out.println( " positive =  " + positive.size() );
        System.out.println( " negative =  " + negative.size() );
        System.out.println( " neutrality =  " + neutrality.size() );
        System.out.println( " unclassified =  " + unclassified.size() );
    }
}