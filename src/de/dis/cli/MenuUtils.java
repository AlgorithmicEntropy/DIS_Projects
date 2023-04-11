package de.dis.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MenuUtils {
    private static BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

    public static String ChangeString(String settingName, String oldValue) {
        System.out.println("Enter new value for " + settingName + " (was: " + oldValue + ")");
        try {
            var newVal = stdin.readLine();
            if (!newVal.equals("")) {
                return newVal;
            } else {
                return oldValue;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Integer ChangeInt(String settingName, int oldValue) {
        System.out.println("Enter new value for " + settingName + " (was: " + oldValue + ")");
        try {
            String newVal = stdin.readLine();
            if (!newVal.equals("")) {
                return Integer.parseInt(newVal);
            } else {
                return oldValue;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
