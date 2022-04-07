import findspark, os, time, tqdm
findspark.find()
findspark.init(os.environ.get("SPARK_HOME"))

os.environ['PYSPARK_SUBMIT_ARGS'] = '--packages org.apache.spark:spark-sql-kafka-0-10_2.12:3.1.2 consumer_producer.py'

from pyspark.sql import *
from pyspark.sql.functions import col, pandas_udf, split, json_tuple
import json

import numpy as np

# INFO 로그 안보이게
from pyspark import SparkContext

sc = SparkContext()
log4jLogger = sc._jvm.org.apache.log4j
log4jLogger.LogManager.getLogger("org").setLevel(log4jLogger.Level.OFF)
log4jLogger.LogManager.getLogger("akka").setLevel(log4jLogger.Level.OFF)

from model_variable import test_sentences, softmax

def labeling(title):
    logits = test_sentences([title])
    res = softmax(logits)[0]
    if max(res) >= 0.65:
        return str(np.argmax(logits))
    else:
        return '3'


spark_session = SparkSession \
        .builder \
        .appName("pyspark_producer")\
        .getOrCreate()


df = spark_session \
    .readStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("subscribe", "test5") \
    .option("startingOffsets", "earliest") \
    .load()

df = df.withColumn('value', col("value").cast("string"))
df = df.withColumn('key', col("key").cast("string"))
df = df.withColumn('offset', col("offset").cast("integer"))
df.printSchema()

tmp = df.writeStream.format('memory').queryName('test5').outputMode("append").start()
time.sleep(10)
val = spark_session.sql("SELECT value FROM test5").toJSON().map(lambda x: json.loads(x)).collect()



class Watcher:
    """ variable을 감시하는 간단한 클래스 """

    def __init__(self, value):
        self.variable = value

    def set_value(self, new_value):
        if self.variable != new_value:
            self.pre_change()
            self.post_change(new_value)
            self.variable = new_value


    def pre_change(self):
        pass  # variable이 변화되기 전의 행동을 구현

    def post_change(self, new_value):
          # variable이 변환된 후의 행동을 구현
        for i in range(self.variable, new_value):
            print('change')
            # print(val[i])
            val[i]['offset'] = off[i].offset
            j = json.loads(val[i]['value'])
            j['label'] = labeling(j['title'])
            val[i]['value'] = json.dumps(j)

        tt = spark_session.createDataFrame(data=val)
        tt.show()

        dff = df.join(tt, ['offset'], how='inner').drop(df.value)
        dff.printSchema()

        ds = dff.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)").writeStream.format("kafka") \
            .option("kafka.bootstrap.servers", "localhost:9092, localhost:9093, localhost:9094") \
            .option("topic", "reload4") \
            .option("checkpointLocation", "hdfs://localhost:9000/pyspark_check") \
            .start().awaitTermination()

off = spark_session.sql("SELECT offset FROM consum1").collect()

watch = Watcher(len(val))
if len(val):
    for i in range(len(val)):
        # print(val[i])
        val[i]['offset'] = off[i].offset
        j = json.loads(val[i]['value'])
        j['label'] = labeling(j['title'])
        val[i]['value'] = json.dumps(j)

    tt = spark_session.createDataFrame(data=val)
    tt.show()

    dff = df.join(tt, ['offset'], how='inner').drop(df.value)
    dff.printSchema()

    ds = dff.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)").writeStream.format("kafka") \
            .option("kafka.bootstrap.servers", "localhost:9092, localhost:9093, localhost:9094") \
            .option("topic", "reload5") \
            .option("checkpointLocation", "hdfs://localhost:9000/pyspark_check") \
            .start()

while True:
    val = spark_session.sql("SELECT value FROM consum1").toJSON().map(lambda x: json.loads(x)).collect()
    watch.set_value(len(val))