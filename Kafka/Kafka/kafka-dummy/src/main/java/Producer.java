import com.opencsv.CSVReader;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.clients.producer.internals.DefaultPartitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.json.simple.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class Producer {
    private static final String TOPIC_NAME = "p2_10000";

    public static void main(String[] args) throws InterruptedException, IOException {
        Properties prop = new Properties();
        prop.put("bootstrap.servers", "{ip address}:9092,{ip address}:9093,{ip address}:9094");
        prop.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        prop.put("value.serializer", "org.springframework.kafka.support.serializer.JsonSerializer");
        prop.put("acks", "all");
        prop.put("max.request.size", "2049152");
//        prop.put("partitioner.class", SimplePartitional.class);
//        prop.put("max.message.bytes", "2049152");
//        prop.put("retries", 3);
//        prop.put("block.on.buffer.full", "true");

        JSONObject message = null;

        // producer 생성
        @SuppressWarnings("resource")
        KafkaProducer<String, JSONObject> producer = new KafkaProducer<>(prop);

        CSVReader csvReader = new CSVReader(new FileReader("SBS.csv"), '\t');

        String[] nextLine;
        final int[] count = {0};
        int failCount = 0;
//        while((nextLine= csvReader.readNext())!=null) {
        for(int i=0;i<10000;i++) {
            nextLine = csvReader.readNext();
            if(nextLine==null || nextLine.length==0) {
                failCount++;
            }
            JSONObject data = new JSONObject();
            data.put("title", nextLine[0]);
            data.put("content", nextLine[1]);
            data.put("time", String.valueOf(nextLine[2]));
            data.put("url", nextLine[3]);
            data.put("press", "SBS");
            message = data;
            System.out.println(message);
            try {
                producer.send(new ProducerRecord<String, JSONObject>(TOPIC_NAME, message), new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception exception) {
                        if (exception != null) {
                            exception.printStackTrace();
                        } else {
                            count[0]++;
                        }
                    }
                });
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        producer.close();
        System.out.println(count[0]);
        System.out.println(failCount);
    }
    public static class SimplePartitional extends DefaultPartitioner {
        private int num;

        @Override
        public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
            List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
            return (num++ + 1) % partitions.size();
        }
    }
}
