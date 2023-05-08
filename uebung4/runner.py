import time
import threading

ids = {'x': 1, 'y': 2}
names = ["Anne", "Tina"]


class ScheduleRunner:
    def __init__(self, conn, conn2, use_locks=False):
        self.conn = conn
        self.conn2 = conn2
        self.use_locks = use_locks
        self.t1_ops = 0
        self.t2_ops = 0

    def run(self, schedule: list):
        self.begin(1)
        self.begin(2)
        for op in schedule:
            threading.Thread(target=self.run_op, args=(op,)).start()
            time.sleep(.25)

    def run_op(self, op: str):
        op_type = op[0]
        trans_num = int(op[1])
        if op_type != 'c':
            # increment outstanding ops
            if trans_num == 1:
                self.t1_ops += 1
            else:
                self.t2_ops += 1
            operand = op[3]
        else:
            self.commit(trans_num)
            return
        name_id = ids[operand]
        if op_type == 'r':
            self.read(trans_num, name_id)
        else:
            self.write(trans_num, name_id, names[trans_num - 1])
        # decrement outstanding ops
        if trans_num == 1:
            self.t1_ops -= 1
        else:
            self.t2_ops -= 1

    def begin(self, transaction):
        if transaction == 1:
            cur = self.conn.cursor()
        else:
            cur = self.conn2.cursor()
        cur.execute("BEGIN;")
        print(f"Begin T{transaction}", flush=True)

    def read(self, transaction, name_id):
        if transaction == 1:
            cur = self.conn.cursor()
        else:
            cur = self.conn2.cursor()
        # locks
        if self.use_locks:
            cur.execute("SELECT * FROM sheet4 WHERE id = %s FOR SHARE;", (name_id,))
            if cur.fetchone():
                print(f"read lock on id = {name_id} for T{transaction}", flush=True)
            else:
                print(f"failed read lock on id = {name_id} for T{transaction}")
        cur.execute("Select name from sheet4 where id = %s;", (name_id,))
        print(f"SELECT id = {name_id} on T{transaction}: {cur.fetchone()[0]}", flush=True)
        cur.close()

    def write(self, transaction, name_id, name):
        if transaction == 1:
            cur = self.conn.cursor()
        else:
            cur = self.conn2.cursor()
        # locks
        if self.use_locks:
            print(f"try write lock on id = {name_id} for T{transaction}", flush=True)
            cur.execute("SELECT * FROM sheet4 WHERE id = %s FOR UPDATE;", (name_id,))
            if cur.fetchone():
                print(f"write lock on id = {name_id} for T{transaction}", flush=True)
            else:
                print(f"failed write lock on id = {name_id} for T{transaction}")
        cur.execute("UPDATE sheet4 SET name = %s WHERE id = %s;", (name, name_id))
        cur.close()
        print(f"UPDATE WHERE id = {name_id} to {name} on T{transaction}", flush=True)

    def commit(self, transaction):
        if transaction == 1:
            while self.t1_ops > 0:
                time.sleep(.1)
            self.conn.commit()
        else:
            while self.t2_ops > 0:
                time.sleep(.1)
            self.conn2.commit()
        print(f"Commit T{transaction}", flush=True)
