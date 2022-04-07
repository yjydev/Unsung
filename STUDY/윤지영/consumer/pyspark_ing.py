# 이후에 pyspark가 실행되기 때문에 맨 위에 위치하기
import findspark, os, time, tqdm
findspark.find()
findspark.init(os.environ.get("SPARK_HOME"))

os.environ['PYSPARK_SUBMIT_ARGS'] = '--packages org.apache.spark:spark-sql-kafka-0-10_2.12:3.1.2 main.py'

# Spark에 연결하기
from pyspark import SparkContext
from pyspark.streaming import StreamingContext
import pyspark.sql.functions as F
from pyspark.sql.types import *
from pyspark.sql import *
from pyspark.sql.functions import col, pandas_udf, split, json_tuple
# from pyspark.streaming.kafka import KafkaUtils
import json

import numpy as np


from model_variable import test_sentences, softmax

def labeling(title):
    logits = test_sentences([title])
    res = softmax(logits)[0]
    if max(res) >= 0.65:
        return str(np.argmax(logits))
    else:
        return '3'

# labeling_udf = F.udf(lambda x: labeling(x), ArrayType(StringType()))
# labeling_udf = F.udf(labeling, StringType())

# sc = SparkContext(appName="Kafka Spark Demo")  # Spark 실행 및 RDD 생성 기능
# ssc = StreamingContext(sc, 5)  # 5초 간격 배치로 스트리팅 데이터 가져오기
#
# # Kafka에서 가져오는 메시지형태로 이루어진 트윗 데이터를 TransformedDStream 타입으로 반환
# message = KafkaUtils.createDirectStream(ssc, topics=["search_word"],
#                                         kafkaParams={"metadata.broker.list": "localhost:9092"})
#
# print(message)
# ssc.start()  # 실제 스트리밍 app을 실행시키는 코드
# ssc.awaitTermination()  # 강제 종료 or stop()코드 발견 전까지 실행 유지

# kafka_bootstrap_servers = 'your kafka bootstrap servers'
# topic = 'your kafka topic'
# df = spark \
#     .readStream\
#     .format("kafka") \
#     .option("kafka.bootstrap.servers",
#             kafka_bootstrap_servers) \
#             .option("subscribe", topic) \
#     .load()

# KAFKA_TOPIC = "search"
# KAFKA_SEVER = "localhost:9092"

spark_session = SparkSession \
        .builder \
        .appName("pyspark_kafka5")\
        .getOrCreate()
        # .master("spark://ubuntu:7077")\
        # .getOrCreate()

    # run_spark_job(spark)
    #
    # spark.stop()


df = spark_session \
    .readStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("subscribe", "test5") \
    .option("startingOffsets", "earliest") \
    .load()
    # .option("maxOffsetsPerTrigger", 10) \
    # .option("stopGracefullyOnShutdown", "true") \


# da = df.select('value')

# df.withColumn('value2', col('value').map(lambda x: x.decode('utf-8')))
# rd = df.rdd.map(lambda x: (x.value.decode('utf-8')))
# dff = df.writeStream.foreach(lambda x: (x.value[0].decode('utf-8'))).format('console').start()
# toStr = udf((payload: Array[Byte]) => )

df = df.withColumn('value', col("value").cast("string"))
df = df.withColumn('key', col("key").cast("string"))
df = df.withColumn('offset', col("offset").cast("integer"))
df.printSchema()

# dd = df.select(json_tuple(col("value"),"title").alias("title")).writeStream.format('console').start()

# dd = df.select(json_tuple(col("value"),"title").alias("title")).writeStream.format('memory').queryName("pyspark_title").start()
# df2 = rd.toDF(["key", "value","topic","partition","offset","timestamp","timestampType"])
# df2.show()
# dd = df.select('value').writeStream.format('console').start()
# dd = df.select('value').writeStream.foreach(lambda x: (x['title'])).format('console').start()

# class NpEncoder(json.JSONEncoder):
#     def default(self,obj):
#         if isinstance(obj, np.)

# if test, outputMode not set => 중복 데이터 계속 쌓임?

tmp = df.writeStream.format('memory').queryName('test5').outputMode("append").start()
time.sleep(10)
val = spark_session.sql("SELECT value FROM test5").toJSON().map(lambda x: json.loads(x)).collect()
off = spark_session.sql("SELECT offset FROM test5").collect()
tmp.stop()
# print(spark_session.sql("SELECT offset FROM test5").collect())
# type(val[0]) => dict
# type(val) => list
# val => [ { 'value' : '{"time": ..., "title": ...}' }, { ... } , ... , { ... } ]
# len(val) => 7760 // 8260 // 8660 (for : about 32 min)
r = len(val)
print(len(val))
for i in range(r):
    val[i]['offset'] = off[i].offset
    j = json.loads(val[i]['value'])
    j['label'] = labeling(j['title'])
    val[i]['value'] = json.dumps(j)
    # print(val[i]['value'])
