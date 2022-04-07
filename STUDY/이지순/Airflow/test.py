from datetime import datetime
import pendulum
from airflow import DAG
from airflow.operators.bash_operator import BashOperator

local_tz = pendulum.timezone("Asia/Seoul")
 
dag = DAG(
    'crawlingFlow',  # DAG id
    start_date=datetime(2022, 3, 28, tzinfo=local_tz),  # 언제부터 DAG이 시작되는가
    schedule_interval='*/30 * * * *',  # 10 min
    catchup=False)
 
t1 = BashOperator(task_id='joongang', bash_command='java -jar /home/hadoop/airflow/dags/joongang-crawling.jar', dag=dag)

t1