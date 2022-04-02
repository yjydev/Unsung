from collections import Counter

import pandas as pd
from sklearn.feature_extraction.text import CountVectorizer
import numpy as np
from konlpy.tag import Okt
import csv

f = open('test.csv', 'r')
lines = csv.reader(f)
okt = Okt()
vect = CountVectorizer(tokenizer=lambda x: x, lowercase=False)
word_list = []
for line in lines:
    noun = okt.nouns(line[0])
    for v in noun[:]:
        if len(v) < 2:
            noun.remove(v)
    word_list.append(noun)
f.close()

m = vect.fit_transform(word_list)
tf = pd.DataFrame(m.toarray(), columns=vect.get_feature_names_out())
D = len(tf)
df = tf.astype(bool).sum(axis=0)
idf = np.log((D+1) / (df+1)) + 1

tfidf = tf * idf
tfidf = tfidf / np.linalg.norm(tfidf, axis=1, keepdims=True)

# print(tfidf)
# print(type(tfidf))  # <class 'pandas.core.frame.DataFrame'>
# tfidf.to_html("test.html")

# print(tfidf.idxmax(axis=1))
n = len(tfidf.index)
# tfidf.index => RangeIndex(start=0, stop=16, step=1)

words = [[] for _ in range(n)]
for k in range(n):
    li = tfidf.loc[k].values
    for idx, val in enumerate(li):
        if val != 0 :
            # tfidf[k].iloc[np.where(tfidf)]
            words[k].append([tfidf.columns[idx],val])
for w in words:
    w.sort(key=lambda x:x[1], reverse=True)
    print(w)
# print(words)


#
# list(zip(ids, cols))
# print([tfidf.columns[c] for c in cols])


#
# count = Counter(noun)
# f.close()
#
# noun_list = count.most_common(10)
# for v in noun_list:
#     print(v)