import findspark, os, time, tqdm
findspark.find()
findspark.init(os.environ.get("SPARK_HOME"))

os.environ['PYSPARK_SUBMIT_ARGS'] = '--packages org.apache.spark:spark-sql-kafka-0-10_2.12:3.1.2 consumer_producer.py'

from pyspark.sql import *
from pyspark.sql.functions import col, pandas_udf, split, json_tuple
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
off = spark_session.sql("SELECT offset FROM test5").collect()
# tmp.stop()

r = len(val)
print(len(val))
for i in range(r):
    val[i]['offset'] = off[i].offset
    j = json.loads(val[i]['value'])
    j['label'] = labeling(j['title'])
    val[i]['value'] = json.dumps(j)
    
tt = spark_session.createDataFrame(data=val)
tt.show()

dff = df.join(tt, ['offset'], how='inner').drop(df.value)
dff.printSchema()

ds = dff.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)").writeStream.format("kafka")\
    .option("kafka.bootstrap.servers", "localhost:9092")\
    .option("topic","reload1")\
    .option("checkpointLocation", "hdfs://localhost:9000/pyspark_check")\
    .start()
ds.awaitTermination()