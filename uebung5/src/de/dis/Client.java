package de.dis;

import java.util.concurrent.TimeUnit;

public class Client {
    private PersistenceManager persistenceManager;
    public Client() {
        this.persistenceManager = PersistenceManager.getInstance();
    }

    public void RunSchedule(String[] s) {
        int taid = persistenceManager.beginTransaction();
        for (String entry : s) {
            String[] splitEntry = entry.split(":");
            int pageId = Integer.parseInt(splitEntry[0]);
            String data = splitEntry[1];

            if (data.equals("ABORT")) {
                // return so we never commit this T
                return;
            }

            persistenceManager.write(taid, pageId, data);
            // sleep
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        persistenceManager.commit(taid);
    }
}
