import json, csv, re
import pandas as pd


with open('./SentiWord_info.json', encoding='utf-8', mode='r') as fi:
    data = json.load(fi)
    
    labels = []
    titles = []

    f = open('../코로나.csv', 'r', encoding='utf-8')
    rdr = csv.reader(f)

    for line in rdr:
        title = line[4]
        clean_title = re.sub('[-=+,#/\?:^.@*\"※~ㆍ!』\\‘|\(\)\[\]\<\>`\'…》\”\“\’·]', ' ', title)
        word_list = clean_title.split()
        for wordname in word_list:
            label = []
            for i in range(0, len(data)):
                if data[i]['word'] == wordname or data[i]['word_root'] in wordname:
                    label.append((data[i]['word'],data[i]['polarity']))
                    break

        titles.append(clean_title)
        labels.append(label)
f.close()

my_title = pd.DataFrame({"title":titles, "label":labels})
my_title.to_csv(('./title_data_코로나5.csv'), sep=",", na_rep="NaN", encoding='utf-8')