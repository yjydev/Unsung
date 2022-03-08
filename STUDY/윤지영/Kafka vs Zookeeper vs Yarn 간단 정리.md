## Kafka vs Zookeeper vs Yarn 간단 정리     

1. Kafka는 왜 Zookeeper와 같이 쓰는가?

   - [링크-하둡 에코 시스템 기반 지식 정리](https://qkqhxla1.tistory.com/1118) 

     [링크2-클라우드카프카](https://www.cloudkarafka.com/blog/cloudkarafka-what-is-zookeeper.html) 

   - Zookeeper를 범용 분산 프로세스 조정 시스템으로 활용하기로 선택했습니다. 따라서 Kafka, Storm, HBase, SolrCloud는 모두 Zookeeper를 사용하여 관리 및 조정을 지원합니다.

   - Kafka는 애초에 클러스터 배포용으로 설계된거라서 Zookeeper 없이 standalone으로 사용하면 애초에 용도와 맞지가 않다. 싱글 노드로 Kafka를 사용할거면 이보다 더 다른 좋은 솔루션이 있을것같다.

     그리고 Spark가 standalone이면 hadoop 없이도 사용가능한 반면 Kafka는 반드시 Zookeeper가 필요하다.

2. Yarn vs Zookeeper

   - yarn은 새로운 map reduce 데몬이고, hadoop 클러스터에서 잡들을 처리한다.
   - zookeeper는 분산 환경 시스템을 조율하는 역할을 하며 yarn을 비롯해 많은 데몬들이 사용

   -> 넓게 분산 처리 환경 시스템을 조율하는게 zookeeper,

   yarn은 그중에서도 map reduce잡을 어떻게 더 효율적으로 처리하는 엔진같은 개념인것같다.