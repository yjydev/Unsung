### YARN 과 MapReduce

- Hadoop이 처음 나왔을 땐, Hadoop 이라는 `하나의 프로젝트` 내에 `HDFS + MapReduece`라는 자바의 패키지로만 분리되어 있었음

- 그러다가, Hadoop이 많은 곳에 사용되고 하나의 클러스터가 수천대 이상으로 구성되며 추가 요구사항 발생

  - 추가 요구사항
    - 하나의 Hadoop 클러스터는 다수의 서버로 구성되어 많은 컴퓨팅 리소스를 가지고 있는데 이것을 하나의 컴퓨팅 플랫폼(MapReduce) 에만 할당하는 것은 비효율적이다.
      - MapReduce 이외에 Spark, Strom 등과 같은 다른 컴퓨팅 플랫폼이 출현하였고 이들을 사용하고자 하는 요구사항도 늘어 났음
    - 여러 개의 컴퓨팅 플랫폼을 동시에 실행할 경우 각 서버의 리소스(주로 메모리)가 부족하여 정상적으로 수행되던 작업들이 다른 작업에 의해 문제가 발생하게 된다.
      - MapReduce 이외에 다른 클러스터를 구성할 경우 클러스터 전체의 사용 효율이 떨어지는 경우가 많음

- 추가 요구사항 충족을 위해 기능적으론 간단하지만 `범용적인 분산 리소스 관리 시스템` 인 YARN이 등장하고 MapReduce도 아래와 같이 변경됨

  `MapReduce`는 `컴퓨팅을 위한 프로그램`만 제공,

  `YARN`은 `클러스터의 리소스 관리, 장애 관리 등`을 관리     



### `기존 Map Reduce 구성`    

- MapReduce라는 프로그램 모델을 추상화시킨 라이브러리    
  - Mapper, Reducer 등 클래스로 별도 main() 메소드로 제공되는 클래스는 없음   
- 분산된 서버에 사용자가 만든 MapReduce 프로그램을 실행시키는 Runtime 환경    
  - **JobTracker, TaskTracker, TaskRunner 등과 같이 분산된 서버에 클러스터 환경을 구성 (⇒ 추후 Yarn으로 분리) **   
  - 사용자의 MapReduce 프로그램을 이들 환경에서 실행시키고, 서버 장애 발생 시 수행되던 작업을 다른 서버에 재시작 시키는 등의 역할을 수행    
  - MapReduce 프로그램을 위한 중간 단계 데이터의 이동(Shuffle), 이동된 데이터의 정렬(Sort) 등의 작업을 수행    
- MapReduce runtime 환경에 사용자가 만든 프로그램의 실행을 지원하는 라이브러리    
  - JobClient, JobConf 등 클래스과 유틸리티 클래스
  - 사용자가 만든 main() 내에서 JobTracker로 작업을 실행하게 요청하거나 작업의 상태 정보 등을 확인하기 위한 유틸리티 클래스    



### `기존 Map Reduce 프로그램 실행 절차`

1. MapReduce 클러스터 실행

   1. 한번 실행된 클러스터는 특별한 문제가 없으면 재시작 되지 않으며 여러 MapReduce 작업이 실행되어 있는 클러스터를 이용

2. 사용자가 Mapper, Reducer 클래스 개발

3. MapReduce 작업에서 사용하는 Mapper Reducer 클래스에 대한 지정, 입력, 출력 디렉토리 설정, 작업 관련 다양한 옵션을 지정하는 Driver 프로그램 개발(Driver 프로그램이 자바의 main() 메소드를 가지고 있음)

4. 사용자가 만든 Driver 프로그램 실행

   Hadoop에서 제공해주는 명령어인 bin/mapred 명령어는 클래스패스 등을 설정해주는 부가 기능만 제공하며 실제 메인 클래스는 사용자가 만든 Driver 클래스

5. Driver 프로그램 내에서는 Job 클래스를 이용하여 JobTracker에 작업 실행 요청

6. JobTracker는 해당 작업의 환경 설정 정보와 입력 데이터의 HDFS 서버의 위치, TaskTracker의 상태 정보를 이용하여 작업 스케줄링

