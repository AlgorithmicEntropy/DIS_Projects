import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogEntry {

    public static final String EOT_LOG_ENTRY = "EOT";

    private int lsn;
    private int taId;
    private Integer pageId;
    private String data;
    private boolean eot;

    private LogEntry() {
    }

    public static LogEntry createEotLogEntry(int lsn, int taId) {
        LogEntry logEntry = new LogEntry();
        logEntry.lsn = lsn;
        logEntry.taId = taId;
        logEntry.eot = true;

        return logEntry;
    }

    public static LogEntry createDataLogEntry(int lsn, int taId, int pageId, String data) {
        LogEntry logEntry = new LogEntry();
        logEntry.lsn = lsn;
        logEntry.taId = taId;
        logEntry.pageId = pageId;
        logEntry.data = data;
        logEntry.eot = false;

        return logEntry;
    }

    public static LogEntry getLogEntryFromLogLine(String logLine) {
        LogEntry logEntry = new LogEntry();
        String[] splitEntry = logLine.split(PersistenceManager.SEPARATOR);

        logEntry.lsn = Integer.valueOf(splitEntry[0]);
        logEntry.taId = Integer.valueOf(splitEntry[1]);

        if(splitEntry.length == 3){
            logEntry.eot = true;
        } else {
            logEntry.pageId = Integer.valueOf(splitEntry[2]);
            logEntry.data = splitEntry[3];
            logEntry.eot = false;
        }

        return logEntry;
    }


    public String toLogString() {
        /* Assemble the log string. Different format depending on whether it is an eot log entry or a data log entry. */
        if (eot) {
            return String.join(PersistenceManager.SEPARATOR, String.valueOf(lsn), String.valueOf(taId), EOT_LOG_ENTRY);
        }
        return String.join(PersistenceManager.SEPARATOR, String.valueOf(lsn), String.valueOf(taId), String.valueOf(pageId), data);
    }

    public void writeLogEntry(File logFile) {
        // Add a new line to the log file to log the LSN and the given values
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            writer.newLine();
            writer.write(this.toLogString());
        } catch (IOException e) {
            throw new RuntimeException("Error on writing log entry.", e);
        }
    }

    public int getLsn() {
        return lsn;
    }

    public int getTaId() {
        return taId;
    }

    public boolean isEot() {
        return eot;
    }

    public Integer getPageId() {
        return pageId;
    }

    public String getData() {
        return data;
    }
}
