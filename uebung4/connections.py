import psycopg2


def new_connection():
    conn = psycopg2.connect(
        host='localhost',
        database='db',
        user='postgres',
        password='postgres'
    )
    return conn