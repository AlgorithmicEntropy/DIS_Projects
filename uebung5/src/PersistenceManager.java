import java.io.*;
import java.nio.file.Files;
import java.text.MessageFormat;
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
    private Hashtable<Integer, String> buffer = new Hashtable<>();


    private PersistenceManager() {
        System.out.println("PersistenceManager was created.");
    }

    /**
     * @return the single existing persistence manager
     */
    public static PersistenceManager getInstance() {
        return manager;
    }

    public int beginTransaction() {
        transactionId++;
        System.out.println("Transaction " + transactionId + " started.");
        return transactionId;
    }

    public void commit(int taid) {
        // FIXME CB: Implement commit? Do we only log that and save it persistently with the next write?!?

        System.out.println("Transaction " + transactionId + " commited.");

        LogEntry eotLogEntry = LogEntry.createEotLogEntry(lsn++, taid);
        eotLogEntry.writeLogEntry(getLogFile());
    }

    public void write(int taid, int pageId, String data) {
        // FIXME CB: Is that correct?
        buffer.put(pageId, data);

        System.out.println(MessageFormat.format("adding to buffer: taid={0}, pageId={1}, data={2}", taid, pageId, data));

        LogEntry dataLogEntry = LogEntry.createDataLogEntry(lsn++, taid, pageId, data);
        dataLogEntry.writeLogEntry(getLogFile());

        // Write buffer data of committed transactions to storage if the buffer contains more than five data sets
        if (buffer.size() > 5) {
            writeBufferToPersistentStorage();

        }
    }

    private void writeBufferToPersistentStorage() {
        // FIXME CB: Is that the way to go to find out which transactions we already committed?
        Map<Integer, Integer> committedTAs = getCommittedTransactions();

        for (Object entry : buffer.entrySet()) {
            // FIXME CB: read page-ID, TA-ID and data from entry
            int pageId = -1;
            int taId = -1;
            String data = "FIXME";

            // FIXME CB: check entry against committed TAs
            if (false // committedTAs.contains(entry.taid)
             ) {

                int lsn = committedTAs.get(taId);
                File pageFile = getPageFileById(pageId);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(pageFile, false))) {
                    writer.write(lsn);
                    writer.write(SEPARATOR);
                    writer.write(data);
                } catch (IOException e) {
                    throw new RuntimeException("Error on writing data to persistent storage.", e);
                }
            }
        }
    }




    /**
     * Returns the page file for the given page id. If the file does not yet exist, the file, and it's parent directories are created.
     *
     * @param id Page ID
     * @return page file
     */
    public File getPageFileById(int id) {
        File pageFile = new File("files" + File.separator + "Page_" + id + ".txt");
        if (!pageFile.exists()) {
            try {
                pageFile.getParentFile().mkdirs();
                pageFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Error on creating page file.", e);
            }
        }
        return pageFile;
    }

    /**
     * Returns the log file. If the file does not yet exist, the file, and it's parent directories are created.
     *
     * @return log file
     */
    public static File getLogFile() {
        File logFile = new File("Log.txt");
        if (!logFile.exists()) {
            try {
                logFile.getParentFile().mkdirs();
                logFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Error on creating log file.", e);
            }
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

}
