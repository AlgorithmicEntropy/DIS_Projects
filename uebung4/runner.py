names = {'x': 'Karl', 'y': 'Lisa'}


class ScheduleRunner:
    def __init__(self, conn, conn2):
        self.conn = conn
        self.conn2 = conn2

    def run(self, schedule: list):
        for op in schedule:
            op_type = op[0]
            trans_num = op[1]
            if op_type != 'c':
                operand = op[3]
            else:
                self.commit(trans_num)
                continue
            if op_type == 'r':
                self.read(trans_num, names[operand])
            else:
                self.write(trans_num, names[operand])

    def read(self, transaction, name):
        if transaction == 1:
            cur = self.conn.cursor()
        else:
            cur = self.conn2.cursor()
        cur.execute("Select id from sheet4 where name = %s", (name,))

    def write(self, transaction, name):
        if transaction == 1:
            cur = self.conn.cursor()
        else:
            cur = self.conn2.cursor()
        cur.execute("INSERT INTO sheet4(name) VALUES(%s)", (name,))

    def commit(self, transaction):
        if transaction == 1:
            self.conn.commit()
        else:
            self.conn2.commit()
