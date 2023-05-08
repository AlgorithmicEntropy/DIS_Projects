import psycopg2


def reset_db():
    conn = psycopg2.connect(
        host='localhost',
        database='db',
        user='postgres',
        password='postgres'
    )

    cur = conn.cursor()
    cur.execute("Drop table if exists sheet4")
    with open('setup.sql', 'r') as f:
        cur.execute(f.read())
    conn.commit()

    print("Reset db")


if __name__ == '__main__':
    reset_db()
