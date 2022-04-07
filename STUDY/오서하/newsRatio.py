# 이후에 pyspark가 실행되기 때문에 맨 위에 위치하기
import findspark, os, time

findspark.find()
findspark.init(os.environ.get("SPARK_HOME"))

os.environ['PYSPARK_SUBMIT_ARGS'] = '--packages org.apache.spark:spark-sql-kafka-0-10_2.12:3.1.2,mysql:mysql-connector-java:8.0.28 newsRatio.py'

# Spark에 연결하기
from pyspark.sql import *
from pyspark.sql.functions import col
import json

from pyspark import SparkContext

sc = SparkContext()
log4jLogger = sc._jvm.org.apache.log4j
log4jLogger.LogManager.getLogger("org").setLevel(log4jLogger.Level.OFF)
log4jLogger.LogManager.getLogger("akka").setLevel(log4jLogger.Level.OFF)

spark_session = SparkSession \
    .builder \
    .appName("pyspark_kafka5") \
    .getOrCreate()

# DataFrame create
df = spark_session \
    .read \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("subscribe", "keywordList") \
    .option("startingOffsets", "earliest") \
    .load()

df = df.withColumn('value', col("value").cast("string"))
df = df.withColumn('key', col("key").cast("string"))
df = df.withColumn('offset', col("offset").cast("integer"))
df.printSchema()

# tmp = df.write.format('memory').queryName('keywordList').outputMode("append").start()
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
        if label == 0:  # negative
            if keyword in negative:
                val = negative.get(keyword) + 1
                negative[keyword] = val
            else:
                negative[keyword] = 1
        elif label == 1:  # positive
            if keyword in positive:
                val = positive.get(keyword) + 1
                positive[keyword] = val
            else:
                positive[keyword] = 1
        elif label == 2:  # neutrality
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

# keyword별 부정/긍정/중립/미분류 dataframe 합치기
import pandas as pd
from datetime import datetime

negative_keyword = list(negative.items())
positive_keyword = list(positive.items())
neutrality_keyword = list(neutrality.items())
unclassified_keyword = list(unclassified.items())

negative_df = pd.DataFrame(negative_keyword, columns=['keyword', 'negative_cnt'])
positive_df = pd.DataFrame(positive_keyword, columns=['keyword', 'positive_cnt'])
neutrality_df = pd.DataFrame(neutrality_keyword, columns=['keyword', 'neutrality_cnt'])
unclassified_df = pd.DataFrame(unclassified_keyword, columns=['keyword', 'unclassified_cnt'])

keyword_df = pd.merge(negative_df, positive_df, on='keyword')
keyword_df = pd.merge(keyword_df, neutrality_df, on='keyword')
keyword_df = pd.merge(keyword_df, unclassified_df, on='keyword')

# total cnt
keyword_df['total_cnt'] = keyword_df['negative_cnt'] + keyword_df['positive_cnt'] + keyword_df['neutrality_cnt'] + \
                          keyword_df['unclassified_cnt']

# 오늘 날짜
today = datetime.today().strftime('%Y-%m-%d')
keyword_df['date'] = today

print(keyword_df)

# MySQL Connection 설정을 위한 변수를 선언
sql_url = "localhost"
user = "root"
password = "ssafy"
database = "unsung"
table = "news_analyze"

# #  connector를 내장하고있는 SparkContext를 생성
# # .set함수를 통해 mysql connector를 지정
sparkToMysqlSession = SparkSession \
    .builder \
    .config("jars", "/home/hadoop/spark-3.1.2/jars/mysql-connector-java-8.0.28.jar") \
    .master("local") \
    .appName("PySpark_MySQL_test") \
    .getOrCreate()

keywordRatioDF = sparkToMysqlSession.createDataFrame(keyword_df)
keywordRatioDF.show()


# # # &useUnicode=true&characterEncoding=UTF-8&useSSL=false
keywordRatioDF\
    .select("keyword", "negative_cnt", "positive_cnt", "neutrality_cnt", "unclassified_cnt", "total_cnt", "date") \
    .write.format("jdbc") \
    .option("url", "jdbc:mysql://{}:3306/{}?serverTimezone=Asia/Seoul ".format(sql_url, database))\
    .option("driver", "com.mysql.jdbc.Driver")\
    .option("dbtable", table)\
    .option("user", user)\
    .option("password", password)\
    .mode("overwrite")\
    .save()

keywordRatioDF.show()

tmp.stop()