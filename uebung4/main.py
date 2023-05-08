import psycopg2

from reset_db import reset_db
from runner import ScheduleRunner
from schedules import S1, S2, S3

# reset db
reset_db()

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
# change me to run different schedule
runner.run(S1)

conn.close()
conn2.close()
