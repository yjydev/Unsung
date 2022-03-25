### zookeeper & kafka 실행

```sql
cd zookeeper
bin/zkServer.sh start

cd kafka
bin/kafka-server-start.sh config/server.properties
# 계속 돌아가므로 new window 로 새 터미널 창 열어서 작업 이어하기
```

- kafka 명렁어   

  - topic 리스트 확인    

    - `./kafka-topics.sh --list --bootstrap-server localhost:9092`

  - topic 생성     

    - ```
      ./kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic {Topic name}
      ```

      - replication factor : 복제본의 갯수, 기본 값은 3으로 하나의 파티션이 총 3개로 분산 저장되는 것이다.
      - —create : 토픽 생성을 하겟다
      - —bootstrap-server : 토픽 생성을 위해 붙을 브로커 주소
      - —partitions : 토픽의 파티션 개수
      - —topic : 생성할 토픽의 이름    



#### producer & consumer   

- topic producer
  - `./kafka-console-producer.sh --broker-list localhost:9092 --topic {보내고 싶은 Topic name}`   
- topic consumer  
  - `./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic {보고 싶은 Topic name} --from-beginning `    



<hr>    



### spark 실행

- hadoop 실행 안된 상태라면, 

  ```sql
  cd spark/sbin
  start-all.sh
  ```

  - dfs, yarn 데몬 한번에 실행 가능    

    ```sql
    jps  
    # namenode, secondarynamenode, datanode, resourcemanager, nodemanager 
    # 실행중인 것을 확인 가능
    ```

- hadoop 실행 된 상태라면,

```sql
cd spark/sbin 
start-master.sh 

start-slave.sh {master URL} 
# master URL 은 web ui 에서 확인 가능 
# default : spark://ubuntu:7077 
```

- web ui `http://localhost:8080`  ⇒ `http://localhost:8081` (설정 변경 필요)

  - 404 not found 가 발생한다면,

    - 기본적으로 zookeeper Admin Server는 포트 8080에 바인딩되어있는데,   

      (내장 Jetty 서버가 수신 대기하는 포트의 default = 8080) => [공식문서](https://zookeeper.apache.org/doc/r3.5.1-alpha/zookeeperAdmin.html#sc_adminserver_config)      

      zookeeper를 먼저 실행하면 zookeeper가 먼저 8080 포트를 선점하게 된다.

      그 상태에서 spark를 실행시키면 spark도 8080포트를 쓰려고 하니까 `404 not found` 발생

    - spark를 먼저 실행시키고 zookeeper를 실행시키려고 한다면,

      포트 8080이 이미 spark에서 사용되고 있기 때문에 zookeeper 실행 불가 에러 발생

    ⇒ zookeeper web ui는 사용하려면 추가 과정 필요 (zk-web 등..)   

    

    - 해결방법   

    1. 포트 죽였다가 다시 실행시키기 (추천 X)

       ```sql
       netstat -ntlp | grep 8080   # 8080 포트 사용중인 프로세스 찾기
       
       kill -9 {pid}
       
       # tcp6     0     0  :::8080   :::*    LISTEN     2160/java  라면,
       # kill -9 2160 
       
       netstat -ntlp | grep 8080 
       # 검색 결과 안나오는 것 확인 후 spark 재실행하면 접속 가능
       ```

       - 이렇게 되면 zookeeper도 종료되고, 그에따라 kafka 연결도 끊김   

    

    2. saprk web ui 포트 번호 변경 (추천)

       a. 설정 파일 변경    

       ```sql
       cd spark-3.1.2/conf
       vi spark-env.sh
       
       # 아래 내용 추가  
       SPARK_MASTER_WEBUI_PORT=8081
       
       # SPARK_MASTER_WEBUI_PORT=<your preferred port>
       ```

       

       b. master 실행할 때 옵션 추가 

       ```sql
       /sbin/start-master.sh --webui-port 8081
       # /sbin/start-master.sh --webui-port PORT
       ```

       

    3. zookeeper master server 포트 변경     

       a. 설정 파일 변경

       ```sql
       cd zookeeper/conf
       vi zoo.cfg
       
       admin.serverPort=9876 
       # 8080 이외의 포트
       
       # 관리 서버 완전 비활성화 
       # admin.enableServer=false
       ```

       ```sql
       admin.serverPort
       (자바 시스템 속성: zookeeper.admin.serverPort )
       
       내장 Jetty 서버가 수신 대기하는 포트입니다. 기본값은 8080입니다.
       ```

       

    

​						

