# Airflow

 에어비앤비에서 개발한 워크플로우 스케줄링, 모니터링 플랫폼

### 특징

* Dynamic : 에어플로우 파이프라인(동작순서, 방식)을 파이썬 코드를 이용하여 구성하기 때문에 동적인 구성이 가능
* Extensible : python 이용하여 Operator, executor를 사용하여 사용자의 환경에 맞게 확장하여 구성하는 것이 가능함.
* Elegant : 에어플로우 파이프라인은 간결하고 명시적이며, 진자 템플릿(jinja template)을 이용하여 파라미터화 된 데이터를 전달하고 자동으로 파이프라인을 생성하는 것이 가능
* Scaleable : 분산구조와 메시지 큐를 이용하여 많은 수의 워커간의 협업을 지원하고, 스케일 아웃이 가능함.

### 구성

![https://user-images.githubusercontent.com/46081043/159828214-15c65bd2-1b4f-49cc-be92-b778503986ee.png](https://user-images.githubusercontent.com/46081043/159828214-15c65bd2-1b4f-49cc-be92-b778503986ee.png)

* Scheduler
    * 실행 주기가 되면 작업 생성
    * 의존하는 작업이 모두 성공하면 Broker에 넘김
* Worker
    * 실제 작업을 실행하는 주체
* Broker
    * 실행가능한 작업들이 들어가는 공간
* Meta DB
    * DAG, Task 등이 정의되어있음
        * DAG(Directed Acyclic Graph) : 방향을 가진 그래프 중 순환을 포함하지 않은 그래프, Airflow는 Task의 연결관계를 DAG로 관리하고 웹 인터페이스를 통해서도 DAG 구조를 시각적으로 확인할 수 있다.
    * DAG run, Task Instance 관리

### Spark 실행을 위한 SparkSubmitOperator

* [https://louisdev.tistory.com/8](https://louisdev.tistory.com/8)

### 주의

* 초단위의 데이터 처리가 필요한 경우(스트리밍 용도로 에어플로우를 사용하기에는 부적절하다) 스트리밍 용도로 에어플로우를 사용하기에는 부적절하다.
* 데이터 프로세싱에는 이용하지 않는 것이 좋다.
    
    에어플로우는 데이터 프로세싱에는 이용하지 않는 것이 좋다. (스파크와는 다르다!!)
    
    그러한 작업에 최적화 되어 있지도 않아서 매우 느리고, 경우에 따라 메모리 부족으로 작업이 진행되지 않을 수도 있다.
    
    따라서,
    
    *SparkSubmitOperator*
    
    와 같은 Operator를 이용하여, 데이터 프로세싱은 Spark와 같은 외부 Framework로 처리
    
    Airflow를 통해서는 오케스트레이션만 진행하는 방식으로 주로 사용한다.
    
### 설치
1. Python 가상 환경 설정(안해도 됨)
    
    ```bash
    python --version 
    # Python 버전 확인 후 venv 설정 해야하기 때문에
    
    python3.8 -m venv {가상환경 이름} 
    # 가상환경 생성
    
    source {가상환경 이름}/bin/activate 
    # 가상환경 활성화
    
    deactivate 
    # 가상환경 비활성화
    ```
    
2. Airflow 설치
    
    ```bash
    pip install apache-airflow
    export AIRFLOW_HOME=~/airflow
    airflow db init
    airflow users create --username {Login_ID} --firstname {First_NAME} --lastname {Last_NAME} --role Admin --password {Password} --email {Email}
    # 계정 설정해야 로그인 가능
    
    airflow users create --username unsung --firstname un --lastname sung --role Admin --password unsung --email unsung@test.com
    # 내가 설정한 값
    
    airflow webserver -p {원하는 port}
    ```
    
    ![https://user-images.githubusercontent.com/46081043/159846206-d1bc78f1-1a22-4dbd-9756-fbcc28664723.png](https://user-images.githubusercontent.com/46081043/159846206-d1bc78f1-1a22-4dbd-9756-fbcc28664723.png)
    
3. Example DAGs 삭제
    * DAG 예제 삭제
    * /airflow/airflog.cfg
    
    ```bash
    load_examples = False
    ```

### 참고 - JAR 파일 생성

- File → Project Structure → Artifacts + → JAR → From modules with dependencies → Main class 선택 → MANIFEST.MF 파일 위치 선택 → output derectory 선택하고 OK
- Build → Build Artifacts → build 하면 out이라는 폴더 안에 생성
- pom.xml 추가 하면 MANIFEST.MF 파일 생성됨 이 파일을 src/main/resources에 넣는다.

### 참고

[https://berrrrr.github.io/programming/2020/01/12/what-is-apache-airflow/](https://berrrrr.github.io/programming/2020/01/12/what-is-apache-airflow/)

[https://velog.io/@jjongbumeee/Airflow1](https://velog.io/@jjongbumeee/Airflow1)

[Airflow 설치](https://www.notion.so/Airflow-34b9a3774f0b427bb727d2b3869365c4)

Airflow 명령어 상세 : [https://velog.io/@jjongbumeee/Airflow3](https://velog.io/@jjongbumeee/Airflow3)
[https://velog.io/@insutance/Airflow-Airflow-간단하게-설치하기](https://velog.io/@insutance/Airflow-Airflow-%EA%B0%84%EB%8B%A8%ED%95%98%EA%B2%8C-%EC%84%A4%EC%B9%98%ED%95%98%EA%B8%B0)

# Azkaban

* Hadoop Job Dependency 문제를 해결하기 위해 개발된 분산 워크플로우 오픈소스
* Java 기반 (Airflow Python기반)

[https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=talag&logNo=220907544281](https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=talag&logNo=220907544281)