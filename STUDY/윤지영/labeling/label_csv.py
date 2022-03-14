from tqdm import tqdm
import re
import pandas as pd
import csv, json

with open("./negative_words.txt", encoding='utf-8') as neg:
    negative = neg.readlines()

with open("./positive_words.txt", encoding="utf-8") as pos:
    positive = pos.readlines()

negative = [neg.replace("\n", "") for neg in negative]
positive = [pos.replace("\n", "") for pos in positive]

labels = []
titles = []

f = open('코로나.csv', 'r', encoding="UTF8")
rdr = csv.reader(f)

for line in rdr:
    title = line[4]
    # clean_title = re.sub('[-=+,#/\?:^$.@*\"※~$%!·」』\\')
    clean_title = re.sub('[-=+,#/\?:^.@*\"※~ㆍ!』\\‘|\(\)\[\]\<\>`\'…》\”\“\’·]', ' ', title)
    negative_flag = False
    label = 0
    for i in range(len(negative)):
        if negative[i] in clean_title:
            label = -1
            negative_flag = True
            # print("negative 비교단어: ",negative[i], "제목: ",clean_title)
            break
    if negative_flag == False:
        for i in range(len(positive)):
            if positive[i] in clean_title:
                label = 1
                # print("긍정 비교단어: ",positive[i], "제목: ",clean_title)
                break
    titles.append(clean_title)
    labels.append(label)
f.close()

my_title = pd.DataFrame({"title":titles, "label":labels})
my_title.to_csv(('./title_data_코로나.csv'), sep=",", na_rep="NaN", encoding='utf-8')