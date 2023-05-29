package de.dis;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public class RecoveryManager {

    public static void main(String[] args) {
        recover();
    }

    public static void recover() {
        // Read the log
        List<LogEntry> logEntries;
        try {
            logEntries = Files.readAllLines(PersistenceManager.getLogFile().toPath()).stream().map(LogEntry::getLogEntryFromLogLine).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error on reading LogFile.", e);
        }

        // Find out which are the winner transactions
        List<Integer> winnerTas = logEntries.stream().filter(LogEntry::isEot).map(LogEntry::getTaId).toList();

        // Go through log and redo if necessary
        for (LogEntry logEntry : logEntries) {
            // Skip loser-TAs
            if (!winnerTas.contains(logEntry.getTaId())) {
                continue;
            }
            redoIfNecessary(logEntry);
        }
    }

    private static void redoIfNecessary(LogEntry logEntry) {
        if (logEntry.isEot()) {
            // commit was used to determine winner transactions, but did not modify the database
            // -> nothing to redo for this log entry
            System.out.printf("Nothing to redo (COMMIT) for log entry %s%n", logEntry.getLsn());
            return;
        }

        // Find page file for the page of the current log entry
        PageFile pagefile = new PageFile(logEntry.getPageId());
        List<String> pageLines = pagefile.getContent();

        // Check if we need to redo by comparing LSNs
        if(pageLines.isEmpty() || Integer.parseInt(pageLines.get(0).split(PersistenceManager.SEPARATOR)[0]) < logEntry.getLsn()){
            // Write directly to page file cause winner transactions are committed
            // and can therefore be written to persistent storage -> no need to buffer
            pagefile.save(logEntry.getLsn(), logEntry.getData());
            System.out.printf("Redo for log entry %s, pageID: %s, data: %s%n", logEntry.getLsn(), logEntry.getPageId(), logEntry.getData());
        } else {
            System.out.printf("Nothing to redo for log entry %s since page lsn is higher than / equal to log entry lsn.%n", logEntry.getLsn());
        }
    }
}
