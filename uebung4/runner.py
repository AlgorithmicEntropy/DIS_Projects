ids = {'x': 1, 'y': 2}
names = ["Anne", "Tina"]


class ScheduleRunner:
    def __init__(self, conn, conn2):
        self.conn = conn
        self.conn2 = conn2

    def run(self, schedule: list):
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

    def read(self, transaction, name_id):
        if transaction == 1:
            cur = self.conn.cursor()
        else:
            cur = self.conn2.cursor()
        cur.execute("Select name from sheet4 where id = %s;", (name_id,))
        print(f"SELECT id = {name_id} on T{transaction}: {cur.fetchone()[0]}")

    def write(self, transaction, name_id, name):
        if transaction == 1:
            cur = self.conn.cursor()
        else:
            cur = self.conn2.cursor()
        cur.execute("UPDATE sheet4 SET name = %s WHERE id = %s;", (name, name_id))
        print(f"UPDATE WHERE id = {name_id} to {name} on T{transaction}")

    def commit(self, transaction):
        if transaction == 1:
            self.conn.commit()
        else:
            self.conn2.commit()
        print(f"Commit T{transaction}")
