## HDFS File READ

- Hadoop 실행 

  ```sql
  # dfs 데몬 실행
  start-dfs.sh 
  ```

- 기존 데이터 읽기    

  - 기존 데이터를 hdfs 내부로 복사

  ```sql
  hdfs dfs -mkdir /wordcount_test
  # wordcount_test 폴더 만들기
  # 리눅스 폴더 접근하는 것 처럼 무조건 / 부터 시작해야 namenode가 읽을 수 있음!!
  
  cd /home/hadoop/Project/data 
  # 넣을 데이터가 있는 경로 접근
  
  hdfs dfs -put wordcount-data.txt wordcount_test
  # wordcount_test 내부에 -put 명령어로 wordcount-data.txt 파일 넣기
  ```

  - HDFS 내부 파일 읽기 

  ```sql
  hdfs getconf -confKey fs.defaultFS
  # namenode 경로 찾기 => 기본적으론 hdfs://localhost:9000
  
  # 파일 읽어오기 위한 Path (java, spark 등 다른 곳에서 파일 읽을 때 필요)
  hdfs://localhost:9000/wordcount_test/wordcount-data.txt
  
  # hdfs는 다른 파일 시스템과 다르게 namenode 경로부터 시작해야함! 
  # hdfs는 namenode에 데이터들의 메타데이터가 있기때문에 namenode를 거치지않고 바로 datanode에서 값을 읽어오는 것은 불가능
  ```

  - spark-shell 에서 읽는다고하면,

    ```sql
    val rdd = spark.sparkContext.textFile(hdfs://localhost:9000/wordcount_test/wordcount-data.txt)
    ```

    처럼 사용 





------

참고 : 

- https://sparkbyexamples.com/spark/spark-read-write-files-from-hdfs-txt-csv-avro-parquet-json/

- https://lockkay.tistory.com/87
- https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=airguy76&logNo=220385579105