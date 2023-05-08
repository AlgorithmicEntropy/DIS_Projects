import asyncio

ids = {'x': 1, 'y': 2}
names = ["Anne", "Tina"]


class ScheduleRunner:
    def __init__(self, conn, conn2, use_locks=False):
        self.conn = conn
        self.conn2 = conn2
        self.use_locks = use_locks

    async def run(self, schedule: list):
        t1 = []
        t2 = []
        for op in schedule:
            if int(op[1]) == 1:
                t1.append(op)
            else:
                t2.append(op)

        await asyncio.gather(self.run_transaction(t1, 1), self.run_transaction(t2, 2))

    async def run_transaction(self, schedule: list, number: int):
        await self.begin(number)
        for op in schedule:
            print(f"Sleeping for T{number}... ")
            # await asyncio.sleep(.1)
            print(f"Done sleeping for T{number}")
            op_type = op[0]
            trans_num = int(op[1])
            if op_type != 'c':
                operand = op[3]
            else:
                await self.commit(trans_num)
                continue
            name_id = ids[operand]
            if op_type == 'r':
                await self.read(trans_num, name_id)
            else:
                await self.write(trans_num, name_id, names[trans_num - 1])

    async def begin(self, transaction):
        if transaction == 1:
            cur = self.conn.cursor()
        else:
            cur = self.conn2.cursor()
        cur.execute("BEGIN;")
        print(f"Begin T{transaction}")

    async def read(self, transaction, name_id):
        if transaction == 1:
            cur = self.conn.cursor()
        else:
            cur = self.conn2.cursor()
        # locks
        if self.use_locks:
            cur.execute("SELECT * FROM sheet4 WHERE id = %s FOR SHARE;", (name_id,))
            if cur.fetchone():
                print(f"read lock on id = {name_id} for T{transaction}")
            else:
                print(f"failed read lock on id = {name_id} for T{transaction}")
        cur.execute("Select name from sheet4 where id = %s;", (name_id,))
        print(f"SELECT id = {name_id} on T{transaction}: {cur.fetchone()[0]}")
        cur.close()

    async def write(self, transaction, name_id, name):
        if transaction == 1:
            cur = self.conn.cursor()
        else:
            cur = self.conn2.cursor()
        # locks
        if self.use_locks:
            cur.execute("SELECT * FROM sheet4 WHERE id = %s FOR UPDATE;", (name_id,))
            if cur.fetchone():
                print(f"write lock on id = {name_id} for T{transaction}")
            else:
                print(f"failed write lock on id = {name_id} for T{transaction}")
        cur.execute("UPDATE sheet4 SET name = %s WHERE id = %s;", (name, name_id))
        cur.close()
        print(f"UPDATE WHERE id = {name_id} to {name} on T{transaction}")

    async def commit(self, transaction):
        if transaction == 1:
            self.conn.commit()
        else:
            self.conn2.commit()
        print(f"Commit T{transaction}")
