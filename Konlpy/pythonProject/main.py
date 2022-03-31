from collections import Counter
from konlpy.tag import Okt
import csv

filename = "test.csv"
f = open(filename, 'r', encoding='utf-8')
news = f.read()

okt = Okt()
noun = okt.nouns(news)

for i, v in enumerate(noun):
    if len(v) < 2:
        noun.pop(i)

count = Counter(noun)
f.close()

noun_list = count.most_common(10)
for v in noun_list:
    print(v)
