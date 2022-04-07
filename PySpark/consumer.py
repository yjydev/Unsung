import findspark, os, time, tqdm
findspark.find()
findspark.init(os.environ.get("SPARK_HOME"))

os.environ['PYSPARK_SUBMIT_ARGS'] = '--packages org.apache.spark:spark-sql-kafka-0-10_2.12:3.1.2 main.py'


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

spark_session = SparkSession \
    .builder \
    .appName("pyspark_kafka_consum") \
    .getOrCreate()

df = spark_session \
    .readStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("subscribe", "reload2") \
    .option("startingOffsets", "latest") \
    .load()

df = df.withColumn('value', col("value").cast("string"))
df = df.withColumn('key', col("key").cast("string"))
df = df.withColumn('offset', col("offset").cast("integer"))
df.printSchema()

tmp = df.writeStream.format('memory').queryName('consume').outputMode("append").start()
# time.sleep(10)
val = spark_session.sql("SELECT value FROM consume").toJSON().map(lambda x: json.loads(x)).collect()
# tmp.stop()
print(val)
r = len(val)
print(len(val))
for i in range(r):
    print(val[i])

