package de.dis;

import java.io.*;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.stream.Collectors;

public class PersistenceManager {

    private static final PersistenceManager manager = new PersistenceManager();

    public static final String SEPARATOR = ";";

    private int transactionId = 0;
    private int lsn = 0;

    /* FIXME CB: What do we actually need here? I am a little bit confused. Not sure what key and value need to be. pageId and data?
        But where does that leave our transactionId? */
    private final Hashtable<Integer, DbOperation> buffer = new Hashtable<>();

    private final ArrayList<Integer> runningTransactions = new ArrayList<>();
    private final ArrayList<Integer> committedTransactions = new ArrayList<>();


    private PersistenceManager() {
        System.out.println("de.dis.PersistenceManager was created.");
    }

    /**
     * @return the single existing persistence manager
     */
    public static PersistenceManager getInstance() {
        return manager;
    }

    public synchronized int beginTransaction() {
        transactionId++;

        runningTransactions.add(transactionId);

        System.out.println("Transaction " + transactionId + " started.");
        return transactionId;
    }

    public synchronized void commit(int taid) {
        runningTransactions.remove((Integer) taid);
        committedTransactions.add(taid);

        System.out.println("Transaction " + taid + " commited.");

        LogEntry eotLogEntry = LogEntry.createEotLogEntry(lsn++, taid);
        eotLogEntry.writeLogEntry(getLogFile());
    }

    public synchronized void write(int taid, int pageId, String data) {
        // FIXME CB: Is that correct?
        buffer.put(pageId, new DbOperation(taid, lsn, data));

        System.out.println(MessageFormat.format("adding to buffer: taid={0}, pageId={1}, data={2}", taid, pageId, data));

        LogEntry dataLogEntry = LogEntry.createDataLogEntry(lsn, taid, pageId, data);
        dataLogEntry.writeLogEntry(getLogFile());
        lsn++;

        // Write buffer data of committed transactions to storage if the buffer contains more than five data sets
        if (buffer.size() > 5) {
            writeBufferToPersistentStorage();
        }
    }

    private void writeBufferToPersistentStorage() {
        ArrayList<Integer> writtenPages = new ArrayList<>();
        for (Map.Entry<Integer, DbOperation> entry: buffer.entrySet()) {
            int pageId = entry.getKey();
            DbOperation op = entry.getValue();
            if (committedTransactions.contains(op.getTransactionID())) {
                new PageFile(pageId).save(op.getLsn(), op.getData());
                writtenPages.add(pageId);
            }
        }
        for (int pageId : writtenPages) {
            buffer.remove(pageId);
        }
        committedTransactions.clear();
        System.out.printf("Buffer written to persistent storage with %s pages.%n", writtenPages.size());
    }


    /**
     * Returns the log file. If the file does not yet exist, the file, and it's parent directories are created.
     *
     * @return log file
     */
    public static File getLogFile() {
        File logFile = new File("Log.txt");
        // createNewFile checks if exists or not
        try {
            logFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return logFile;
    }

    /**
     * Reads the transactions that are already committed from the log file.
     * @return Map containing the already committed transactions, with the TA-ID as key and the last LSN of the TA as value.
     */
    public static Map<Integer, Integer> getCommittedTransactions() {
        try {
            return Files.readAllLines(getLogFile().toPath()).stream().map(LogEntry::getLogEntryFromLogLine).filter(LogEntry::isEot)
                    .collect(Collectors.toMap(LogEntry::getTaId, LogEntry::getLsn));
        } catch (IOException e) {
            throw new RuntimeException("Error on reading log file.", e);
        }
    }

    public void resetDB() {
        // clear logs
        File logFile = new File("Log.txt");
        logFile.delete();
        var dir = new File("files");
        try {
            var files = dir.listFiles();
            for(File file: files)
                if (!file.isDirectory())
                    file.delete();
        } catch (NullPointerException e) {
            // ignore
        }
    }
}
