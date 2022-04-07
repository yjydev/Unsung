## IDE 설치

### snap

- IDE 설치를 위한 snap 설치

```sql
sudo apt-get install snapd
```



<hr>



### intellij

- intellij 설치

```sql
sudo snap install intellij-idea-community --classic
```



#### maven 프로젝트 시작

1. new project > Maven > Next > Name 설정 > Finish

2. pom.xml 수정하여 의존성 추가

   - pom.xml (의존성 추가)

     ```sql
     # pom.xml 
     
     <properties>
     ....
     </properties>
     
     <dependencies>
             <!-- <https://mvnrepository.com/artifact/org.apache.kafka/kafka> -->
             <dependency>
                 <groupId>org.apache.kafka</groupId>
                 <artifactId>kafka_2.13</artifactId>
                 <version>2.8.0</version>
             </dependency>
             <!-- <https://mvnrepository.com/artifact/org.apache.kafka/kafka-clients> -->
             <dependency>
                 <groupId>org.apache.kafka</groupId>
                 <artifactId>kafka-clients</artifactId>
                 <version>2.8.0</version>
             </dependency>
             <!-- <https://mvnrepository.com/artifact/org.apache.kafka/kafka-streams> -->
             <dependency>
                 <groupId>org.apache.kafka</groupId>
                 <artifactId>kafka-streams</artifactId>
                 <version>2.8.0</version>
             </dependency>
             <dependency>
                 <groupId>org.slf4j</groupId>
                 <artifactId>slf4j-simple</artifactId>
                 <version>1.7.21</version>
             </dependency>
             <dependency>
                 <groupId>org.jsoup</groupId>
                 <artifactId>jsoup</artifactId>
                 <version>1.13.1</version>
             </dependency>
             <!-- <https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple> -->
             <dependency>
                 <groupId>com.googlecode.json-simple</groupId>
                 <artifactId>json-simple</artifactId>
                 <version>1.1.1</version>
             </dependency>
             <!-- <https://mvnrepository.com/artifact/org.springframework.kafka/spring-kafka> -->
             <dependency>
                 <groupId>org.springframework.kafka</groupId>
                 <artifactId>spring-kafka</artifactId>
                 <version>2.8.0</version>
             </dependency>
     
         </dependencies>
     ```

3. 오른쪽에 가로로 Maven이라 쓰여있는 곳 눌러서 새로고침 (의존성 설치)