# print(val)
# spark_session.sql("UPDATE test5 SET value = ")

tt = spark_session.createDataFrame(data=val)
tt.show()

# print(type(tt))
# print(tt.dtypes)
# tt.printSchema()
# tt.show()

# df = df.withColumn('value', tt.value)
# df = df.union(tt)  => col num different

# df.drop('value')
# df.printSchema()
df.drop(df.value)
df.printSchema()
dff = df.join(tt, ['offset'], how='inner')
dff.printSchema()

# df.withColumn('value',).where()

# df = df.withColumn('value', tt['value'])
# df.show()

# ds = df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)").writeStream.format("kafka")\
#     .option("kafka.bootstrap.servers", "localhost:9092")\
#     .option("topic","reload1")\
#     .start()
# ds.awaitTermination()

# j = json.loads(val[0]['value'])
# print(j)
# j['label'] = labeling(j['title'])
# j = json.dumps(j)

# spark_session.createDataFrame(j).show()
# print(val)

# ds =



# a = df.select(json_tuple(col('value'), "title", "content", "url", "time"))
# print(a.toJSON())
# print(a.dtypes)
# for c in a:
#     print(c.show(), type(a))
# print(a.select(a.c0).dtypes)
# b = a.map(lambda x: json.loads(x))

# print(type(b), b)


# dd = df.select(json_tuple(col("value"), "title","content","url","time").alias("title","content","url","time"))\
#                 .withColumn('label', labeling_udf(col("title"))).writeStream.format('console').start()

# dd = df.select(json_tuple(col("value"), "title","content","url","time").alias("title","content","url","time"))\
#                 .withColumn('label', labeling_udf(col("title")))
# dd = df.select(json_tuple(col("value"), "title","content","url","time").alias("title","content","url","time"))\
#                 .withColumn('label', labeling_udf(col("title"))).writeStream.format('memory').queryName("test").outputMode("append").start()
# # show() => streaming query X
# # dd.awaitTermination()
# time.sleep(10)
# dd.stop()
# spark_session.sql("SELECT * FROM test").show()





# dd = df.writeStream.format('console').start()
# title = df.selectExpr("value['title']")
# title.show()
# df = df.withColumn('label', col("value"). labeling_udf(json_tuple(col("value"),"title")))
# df.printSchema()
# dfff = label.writeStream.format('console').start()
# label = df.rdd.map(lambda x : (labeling(x.value)))


# time.sleep(10)
# title.stop()
# dfff.stop()
# dd3.stop()
# dff.stop()

# spark_session.sql("SELECT * FROM pyspark_title").show()
# dd.stop()

# df.write.csv("hdfs://localhost:9000/user/spark_tmp")

# log = df.select('value')\
#     .writeStream\
#     .format("parquet")\
#     .option("path","hdfs://localhost:9000/user/spark_tmp")\
#     .option("checkpointLocation", "hdfs://localhost:9000/streamcheckpoint")\
#     .start()
#
# log.awaitTermination(30000)

# query = df.selectExpr("CAST(value AS STRING)") \
#     .writeStream \
#     .format("console") \
#     .option("truncate", "false") \
#     .start()
#
# query.awaitTermination()

# query2 = df.selectExpr("CAST(value AS STRING)") \
#     .writeStream \
#     .format("parquet") \
#     .outputMode("append") \
#     .option("checkpointLocation", "/check") \
#     .option("path", "/test") \
#     .start()
#
# query2.awaitTermination()



# 콘솔에 결과를 바로 출력
# consoleSink = df \
#     .writeStream \
#     .queryName("kafka_spark_console")\
#     .format("console") \
#     .option("truncate", "false") \
#     .start()
    # 메세지 길이를 줄이지 않고 모두 출력

# 메모리에 저장
# memorySink = df \
#     .writeStream \
#     .queryName("kafka_spark_memory2") \
#     .format("memory") \
#     .start()
#
# # memorySink.awaitTermination()
#
# # # 메모리에 저장한 결과 확인
# spark_session.sql("SELECT * FROM kafka_spark_memory").show()
# memorySink.stop()
#
# # consoleSink.awaitTermination()
# # memorySink.awaitTermination()
#
# memorySink.stop()


# Show schema for the incoming resources for checks
# df.printSchema()
# print(df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)"))
# df.cache()
# print(df.collect())

# spark-submit --packages org.apache.spark:spark-sql-kafka-0-10_2.12:3.1.2 spark-kafka_consumer.py

