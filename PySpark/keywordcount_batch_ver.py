# 이후에 pyspark가 실행되기 때문에 맨 위에 위치하기
import findspark, os, time
from pyspark.sql.functions import col, json_tuple, unix_timestamp, window
from pyspark.sql.types import TimestampType

findspark.find()
findspark.init(os.environ.get("SPARK_HOME"))

os.environ[
    'PYSPARK_SUBMIT_ARGS'] = '--packages org.apache.spark:spark-sql-kafka-0-10_2.12:3.1.2,mysql:mysql-connector-java:8.0.28 keywordcount_ver2.py'

from pyspark.sql import SparkSession
from pyspark import SparkContext

sc = SparkContext()
log4jLogger = sc._jvm.org.apache.log4j
log4jLogger.LogManager.getLogger("org").setLevel(log4jLogger.Level.OFF)
log4jLogger.LogManager.getLogger("akka").setLevel(log4jLogger.Level.OFF)

spark = SparkSession \
    .builder \
    .appName("Python Spark SQL basic example") \
    .config("jars", "/home/hadoop/spark-3.1.2/jars/mysql-connector-java-8.0.28.jar") \
    .getOrCreate()

# Subscribe to 1 topic

# readStream / writeStream : 실시간성으로 데이터를 읽어오고 저장할 수 있다.
# read/write : 배치로 데이터를 읽어오고 저장할 수 있다.

####### kafka producer를 실시간으로 가져올 수 있으나, 실시간으로 mysql에 적을 수 없는 문제로 배치로 데이터를 처리하기로 함.

# format : 읽어올 데이터 저장소 ⇒ 카프카
# subsribe : 카프카 토픽 중 읽고자하는 메세지 큐
# startingOffset : 메세지 큐 안에서 읽기 시작할 포인트
# - earliest : 가장 이른 오프셋에서 가져옴
# - latest : 가장 최근 오프셋을 가져옴
# endingOffsets : 배치 쿼리가 종료될때 끝점
# - default : latest
# kafkaConsumer.pollTimeoutMs : kafka의 데이터를 폴링하는 시간 제한(밀리초)
# - default : 512
# fetchOffset.numRetries : Kafka 오프셋 가져오기를 포기하기 전에 재시도할 횟수
# - default : 3
# fetchOffset.retryIntervalMs : Kafka 오프셋을 가져오기 위해 재시도하기 전에 대기하는 시간(밀리초)
# - default : 10

df = spark \
    .read \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("subscribe", "readKeyword") \
    .option("startingOffsets", "earliest") \
    .option("endingOffsets", "latest")\
    .option("kafkaConsumer.pollTimeoutMs", 512)\
    .option("fetchOffset.numRetries", 3)\
    .option("fetchOffset.retryIntervalMs", 10)\
    .load()

# 안하면 바이트타입이라서인지 다 깨진다
df = df.withColumn('value', col("value").cast("string"))
df = df.withColumn('key', col("key").cast("string"))
df = df.withColumn('offset', col("offset").cast("integer"))
df.printSchema()

keyword = df.select(json_tuple(col('value'), 'press', 'label', 'keyword', 'date').alias('press', 'label', 'keyword', 'date'))

keywordcount = keyword .groupby( 'keyword', 'label', 'press', "date").count()

# MySQL Connection 설정을 위한 변수를 선언
sql_url = "localhost"
user = "root"
password = "ssafy"
database = "unsung"
table = "keywordcount"

db_target_properties = {"user": user, "password": password}

keywordcount\
    .select("keyword", "press", "label", "date", "count") \
    .write.format("jdbc") \
    .option("url", "jdbc:mysql://{}:3306/{}?serverTimezone=Asia/Seoul ".format(sql_url, database))\
    .option("driver", "com.mysql.jdbc.Driver")\
    .option("dbtable", table)\
    .option("user", user)\
    .option("password", password)\
    .mode("overwrite")\
    .save()
keywordcount.show()
