package de.dis;

public class Main {
    public static void main(String[] args) {
        // reset db
        PersistenceManager.getInstance().resetDB();

        var s1 = new String[]{"1:Hallo", "5:Welt"};
        var s2 = new String[]{"11:Welt", "12:Hallo", "00:ABORT"}; // this T will never commit -> loser TA
        var s3 = new String[]{"21:Hallo", "23:Test", "21:Welt"};

        var client1 = new Client();
        var client2 = new Client();
        var client3 = new Client();

        new Thread(() -> client1.RunSchedule(s1)).start();
        new Thread(() -> client2.RunSchedule(s2)).start();
        new Thread(() -> client3.RunSchedule(s3)).start();
    }
}
