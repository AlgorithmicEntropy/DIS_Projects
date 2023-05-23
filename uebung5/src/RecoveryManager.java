import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public class RecoveryManager {

    public static void recover() {
        // Read the log
        List<LogEntry> logEntries = null;
        try {
            logEntries = Files.readAllLines(PersistenceManager.getLogFile().toPath()).stream().map(LogEntry::getLogEntryFromLogLine).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error on reading LogFile.", e);
        }

        // Find out which are the winner transactions
        List<Integer> winnerTas = logEntries.stream().filter(LogEntry::isEot).map(LogEntry::getTaId)
                .collect(Collectors.toList());

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
        PersistenceManager manager = PersistenceManager.getInstance();

        /* FIXME CB how do we deal with commit at this point? Do we redo it as well? Do we have to check whether the commit actually "came through"? */
        if (logEntry.isEot()) {
            // manager.commit(logEntry.getTaid());
            return;
        }

        // Find page file for the page of the current log entry
        File pagefile = manager.getPageFileById(logEntry.getPageId());
        List<String> pageLines;
        try {
            pageLines = Files.readAllLines(pagefile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Check if we need to redo
        if(pageLines.isEmpty() || Integer.valueOf(pageLines.get(0).split(PersistenceManager.SEPARATOR)[0]) < logEntry.getLsn()){
            /* FIXME This could go quite wrong since we increase the lsn here. That IS what we need to do,
                if i understand the principle correctly, but I'm note sure if we get issues further on if we need to redo more stuff
                and the buffer has already written the higher lsn ... we might need a redo instead of using the same write.
                I'd have to look up the redo stuff.
               */
            manager.write(logEntry.getTaId(), logEntry.getPageId(), logEntry.getData());
        }


    }
}
