# Kafka
- [X] Kafka producer 크롤링 연결
- [X] 크롤러 2개 이상 띄우고 consumer 확인
- [X] Kafka topic - Spark streaming 연결
- [ ] Kafka partition
- [ ] Spark streaming HDFS


### config/server.properties 설정 상세
```Plain Text
broker.id : 정수로 된 브로커 번호. 클러스터 내 고유번호로 지정
listeners : kafka 통신에 사용되는 host:port
advertised.listeners : Kafka client가 접속할 host:port
log.dirs : 메시지를 저장할 디스크 디렉토리. 세그먼트가 저장됨
log.segment.bytes : 메시지가 저장되는 파일의 크기 단위
log.retention.ms : 메시지를 얼마나 보존할지 지정. 닫힌 세그먼트를 처리
zookeeper.connect : 브로커의 메타데이터를 저장하는 주키퍼의 위치
auto.create.topics.enable : 자동으로 토픽이 생성여부
num.partitions : 자동생성된 토픽의 default partition 개수
message.max.bytes : Kafka broker에 쓰려는 메시지 최대 크기
```

### Kafka Partition
 기존 파티션의 개수 1개 -> 파티션과 컨슈머가 하나라면 성능상 부족
 * Topic의 분할 단위
    * partition이 여러개일 경우 producer가 보낸 메시지의 순서는 보장 될 수 없지만, 각 partition안에서의 메세지 순서가 보장된다.
    ![image](https://user-images.githubusercontent.com/46081043/159642606-e651e541-4917-424a-97d0-68f6e0466789.png)
* 하나의 토픽에 여러개의 파티션을 나눠서 쓰는 이유?
    * 몇 천건의 메시지가 동시에 카프카에 쓰여진다고 생각했을 때 하나의 파티션에 순차적으로 append 될텐데 처리하는게 버거움 => 여러개의 파티션 두어 분산저장
    * 주의
        * 한 번 늘린 파티션은 절대로 줄일 수 없기 때문에 파티션 늘리는 것 항상 고려해봐야함
        * Round-robin 방식이므로 순차적으로 메세지가 쓰여지지 않음, 증권시스템의 경우 순차 소비를 보장해주지 않기 때문에 위험
* Consumer Group
    * 파티션 증가시 Consumer의 개수도 고려
        * 개수 같이 맞춰주는 것 권장
        * 메세지가 쌓이는 속도보다 처리하는 속도가 빠르다면 파티션 >= 컨슈머
        ![image](https://user-images.githubusercontent.com/46081043/159649552-1bf07401-d568-4f0f-ac77-d8693f0dfd05.png)
    * consumer group은 하나의 topic에 대한 책임 가지고 있다.

### offset
* Topic 내 각 파티션에 존재하는 offset의 위치를 통해서 이전에 소비했던 offset의 위치를 기억하고 관리하고 이를 통해서, 혹시 Consumer가 죽었다가 다시 살아나도, 전에 마지막으로 읽엇던 위치에서 부터 다시 읽어들일 수 있다
    * -> fail-over에 대한 신뢰 존재

##### 참조
https://medium.com/@umanking/%EC%B9%B4%ED%94%84%EC%B9%B4%EC%97%90-%EB%8C%80%ED%95%B4%EC%84%9C-%EC%9D%B4%EC%95%BC%EA%B8%B0-%ED%95%98%EA%B8%B0%EC%A0%84%EC%97%90-%EB%A8%BC%EC%A0%80-data%EC%97%90-%EB%8C%80%ED%95%B4%EC%84%9C-%EC%9D%B4%EC%95%BC%EA%B8%B0%ED%95%B4%EB%B3%B4%EC%9E%90-d2e3ca2f3c2