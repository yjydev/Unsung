## 하둡 분산 시스템 세팅
-------

### 우분투 Master, slave1, slave2 세팅
![우분투분산](./image/%EC%9A%B0%EB%B6%84%ED%88%AC%20%EB%B6%84%EC%82%B0.png)

-------

### 우분투 master, slave1, slave2 spark 설치

 
![스파크설치](./image/Slave%20spark%EC%84%A4%EC%B9%98.png)

이 후 환경설정 및 분산 데이터 처리 해볼 예정


## TF-IDF 알고리즘
--------------------
```java
//문서별 단어 중요도 계산
/**
     * @param doc  list of strings
     * @param term String represents a term
     * @return term frequency of term in document
*/
public double tf(List<String> doc, String term) {
	        double result = 0;
	        for (String word : doc) {
	            if (term.equalsIgnoreCase(word))
	                result++;
	        }
	        return result / doc.size();
	    }
//단어가 다른 문서에서 얼만큼 등장하는지 계산하여 역빈도 산출
/**
     * @param docs list of list of strings represents the dataset
     * @param term String represents a term
     * @return the inverse term frequency of term in documents
*/
public double idf(List<List<String>> docs, String term) {
	        double n = 0;
	        for (List<String> doc : docs) {
	            for (String word : doc) {
	                if (term.equalsIgnoreCase(word)) {
	                    n++;
	                    break;
	                }
	            }
	        }
	        return Math.log(docs.size() / n);
	    }
//TF와 IDF를 호출해 최종 TF-IDF값을 연산
/**
     * @param doc  a text document
     * @param docs all documents
     * @param term term
     * @return the TF-IDF of term
*/

public double tfIdf(List<String> doc, List<List<String>> docs, String term) {
	        return tf(doc, term) * idf(docs, term);
}

//이후 크롤링된 파일의 단어 빈도수 산출
```

### 텍스트를 읽어 키워드 추출하는 알고리즘
----------------

```python
#문장을 키워드 추출하기

from textrank import KeywordSummarizer

docs = ['list of str form', 'sentence list']

keyword_extractor = KeywordSummarizer(
    tokenize = lambda x:x.split(),      # YOUR TOKENIZER
    window = -1,
    verbose = False
)

keywords = keyword_extractor.summarize(sents, topk=30)
for word, rank in keywords:
    # do something

#두 용어가 발생하는 빈도수 구하기
from textrank import KeywordSummarizer

keyword_extractor = KeywordSummarizer(
    tokenize = lambda x:x.split()
    min_count=2,
    window=-1,                     # cooccurrence within a sentence
    min_cooccurrence=2,
    vocab_to_idx=None,             # you can specify vocabulary to build word graph
    df=0.85,                       # PageRank damping factor
    max_iter=30,                   # PageRank maximum iteration
    bias=None,                     # PageRank initial ranking
    verbose=False
)

#중요 문장 추출 함수
from textrank import KeysentenceSummarizer

summarizer = KeysentenceSummarizer(
    tokenize = YOUR_TOKENIZER,
    min_sim = 0.5,
    verbose = True
)

keysents = summarizer.summarize(sents, topk=5)
for sent_idx, rank, sent in keysents:
    # do something

#중요 문장 뽑아오기
from konlpy.tag import Komoran

komoran = Komoran()
def komoran_tokenizer(sent):
    words = komoran.pos(sent, join=True)
    words = [w for w in words if ('/NN' in w or '/XR' in w or '/VA' in w or '/VV' in w)]
    return words

summarizer = KeysentenceSummarizer(
    tokenize = komoran_tokenizer,
    min_sim = 0.3,
    verbose = False
)

keysents = summarizer.summarize(sents, topk=3)

#중요 문장의 키워드 빈도수 추출함수
from textrank import KeywordSummarizer

summarizer = KeywordSummarizer(tokenize=komoran_tokenizer, min_count=2, min_cooccurrence=1)
summarizer.summarize(sents, topk=20)