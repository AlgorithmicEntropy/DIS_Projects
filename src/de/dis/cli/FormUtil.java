package de.dis.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Kleine Helferklasse zum Einlesen von Formulardaten
 */
public class FormUtil {

	private static final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));


	/**
	 * Liest einen String vom standard input ein
	 * @param label Zeile, die vor der Eingabe gezeigt wird
	 * @return eingelesene Zeile
	 */
	public static String readString(String label) {
		String ret = null;
		try {
			System.out.print(label+": ");
			ret = stdin.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	/**
	 * Liest einen Integer vom standard input ein
	 * @param label Zeile, die vor der Eingabe gezeigt wird
	 * @return eingelesener Integer
	 */
	public static int readInt(String label) {
		int ret = 0;
		boolean finished = false;

		while(!finished) {
			String line = readString(label);
			
			try {
				ret = Integer.parseInt(line);
				finished = true;
			} catch (NumberFormatException e) {
				System.err.println("Ungültige Eingabe: Bitte geben Sie eine Zahl an!");
			}
		}
		
		return ret;
	}

	public static String changeString(String settingName, String oldValue) {
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

	public static Integer changeInt(String settingName, int oldValue) {
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
