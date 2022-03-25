<https://haerong22.tistory.com/94?category=801599>

<aside>💡 HDFS에 저장된 파일을 읽어와 spark ( java)로 wordcount 작성

</aside>

# 하둡 실행시키기

```
start-dfs.sh

```

# 스파크 실행시키기

```
cd spark-3.1.2/bin 
./spark-shell   

cd spark-3.1.2/sbin
./start-master.sh

jsp => master 도 떠 있는지 확인 

```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/ed249c8a-a14d-4520-9640-df52695826e8/Untitled.png)

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/5cbb0653-3860-4d00-9c73-79a5c006ba81/Untitled.png)

```
./start-slave.sh  {마스터 주소}

./start-slave.sh spark://ubuntu:7077

#skark_env job 2개를 설정한 기준, worker가 2개 뜨면 성공 

```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/664a5276-9e61-4779-bbe5-77877983b800/Untitled.png)

# 이클립스로 wordcount 작성하기

## spring MVC Project 생성

> spring MVC Project가 없다면

**Help → Eclipse Marketplace** 을 클릭하고 **Find** 에서 **STS** 를 검색해서 설치

> 