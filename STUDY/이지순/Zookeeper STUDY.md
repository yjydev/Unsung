## Zookeeper란?

분산 코디네이션 서비스를 제공하는 오픈소스 프로젝트

 분산 시스템을 설계 하다 보면, 가장 문제점 중의 하나가 분산된 시스템간의 정보를 어떻게 공유할 것이고, 클러스터에 있는 서버들의 상태를 체크할 필요가 있으며 또한, 분산된 서버들간에 동기화를 위한 락(lock)을 처리하는 것들이 문제로 부딪힌다.

 이러한 문제를 해결하는 시스템을 코디네이션 서비스 시스템 (coordination service)라고 하는데, Apache Zookeeper가 대표적이다.

* 설정 관리(Configuration management) : 클러스터의 설정 정보를 최신으로 유지하기 위한 조율 시스템으로 사용됩니다.
* 클러스터 관리(Cluster management) : 클러스터의 서버가 추가되거나 제외될 때 그 정보를 클러스터 안 서버들이 공유하는 데 사용됩니다.
* 리더 채택(Leader selection) : 다중 어플리케이션 중에서 어떤 노드를 리더로 선출할 지를 정하는 로직을 만드는 데 사용됩니다. 주로 복제된 여러 노드 중 연산이 이루어지는 하나의 노드를 택하는 데 사용됩니다.
* 락, 동기화 서비스(Locking and synchronization service) : 클러스터에 쓰기 연산이 빈번할 경우 경쟁상태에 들어갈 가능성이 커집니다. 이는 데이터 불일치를 발생시킵니다. 이 때, 클러스터 전체를 대상을 동기화해( 락을 검 ) 경쟁상태에 들어갈 경우를 사전에 방지합니다.

## Zookeeper가 사용되는 서비스

* Apache HBase - Hadoop에서 사용되는 HBase는 클러스터 마스터(cluster master)를 선출하기 위해 주키퍼 사용, 현재 이용 가능한 서버가 어떤 것들이 있는지에 대한 정보를 저장하고, 클러스터의 메타 데이터를 보관하는데 쓰인다.
* Apache Kafka - 메시징 시스템으로 널리 쓰이는 카프카(Kafka)는 카프카 서버의 크래시를 감지하기 위해 사용되며 새로운 토픽이 생성되었을 때, 토픽의 생성과 소비에 대한 상태를 저장하기 위해 주키퍼를 도입했다.
* Facebook Messages - 이메일, SMS, 페이스북 챗 등을 통합한 서비스인 페이스북 메시지는 샤딩(sharding)과 페일오버(failover) 컨트롤러의 구현을 위해 주키퍼를 사용했다.

아파치 카프카를 관리하기 위해서는 반드시 필요

서버는 홀수개로 구성해야함

## 구성

* znode: 파일과 디렉터리를 통합한 노드 제공
* znode로 불리는 노드를 계층적 트리 형태로 관리
* znode는 루트(/) 노드를 시작으로 계층적인 네임스페이스 형성
* 호스트명이나 IP주소와 같은 멤버 정보가 각각의 znode에 저장된다.
* 대용량의 데이터를 저장하는 용도로는 사용할 수 없으며 znode에 저장할 수 있는 데이터의 크기는 1MB로 제한되어있다.
* 데이터 접근은 원자성(성공 또는 실패)을 가지며 클라이언트가 znode에 저장된 데이터를 읽을 때 데이터의 일부만 받을 수 없으며 데이터 전체가 전달되지 않으면 읽기는 결국 실패할 것.
    
    ⇒ 쓰기를 성공과 실패로만 결정
    
* 주키퍼의 각 znode는 경로에 참조 된다. (항상 절대경로로 참조)

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/39ee42de-e3e0-4c37-9fd4-abb6856ab2f0/Untitled.png)

* 읽기는 follower가 쓰기는 대표가 커밋함

## Zookeeper 실행 테스트

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/9e91d03f-074b-4a0e-8d3a-5bb1e81bce9e/Untitled.png)

* 서버 한대만 주키퍼를 실행시켰을 때 에러 로그 출력 → 과반수 이상이 실행되어야 정상 실행으로 판단하기 때문이다.
* 2개 이상 서버가 올라가는 순간 에러 로그가 뜨지 않음

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/69e93890-4e97-4895-9daa-4a8332f3c314/Untitled.png)

* 서버 하나 종료 시켰을 때

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/6dcd9b90-32e9-44aa-8e27-a589d94bf6ab/Untitled.png)

* 상태 확인
![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/b3c2ce68-88fe-4f01-9f4b-fde46c4a9db7/Untitled.png)