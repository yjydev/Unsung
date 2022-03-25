package com.example.sparkTest;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;

public class WordCount {

    public static void main(String[] args) {
        SparkConf sparkConf = new SparkConf().setAppName("wordCount").setMaster("local");
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

        sparkContext.textFile("src/main/java/data/README.md")
                .flatMap(word -> Arrays.asList(word.split("[ ,.]")))
                .filter(word -> word.length()>0)
                .map(word -> word.toLowerCase().trim())
                .mapToPair(word -> new Tuple2<>(word, 1))
                .reduceByKey((amount, value) -> amount + value)
                .foreach(word -> {
                    System.out.println(word);
                });

        sparkContext.close();
    }
}
