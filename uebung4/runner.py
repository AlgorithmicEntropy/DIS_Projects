ids = {'x': 1, 'y': 2}
names = ["Anne", "Tina"]


class ScheduleRunner:
    def __init__(self, conn, conn2, use_locks=False):
        self.conn = conn
        self.conn2 = conn2
        self.use_locks = use_locks

    def run(self, schedule: list):
        self.begin(1)
        self.begin(2)
        for op in schedule:
            op_type = op[0]
            trans_num = int(op[1])
            if op_type != 'c':
                operand = op[3]
            else:
                self.commit(trans_num)
                continue
            name_id = ids[operand]
            if op_type == 'r':
                self.read(trans_num, name_id)
            else:
                self.write(trans_num, name_id, names[trans_num - 1])

    def begin(self, transaction):
        if transaction == 1:
            cur = self.conn.cursor()
        else:
            cur = self.conn2.cursor()
        cur.execute("BEGIN;")
        print(f"Begin T{transaction}")

    def read(self, transaction, name_id):
        if transaction == 1:
            cur = self.conn.cursor()
        else:
            cur = self.conn2.cursor()
        # locks
        if self.use_locks:
            cur.execute("SELECT * FROM sheet4 WHERE id = %s FOR SHARE SKIP LOCKED;", (name_id,))
            if cur.fetchone():
                print(f"read lock on id = {name_id} for T{transaction}")
            else:
                print(f"failed read lock on id = {name_id} for T{transaction}")
        cur.execute("Select name from sheet4 where id = %s;", (name_id,))
        print(f"SELECT id = {name_id} on T{transaction}: {cur.fetchone()[0]}")

    def write(self, transaction, name_id, name):
        if transaction == 1:
            cur = self.conn.cursor()
        else:
            cur = self.conn2.cursor()
        # locks
        if self.use_locks:
            cur.execute("SELECT * FROM sheet4 WHERE id = %s FOR UPDATE SKIP LOCKED;", (name_id,))
            if cur.fetchone():
                print(f"write lock on id = {name_id} for T{transaction}")
            else:
                print(f"failed write lock on id = {name_id} for T{transaction}")
        cur.execute("UPDATE sheet4 SET name = %s WHERE id = %s;", (name, name_id))
        print(f"UPDATE WHERE id = {name_id} to {name} on T{transaction}")

    def commit(self, transaction):
        if transaction == 1:
            self.conn.commit()
        else:
            self.conn2.commit()
        print(f"Commit T{transaction}")
