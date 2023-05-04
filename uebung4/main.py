import psycopg2

from runner import ScheduleRunner
from schedules import S1, S2, S3

conn = psycopg2.connect(
    host='localhost',
    database='db',
    user='postgres',
    password='postgres',
)
conn.autocommit = False

conn2 = psycopg2.connect(
    host='localhost',
    database='db',
    user='postgres',
    password='postgres',
)
conn2.autocommit = False


runner = ScheduleRunner(conn, conn2)
runner.run(S1)

conn.close()
conn2.close()