4. `src/main/java` 폴더 선택된 상태에서 아래 코드 붙여넣기

   - Producer.java

     ```java
     import org.apache.kafka.clients.producer.KafkaProducer;
     import org.apache.kafka.clients.producer.ProducerRecord;
     import org.json.simple.JSONObject;
     import org.jsoup.Jsoup;
     import org.jsoup.nodes.Document;
     import org.jsoup.nodes.Element;
     import org.jsoup.select.Elements;
     
     import java.io.IOException;
     import java.time.LocalDateTime;
     import java.time.ZoneId;
     import java.time.ZonedDateTime;
     import java.time.format.DateTimeFormatter;
     import java.util.ArrayList;
     import java.util.Properties;
     
     public class Producer {
         private static final String TOPIC_NAME = "search_word";
     
         public static void main(String[] args) throws InterruptedException, IOException {
             Properties prop = new Properties();
             prop.put("bootstrap.servers", "localhost:9092"); // server, kafka host
             prop.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
             prop.put("value.serializer", "org.springframework.kafka.support.serializer.JsonSerializer");
             prop.put("acks", "all");
             prop.put("block.on.buffer.full", "true");
     
             JSONObject message = null;
     
             // producer 생성
             @SuppressWarnings("resource")
             KafkaProducer<String, JSONObject> producer = new KafkaProducer<>(prop);
             // message 전달
             while(true) {
                 ZonedDateTime nowUTC = ZonedDateTime.now(ZoneId.of("UTC"));
                 LocalDateTime now = nowUTC.withZoneSameInstant(ZoneId.of("Asia/Seoul")).toLocalDateTime();
                 now = now.minusMinutes(30);
     
                 ArrayList<JSONObject> list = crawlingTime(now);
                 for (int i=0;i<list.size();i++) {
                     message = list.get(i);
                     producer.send(new ProducerRecord<String, JSONObject>(TOPIC_NAME, message));
                 }
                 System.out.println("stop ------------------------------");
                 Thread.sleep(60000); // 1초 - 1000
             }
         }
     
         public static ArrayList<JSONObject> crawlingTime(LocalDateTime initTime) throws IOException {
             // 현재 시간 기준 15분 이내의 기사만 가져오기
             ArrayList<JSONObject> list = new ArrayList<>();
     
             DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm");
     
             String URL = "<https://news.naver.com/main/list.naver?mode=LPOD&mid=sec&oid=025&listType=title&date=20220324&page=>" + 1;
     
             Document doc = Jsoup.connect(URL).get();
             Elements elements = doc.select("div[class=\\"paging\\"] > a");
     
             Elements element = doc.select("ul[class=\\"type02\\"]");
     
             outer: for (Element ulEle : element.select("li")) {
                 Elements tag = ulEle.select("a");
     
                 for (Element e : tag) {
                     String newsUrl = e.attr("href");
                     Document subDoc = Jsoup.connect(newsUrl).get();
     
                     String title = null;
                     String content = null;
                     String time = null;
     
                     if (subDoc.getElementById("articleTitle") == null) {
                         if (subDoc.getElementsByClass("end_tit").isEmpty()) {
                             // 스포츠
                             title = subDoc.getElementsByClass("title").text();
                             content = subDoc.getElementById("newsEndContents").text();
                             time = subDoc.selectFirst("div.info > span").text();
                             time = time.replace("기사입력 ", "");
                         } else {
                             // 연예
                             title = subDoc.getElementsByClass("end_tit").text();
                             content = subDoc.getElementsByClass("end_body_wrp").text();
                             time = subDoc.select("span.author > em").text();
                         }
                     } else {
                         title = subDoc.getElementById("articleTitle").text();
                         content = subDoc.getElementById("articleBodyContents").text();
                         time = subDoc.selectFirst("div.sponsor > span[class=\\"t11\\"]").text();
                     }
                     String[] timeSpl = time.split(" ");
                     if (timeSpl[2].length() == 4)
                         timeSpl[2] = "0" + timeSpl[2];
                     String dateTime = timeSpl[0] + " " + timeSpl[2];
     
                     LocalDateTime date = LocalDateTime.parse(dateTime, sdf);
                     LocalDateTime newsDate = null;
     
                     if (timeSpl[1].equals("오후") && !timeSpl[2].substring(0, 2).equals("12")) {
                         newsDate = date.plusHours(12);
                     } else {
                         newsDate = date;
                     }
     
                     if (initTime.isBefore(newsDate)) {
                         JSONObject data = new JSONObject();
                         data.put("title", title);
                         data.put("content", content);
                         data.put("url", newsUrl);
                         data.put("time", String.valueOf(newsDate));
                         System.out.println(data);
                         list.add(data);
                     } else {
                         break outer;
                     }
                 }
             }
             return list;
         }
     }
     ```

   - 붙여넣기 하면 알아서 Class 랑 기타 등등 잡는다.

5. `Producer.java` 에서 오른쪽 버튼 클릭 > `Run`    



<hr> 



### pyCharm    

###### 리눅스나 우분투 환경에서 관리자 권한이 없을 경우, pyCharm에서 기본적으로 제공하는 가상환경엔 패키지 관리자 pip 가 default로 설치되어 있기 때문에 유용하게 활용 가능!    

#### pyCharm 설치

```sql
sudo snap install pycharm-community --classic
```

#### 파이참 프로젝트 시작 & 패키지 설치

1. new project > Name 설정 > create ⇒ 알아서 설정 완료
   - 다만, Default 설정이 가상환경이기 때문에 인터프리터 설정 필요
     - File > Setting > Project : {프로젝트명} > Python interpreter 옆 톱니바퀴 > Add > System Interpreter
     - 한번 실행하고 나면, 두번째부턴 프로젝트 생성할 때 부터는 `Previously configured interprter` 선택

2. 패키지 관리자 `pip` 설치    

   ```sql
   sudo apt-get install python3-pip
   ```



3. `kafka-python` , `pyspark` 설치   

   ```sql
   pip install kafka-python
   
   pip install pyspark
   ```



<hr>



### ai 모델

- 패키지 설치

  ```python
  pip install tensorflow
  pip install torch
  
  # Hugging Face의 트랜스포머 모델을 설치
  pip install transformers
  
  pip install pandas
  ```