7. TaskTracker는 자신에게 할당된 Task를 JobTracker에서 가져와 실행

   - Task Tracker가 Task 실행시킬 때 진행되는 절차

     1. TaskTracker가 Task 실행 시 TaskTrasker의 쓰레드로 실행하지 않고 별도의 프로세스로 fork 시켜서 실행

        **a.** Fork 시에는 main() 메소드를 포함하고 있는 클래스가 있어야 하는데 이때 사용되는 클래스가 MapRunner, ReduceRunner로 별도의 프로세스로 실행된 후 입력 데이터를 읽어 사용자가 개발한 map(), reduce() 함수를 호출해준다

        **b.** MapReduce가 계속 변화되었기 때문에 MapRunner, ReduceRunner라는 클래스명은 정확하지 않을 수 있다.

     2. Reduce는 Map 결과를 받기 위해 각각의 TaskTracker로 Map Task의 결과 정보를 요청하여 이 데이터를 로컬 파일로 저장

     3. 위에서 로컬 파일로 저장한 Map 결과를 Reduce Task가 정렬한 후 reduce() 함수 실행



8. 각 Task 가 종료될 때 마다 TaskTrakcer는 다음 Task를 JobTraker로 부터 가져와서 실행

9. JobTracker는 특정 서버(TaskTracker) 장애시 또는 특정 Task 장애시 재 스케줄링 수행     



### 기존 MapReduce 문제점

- YARN 이전엔 위의 과정이 MapReduce 클러스터 내에서 `모두 수행`됨

  ⇒ Hadoop 클러스터의 서버에서 컴퓨팅에 필요한 리소스 상태(각 서버의 상태)는 MapRedue의 Master 서버 역할을 수행하는 `JobTracker에 의해 관리`됨

  ⇒ JobTracker에 의해 리소스가 관리되기 때문에 Hadoop이 설치된 클러스터 서버들의 리소스를 사용하고자 하는 다른 컴퓨팅 클러스터와 `연동하기 어려운 문제 존재`

- 위의 문제를 해결하기 위해 `YARN이 등장`하였고, YARN은 기존 MapReduce 중, `클러스터의 리소스를 관리하는 부분만` 가져와서 `다른 서비스에서도 사용 가능`하도록 구성한 시스템

  = 즉, 기능적으론 간단하지만 `범용적인 분산 리소스 관리 시스템`      



### YARN 구성

- 핵심 구성요소 : `ResourceManager`, `NodeManager`

- **ResourceManager**

  - YARN 클러스터의 `Master 서버`로 하나 또는 이중화를 위해 두개의 서버에만 실행됨
  - 클러스터 전체의 `리소스를 관리`
  - YARN 클러스터의 리소스를 사용하고자 하는 다른 플랫폼으로부터 요청을 받아 리소스 할당(스케줄링)

- **NodeManager**

  - YARN 클러스터의 `Worker 서버`로 ResourceManager를 제외한 모든 서버에 실행
  - 사용자가 요청한 프로그램을 실행하는 Container를 fork 시키고 Container를 모니터링 Container 장애 상황 또는 Container가 요청한 리소스보다 많이 사용하고 있는지 감시(요청한 리소스보다 많이 사용하면 해당 Container를 kill 시킴)

- 외부의 특별한 요청이 없으면 다른 동작은 수행하지 않고

  **NodeManager**는 자신의 상태만 계속 **ResourceManager** 에게 전송,

  **ResourceManager**는 클러스터의 전체 리소스 상황에 대한 현황 관리만 수행

  = **YARN 자체만으론 할 수 있는 것이 아무것도 없으며, YARN을 사용하는 `분산 컴퓨팅 플랫폼` / `분산된 환경에서의 컴퓨팅 리소스(CPU, 메모리 등)가 필요한 클러스터 기반 시스템`이 있어야 함**

  - 분산 컴퓨팅 플랫폼 ⇒ MapReduce, Spark 등..
  - 분산된 환경에서의 컴퓨팅 리소스가 필요한 클러스터 기반 시스템 ⇒ HBase 등..     



### YARN에서 MapReduce 동작

- MapReduce가 YARN을 최초로 사용한 시스템이었으며 같은 Hadoop 프로젝트 내에 엮여있어 YARN을 가장 잘 사용하고 있음

  ⇒ **YARN의 동작 방식을 이해하기에 가장 적합**

