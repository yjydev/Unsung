import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SparkStreamingTest {
    @SuppressWarnings("serial")
    public static void main(String[] args) throws InterruptedException {
        SparkConf conf = new SparkConf().setAppName("kafka-spark").setMaster("local[2]").set("spark.driver.allowMultipleContexts", "true");
        JavaStreamingContext ssc = new JavaStreamingContext(conf, Durations.seconds(10));

        Map<String,Object> kafkaParams = new HashMap<String,Object>();
        kafkaParams.put("bootstrap.servers", "localhost:9092");
        kafkaParams.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaParams.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        kafkaParams.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaParams.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        kafkaParams.put("group.id","spark_id");
        kafkaParams.put("auto.offset.reset","latest");
        kafkaParams.put("enable.auto.commit",false);

        Collection<String> topics = Arrays.asList("search_word"); // topic name

        JavaInputDStream<ConsumerRecord<String,String>> stream = KafkaUtils.createDirectStream(
                ssc, LocationStrategies.PreferConsistent(), ConsumerStrategies.<String,String>Subscribe(topics,kafkaParams));

        stream.mapToPair(
                new PairFunction<ConsumerRecord<String,String>, String, String>(){
                    public Tuple2<String,String> call(ConsumerRecord<String,String> record){
                        return new Tuple2<String,String>(record.key(),record.value());
                    }
                });

        stream.map(raw->raw.value()).print();

        ssc.start();
        ssc.awaitTermination();
    }
}
