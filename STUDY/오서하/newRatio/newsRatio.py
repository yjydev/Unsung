# 이후에 pyspark가 실행되기 때문에 맨 위에 위치하기
import findspark, os, time
findspark.find()
findspark.init(os.environ.get("SPARK_HOME"))

os.environ['PYSPARK_SUBMIT_ARGS'] = '--packages org.apache.spark:spark-sql-kafka-0-10_2.12:3.1.2 newsRatio.py'

# Spark에 연결하기
from pyspark.streaming import StreamingContext
import pyspark.sql.functions as F
from pyspark.sql.types import *
from pyspark.sql import *
from pyspark.sql.functions import col, pandas_udf, split, json_tuple
# from pyspark.streaming.kafka import KafkaUtils
import json
import numpy as np


from pyspark import SparkContext

sc = SparkContext()
log4jLogger = sc._jvm.org.apache.log4j
log4jLogger.LogManager.getLogger("org").setLevel(log4jLogger.Level.OFF)
log4jLogger.LogManager.getLogger("akka").setLevel(log4jLogger.Level.OFF)

spark_session = SparkSession \
        .builder \
        .appName("pyspark_kafka5")\
        .getOrCreate()

#DataFrame create
# .format() set data ingestion format as Kafka
# .option("substribe", "<topic_name>") => This is required although it says option.
# .option("kafka.bootstrap.servers", " localhost:<port>") You will also need the url and port of the bootstrap server
df = spark_session \
    .readStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("subscribe", "keywordList") \
    .option("startingOffsets", "earliest") \
    .load()


df = df.withColumn('value', col("value").cast("string"))
df = df.withColumn('key', col("key").cast("string"))
df = df.withColumn('offset', col("offset").cast("integer"))
df.printSchema()

tmp = df.writeStream.format('memory').queryName('keywordList').outputMode("append").start()
time.sleep(10)
val = spark_session.sql("SELECT value FROM keywordList").toJSON().map(lambda x: json.loads(x)).collect()
# print(val)

positive = {}
negative = {}
neutrality = {}
unclassified = {}

for i in val:
    k = json.loads(i["value"])
    label = k["label"]
    keywordList = k["keyword"]

    # print(label)
    for keyword in keywordList:
        # print(" " + keyword)
        if label == 0: # negative
            if keyword in negative:
                val = negative.get(keyword) + 1
                negative[keyword] = val
            else:
                negative[keyword] = 1
        elif label == 1: # positive
            if keyword in positive:
                val = positive.get(keyword) + 1
                positive[keyword] = val
            else:
                positive[keyword] = 1
        elif label == 2: # neutrality
            if keyword in neutrality:
                val = neutrality.get(keyword) + 1
                neutrality[keyword] = val
            else:
                neutrality[keyword] = 1
        else:
            if keyword in unclassified:
                val = unclassified.get(keyword) + 1
                unclassified[keyword] = val
            else:
                unclassified[keyword] = 1

print('negative')
print(negative.items())
print('positive')
print(positive.items())
print('neutrality')
print(neutrality.items())
print('unclassified')
print(unclassified.items())


# keyword별 부정/긍정/중립/미분류 합치기
negative_keyword = list(negative.keys())
positive_keyword = list(positive.keys())
neutrality_keyword = list(neutrality.keys())
unclassified_keyword = list(unclassified.keys())

# 리스트 하나로 합치기
total_keyword = negative_keyword + positive_keyword + neutrality_keyword + unclassified_keyword

# SET을 이용하여 중복 제거 => 나온 keyword 추출
keyword_set = set(total_keyword)
total_keyword = list(keyword_set)
# print(total_keyword)

# from here to mysql
# spark = SparkSession\
#     .builder\
#     .config("spark.jars", "/home/hduser/mysql-connector-java-5.1.47/mysql-connector-java-5.1.47.jar") \
#     .master("local")\
#     .appName("PySpark_MySQL_test2")\
#     .getOrCreate()
# keywordRatioDF = spark.createDataFrame()

# time.sleep(300) #5 min



tmp.stop()
