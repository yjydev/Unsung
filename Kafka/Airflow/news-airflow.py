from datetime import datetime
import pendulum
from airflow import DAG
from airflow.operators.bash_operator import BashOperator

local_tz = pendulum.timezone("Asia/Seoul")
 
dag1 = DAG(
    'crawlingFlow',  # DAG id
    start_date = datetime(2022, 4, 5),
    schedule_interval='*/10 * * * *',  # 10 min
    catchup=False)

dag2 = DAG(
    'sqoopFlow',  # DAG id
    start_date = datetime(2022, 4, 5),
    schedule_interval='*/10 * * * *',  # 10 min
    catchup=False)
 
# t1 = BashOperator(task_id='yna-ytn', bash_command='java -jar /home/j6b207/airflow/dags/yna_ytn.jar', dag=dag1)

t2 = BashOperator(task_id='kbs-jtbc', bash_command='java -jar /home/j6b207/airflow/dags/kbs_jtbc.jar', dag=dag1)

t3 = BashOperator(task_id='joongang-sbs', bash_command='java -jar /home/j6b207/airflow/dags/joongang_sbs.jar', dag=dag1)

t4 = BashOperator(task_id='sqoop-to-mysql', bash_command='sqoop export --connect jdbc:mysql://j6b207.p.ssafy.io:3306/unsung --export-dir /user/j6b207/unsung/news/newdata --username root --password ssafy_b207@ -m 1 --bindir /home/j6b207/sqoop-1.4.7.bin__hadoop-2.6.0 --table keywordratio --input-fields-terminated-by "," --columns keyword,press,date,label,count --update-key keyword,label,press,date --update-mode allowinsert', dag = dag2)
 
# t1
t2
t3
t4