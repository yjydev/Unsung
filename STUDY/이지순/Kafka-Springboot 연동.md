### Kafka, Springboot 연동
1. ubuntu 내에 intellij 설치
2. zookeeper & kafka 설치
    * [참고] https://eyeballs.tistory.com/210
    ![image](https://user-images.githubusercontent.com/46081043/157643553-78fb6361-ecbd-42b6-addc-b0e592c409c4.png)
3. springboot kafka 연동
    * [참고] https://oingdaddy.tistory.com/308
    * [참고] https://deep-jin.tistory.com/entry/kafka-topic-message-test 
    ![image](https://user-images.githubusercontent.com/46081043/157643733-5ad06f69-0512-442f-a450-c5b0e26733c1.png)


### Kafka 명령어
* kafka 실행
    ```bin/kafka-server-start.sh config/server.properties```
* topic 리스트 확인
    ```./kafka-topics.sh --list --bootstrap-server localhost:9092```
* consumer
    ```./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic [topic name]```