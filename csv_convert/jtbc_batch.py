
import json

import numpy as np
import pandas as pd

from model_variable import test_sentences, softmax
from keyword_hot import hot_keyword

def labeling(title):
    logits = test_sentences([title])
    res = softmax(logits)[0]
    if max(res) >= 0.65:
        return str(np.argmax(logits))
    else:
        return '3'

def keyword(content, title):
    sentence = title + ',' + content
    print(sentence)
    return hot_keyword([sentence])

# df = pd.read_csv('C:\\Users\\multicampus\\Desktop\\news\\JTBC(2021_1=2021_10.8).csv', sep="\t", names=['title','content','date','url','press'],nrows=3)

df = pd.read_csv('C:\\Users\\multicampus\\Desktop\\news\\JTBC(2021_1=2021_10.8).csv', sep="\t", names=['title','content','date','url','press'])

df.fillna('jtbc', inplace=True)
# pd.set_option('display.width', 1000)
# pd.set_option('display.max_rows', None)
# pd.set_option('display.max_columns', None)

df['label'] = df['title'].apply(labeling, args=( ))

df['keyword'] = df['content'].apply(keyword, args=(df['title'], ))

df = df.explode('keyword')

df['date'] = pd.to_datetime(df['date'])

df['date'] = df['date'].dt.date

# keys = lambda x : (x.keyword, x.press, x.date, x.label)

df2 = df.groupby(['keyword','press','date','label']).size().reset_index(name="count")

df2.to_csv('./result/count.csv')


# df.to_csv('hdfs://ip-172-26-4-211.ap-northeast-2.compute.internal:9000/user/j6b207/unsung/origin.csv')

# df.to_csv('./result/')

# keyword_sel = ddd.select(ddd.press, ddd.label, F.date_format(ddd.time, "yyy-MM-dd").alias('date') ,explode(ddd.keyword).alias('keyword'))

# # keywordcount = keyword_sel.groupby('keyword', 'label', 'press', 'date').count()
# #
# # keywordcount.printSchema()

# keyword_sel.show(5)

# # keywordcount.write.format('console').mode('append').save()

# # ddd.write.format('text').mode('append')\
# #     .save('/user/j6b207/unsung/')

# # spark_session.streams.awaitAnyTermination()