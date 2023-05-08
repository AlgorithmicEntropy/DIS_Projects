import asyncio

import psycopg2

from connections import new_connection
from isolation import show_isolation_level, set_isolation, IsolationLevel
from reset_db import reset_db
from runner import ScheduleRunner
from schedules import S1, S2, S3


def main():
    # reset db
    reset_db()

    conn = new_connection()
    conn.autocommit = False

    conn2 = new_connection()
    conn2.autocommit = False

    isolation_level = IsolationLevel.READ_COMMITTED

    # show isolation level
    # show_isolation_level()
    # change isolation level
    set_isolation(conn, isolation_level)
    set_isolation(conn2, isolation_level)

    runner = ScheduleRunner(conn, conn2)

    # change me to run different schedule
    runner.run(S2)

    conn.close()
    conn2.close()


if __name__ == "__main__":
    main()
