package com.example.sparkTest;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SparkTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SparkTestApplication.class, args);
	}


	SparkConf conf = new SparkConf();
	conf.setAppName("firstProgramming");

	conf.setMaster("local");

	JavaSparkContext sparkContext = new JavaSparkContext(conf);

	JavaRDD<Stringf> textRDD = sparkContext.textFile("src/main/java/data/test.txt");

	long data = textRDD.count();
	System.out.println("RDD갯수=>" + data);

	textRDD.foreach(line -> System.out.println(line));
}
