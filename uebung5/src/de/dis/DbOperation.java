package de.dis;

public class DbOperation {
    private final int transactionID;
    private final int lsn;
    private final String data;

    public DbOperation(int transactionID, int lsn, String data) {
        this.transactionID = transactionID;
        this.data = data;
        this.lsn = lsn;
    }

    public int getTransactionID() {
        return transactionID;
    }

    public String getData() {
        return data;
    }

    public int getLsn() {
        return lsn;
    }
}
