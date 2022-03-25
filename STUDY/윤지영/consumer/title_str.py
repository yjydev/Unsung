# jupyter nbconvert --to script 파일명.ipynb 
# jupyter notebook 파일 .ipynb를 .py로 변환하여 추가 예정

# kafka producer 에서 보내서 topic에 저장한 메세지 받아오기까지 완료 

from kafka import KafkaConsumer
from json import loads

# topic, broker list
while True:
    consumer = KafkaConsumer(
        'search_word',
        bootstrap_servers=['localhost:9092'],
        auto_offset_reset='earliest',
        enable_auto_commit=True,
        group_id='my-group',
        # value_deserializer=lambda x: loads(x.decode('utf-8')),
        consumer_timeout_ms=1000 )

    # consumer list를 가져온다

    print('[begin] get consumer list')
    for message in consumer:
        print("Topic: %s, Partition: %d, Offset: %d, Key: %s, Value: %s"
              % ( message.topic, message.partition, message.offset, message.key, message.value.decode('utf-8')))
        msg = message.value.decode('utf-8')
        start = msg.find('title=')
        end = msg.find('content=')
        print(msg[start+6:end-2])
        print('[end] get consumer list')