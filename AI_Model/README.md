- kafka consumer 를 일반 `.py` 파일로 하면, topice 에 저장된 데이터 내에서 무한 루프를 도는 문제 발생

  ⇒ spark streaming 은 consumer에 이미 있는 데이터면 안 가져온다는 이야기를 듣고, kafka와 연동해서 pyspark 로 스트리밍을 구현해보기로 결정!