import psycopg2

conn = psycopg2.connect(
    host='localhost',
    database='db',
    user='postgres',
    password='postgres'
)

cur = conn.cursor()
cur.execute("Drop table sheet4")
with open('setup.sql', 'r') as f:
    cur.execute(f.read())
conn.commit()