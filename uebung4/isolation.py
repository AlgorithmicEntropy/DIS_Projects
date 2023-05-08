from enum import Enum


class IsolationLevel(Enum):
    # postgres does not support read uncommitted
    READ_COMMITTED = "READ COMMITTED",
    REPEATABLE_READ = "REPEATABLE READ",
    SERIALIZABLE = "SERIALIZABLE"


def show_isolation_level(conn):
    cur = conn.cursor()
    cur.execute("SHOW transaction_isolation;")
    print(f"Current isolation level: {cur.fetchone()[0]}")
    cur.close()


def set_isolation(conn, level: IsolationLevel):
    cur = conn.cursor()
    level = level.value
    if isinstance(level, tuple):
        level = level[0]
    cur.execute(f"SET TRANSACTION ISOLATION LEVEL {level};")
    cur.execute("SHOW transaction_isolation;")
    print(f"Current isolation level: {cur.fetchone()[0]}")
    cur.close()
