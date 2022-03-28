from kafka import KafkaConsumer
from json import loads
import pyspark

import tensorflow as tf
import torch

from transformers import BertTokenizer
from transformers import BertForSequenceClassification, AdamW, BertConfig
from keras.preprocessing.sequence import pad_sequences

import pandas as pd
import numpy as np
import random
import time
import datetime

device = torch.device("cpu")

# load model, optimizer 설정
# 분류를 위한 BERT 모델 생성
model_PATH = "/home/hadoop/model/"
model = BertForSequenceClassification.from_pretrained("bert-base-multilingual-cased", num_labels=3)

# 옵티마이저 설정
optimizer = AdamW(model.parameters(),
                       lr = 2e-5, # 학습률,
                       eps = 1e-8 # 0으로 나누는 것을 방지하기 위한 epsilon 값
                       )
tokenizer = BertTokenizer.from_pretrained('bert-base-multilingual-cased', do_lower_case=False)

model.load_state_dict(torch.load(model_PATH + "model_state_dict.pt", map_location=torch.device('cpu') ))
optimizer.load_state_dict(torch.load(model_PATH + "optimizer_state_dict.pt", map_location=torch.device('cpu') ))


# 입력 데이터 변환
def convert_input_data(sentences):
    # BERT의 토크나이저로 문장을 토큰으로 분리
    tokenized_texts = [tokenizer.tokenize(sent) for sent in sentences]

    # 입력 토큰의 최대 시퀀스 길이
    MAX_LEN = 128

    # 토큰을 숫자 인덱스로 변환
    input_ids = [tokenizer.convert_tokens_to_ids(x) for x in tokenized_texts]

    # 문장을 MAX_LEN 길이에 맞게 자르고, 모자란 부분을 패딩 0으로 채움
    input_ids = pad_sequences(input_ids, maxlen=MAX_LEN, dtype="long", truncating="post", padding="post")

    # 어텐션 마스크 초기화
    attention_masks = []

    # 어텐션 마스크를 패딩이 아니면 1, 패딩이면 0으로 설정
    # 패딩 부분은 BERT 모델에서 어텐션을 수행하지 않아 속도 향상
    for seq in input_ids:
        seq_mask = [float(i > 0) for i in seq]
        attention_masks.append(seq_mask)

    # 데이터를 파이토치의 텐서로 변환
    inputs = torch.tensor(input_ids)
    masks = torch.tensor(attention_masks)

    return inputs, masks

# 문장 테스트
def test_sentences(sentences):
    # 평가모드로 변경
    model.eval()

    # 문장을 입력 데이터로 변환
    inputs, masks = convert_input_data(sentences)

    # 데이터를 cpu에 넣음
    b_input_ids = inputs.to(device)
    b_input_mask = masks.to(device)

    # 그래디언트 계산 안함
    with torch.no_grad():
        # Forward 수행
        outputs = model(b_input_ids,
                        token_type_ids=None,
                        attention_mask=b_input_mask)

    # 로스 구함
    logits = outputs[0]

    # CPU로 데이터 이동
    logits = logits.detach().cpu().numpy()

    return logits

def softmax(a) :
  e_a = np.exp(a - np.max(a))
  return e_a / e_a.sum()

# softmax 적용값인 지수표기를 숫자로 지정
np.set_printoptions(precision=6, suppress=True)

pd.options.display.float_format = '{:.5f}'.format
pd.reset_option('display.float_format')

# topic, broker list
consumer = KafkaConsumer(
    'search_word',
    bootstrap_servers=['localhost:9092'],
    auto_offset_reset='earliest',
    enable_auto_commit=True,
    group_id='my-group',
    value_deserializer=lambda x: loads(x.decode('utf-8')),
    consumer_timeout_ms=1000 )

document = []
label = []
result = []

# consumer list를 가져온다
print('[begin] get consumer list')
for message in consumer:
    # print("Topic: %s, Partition: %d, Offset: %d, Key: %s, Value: %s"
    #       % ( message.topic, message.partition, message.offset, message.key, message.value))
    msg = message.value
    title = msg['title']
    # clean_title = re.sub('[-=+,#/\?:^.@*\"※~ㆍ!』\\‘|\(\)\[\]\<\>`\'…》\”\“\’·]', ' ', title)
    logits = test_sentences([title])
    document.append(title)
    label.append(np.argmax(logits))
    result.append(softmax(logits))

    print('[end] get consumer list')

    dff = pd.DataFrame({"document": document, "label": label, "result": result })
    print(dff)
    dff.to_csv('/home/hadoop/label2.csv', encoding='utf-8')
