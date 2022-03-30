import torch

from transformers import BertTokenizer
from transformers import BertForSequenceClassification, AdamW
from keras.preprocessing.sequence import pad_sequences

import pandas as pd
import numpy as np

device = torch.device("cpu")
model_PATH = "/home/hadoop/model/"
model = BertForSequenceClassification.from_pretrained("bert-base-multilingual-cased", num_labels=3)

optimizer = AdamW(model.parameters(),
                       lr = 2e-5, # 학습률,
                       eps = 1e-8 # 0으로 나누는 것을 방지하기 위한 epsilon 값
                       )
tokenizer = BertTokenizer.from_pretrained('bert-base-multilingual-cased', do_lower_case=False)

model.load_state_dict(torch.load(model_PATH + "model_state_dict.pt", map_location=torch.device('cpu') ))
optimizer.load_state_dict(torch.load(model_PATH + "optimizer_state_dict.pt", map_location=torch.device('cpu') ))

def convert_input_data(sentences):
    tokenized_texts = [tokenizer.tokenize(sent) for sent in sentences]
    MAX_LEN = 128
    input_ids = [tokenizer.convert_tokens_to_ids(x) for x in tokenized_texts]
    input_ids = pad_sequences(input_ids, maxlen=MAX_LEN, dtype="long", truncating="post", padding="post")
    attention_masks = []

    for seq in input_ids:
        seq_mask = [float(i > 0) for i in seq]
        attention_masks.append(seq_mask)

    inputs = torch.tensor(input_ids)
    masks = torch.tensor(attention_masks)

    return inputs, masks

def test_sentences(sentences):
    model.eval()
    inputs, masks = convert_input_data(sentences)

    b_input_ids = inputs.to(device)
    b_input_mask = masks.to(device)

    with torch.no_grad():
        outputs = model(b_input_ids,
                        token_type_ids=None,
                        attention_mask=b_input_mask)

    logits = outputs[0]
    logits = logits.detach().cpu().numpy()

    return logits

def softmax(a) :
  e_a = np.exp(a - np.max(a))
  return e_a / e_a.sum()

np.set_printoptions(precision=6, suppress=True)
pd.options.display.float_format = '{:.5f}'.format
pd.reset_option('display.float_format')