- 절차

  1. 기존 MapReduce 프로그램과 동일하게 `Mapper, Reducer, Driver 클래스` 작성

  2. **Driver 클래스 실행**

     main 프로그램이기 때문에 bin/mapred 명령어를 이용하여 실행

  3. Driver 클래스내에서 Job 클래스를 이용하여 MapReduce작업을 요청하는 것이 아니라 `MapReduce 클러스터를 구성하는 작업`을 먼저 수행

     기존 MapReduce는 한번 구성된 클러스터를 영구적으로 사용하지만 YARN 환경에서는

     `Job 별로 클러스터를 구성`

  4. MapReduce 클러스터를 구성하기 위해서는 기존의 JobTracker 역할을 수행하는 Master가 필요 이를 위해 `YARN의 ResourceManager에게 리소스 요청`

     YARN에서는 각각의 컴퓨팅 클러스터를 Application이라고 하며 **Application을 실행하기 위해 필요한 Master 서버**를 `ApplicationMaster` 라고 한다.

     따라서 `기존의 JobTracker`는 YARN 입장에서 보면 **MapReduce 프로그램을 실행하기 위한 ApplicationMaster**가 된다.

     **MapReduce의 Driver 프로그램**은 내부적으로 **MapReduce 클러스터 구성**을 위해 **ApplicationMaster**를 먼저 요청한다. ApplicationMaster는 보통 1개로 구성되기 때문에 1개만 요청한다.

  5. YARN의 ResourceManager는 요청받은 ApplicationMaster를 자신이 관리하는 클러스터(여러개의 NodeManager) 중 `하나의 서버를 선택`하여 `ApplicationMaster(JobTracker 역할)를 실행`하고 이 서버를 클라이언트에게 알려준다.

     ApplicationMaster는 각 컴퓨팅 플랫폼별로 다른데 MapReduce에 사용되는 ApplicationMaster는 `MRAppMaster` 이며  YARN 패키지가 아닌 **MapReduce 패키지내에 존재**한다.

  6. 클라이언트는 ResourceManager로 부터 받은 ApplicationMaster 서버에 `MapReduce 작업 요청`을 한다.

  7. MRAppMaster는 작업 요청을 받으면 사용자가 실행한 MapReduce 작업에 필요한 리소스를 다시 `ResourceManager에게 요청`한다.

  8. ResourceManager는 `요청받은 리소스에 대해 NodeManager를 지정`하고 `Container를 실행`한 후 Container 목록을 MRAppMaster에 준다.

  9. NodeManager에 의해 실행된 MapReduce Task를 위한 Container는 MRAppMaster와 통신을 하며 `기존 방식의 JobTracker/TaskTracker에서 처리했던 방식`과 유사한 방법으로 Task를 실행한다.

     이 과정에서 MRAppMaster의 재활용, Task를 위해 실행된 Container의 재활용 등은 MapReduce와 YARN의 버전업에 따라 처리 방식이 조금씩 다르다.

- 구성을 잘 보면 크게 달라진 것은 없다. MapReduce 작업을 위한 클러스터가 항상 실행되어 있지 않기 때문에 작업을 실행하기 전에 클러스터를 구성하는 단계가 추가 되었을 뿐이다.     



### YARN을 이용한 클러스터 자원 공유

`ex` 100대의 서버 ⇒ 1개의 MapReduce 작업 실행

​	⇒ Spark 작업 요청 수신

- YARN (x)

  1. 서버 50대에 MapReduce 클러스터 구성

     나머지 50대에 Spark 클러스터 구성

     ⇒ MapReduce 작업만 수행하고 Spark 작업은 수행하지 않을 경우 자원 낭비

  2. 100대에 MapReduce 클러스터, Spark 클러스터 모두 구성

     ⇒ 각 클러스터가 메모리를 모두 사용하도록 구성할 경우, 두 작업 동시 실행 `불가`

     메모리를 50% 나눠 쓰도록 구성할 경우, 둘 중 하나만 실행할 땐 나머지 자원 낭비

- YARN (o)

  1. 100 대에 YARN 설치

     MapReduce 작업 실행 시엔, 100대 서버에서 각 서버의 모든 자원을 활용하여 작업 실행

     Spark 작업을 추가로 실행하면, YARN은 50대의 서버에서 MapReduce를 강제 종료하고 Spark 작업으로 할당

  ⇒ 서로 다른 컴퓨팅 플랫폼에서 작업하는 작업들이 하나의 리소스 관리자에 의해 관리되어 서버 리소스 낭비 최소화 가능       



### YARN의 스케줄러

- FIFO를 사용할 경우 위의 시나리오는 제한적으로 얻을 수 있고, Capacity, Fair 를 사용하면 모두 얻을 수 있다.
- 종류    
  1. FIFO (Default)
  2. Capacity
  3. Fair

 