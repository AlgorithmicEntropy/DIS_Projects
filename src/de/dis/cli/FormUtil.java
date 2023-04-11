package de.dis.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.Date;

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

	public static int changeInt(String settingName, int oldValue) {
		var label = "Enter new value for " + settingName + " (was: " + oldValue + ")";
		int ret = oldValue;
		boolean finished = false;

		while(!finished) {
			String line = readString(label);
			if (line.equals("")) {
				return oldValue;
			}
			try {
				ret = Integer.parseInt(line);
				finished = true;
			} catch (NumberFormatException e) {
				System.err.println("Ungültige Eingabe: Bitte geben Sie eine Zahl an!");
			}
		}
		return ret;
	}

	public static double changeDouble(String settingName, double oldValue) {
		var label = "Enter new value for " + settingName + " (was: " + oldValue + ")";
		double ret = oldValue;
		boolean finished = false;

		while(!finished) {
			String line = readString(label);
			if (line.equals("")) {
				return oldValue;
			}
			try {
				ret = Double.parseDouble(line);
				finished = true;
			} catch (NumberFormatException e) {
				System.err.println("Ungültige Eingabe: Bitte geben Sie eine Zahl an!");
			}
		}
		return ret;
	}

	public static boolean changeBoolean(String settingName, boolean oldValue) {
		var label = "Enter new value for " + settingName + " (was: " + oldValue + ")";
		boolean ret = oldValue;
		boolean finished = false;

		while(!finished) {
			String line = readString(label);
			if (line.equals("")) {
				return oldValue;
			}
			if ("y".equals(line)) {
				ret = true;
				finished = true;
			} else if ("n".equals(line)) {
				finished = true;
			} else {
				System.err.println("Ungültige Eingabe: Bitte geben Sie y/n an!");
			}
		}
		return ret;
	}

    public static double readDouble(String label) {
		double ret = 0;
		boolean finished = false;

		while(!finished) {
			String line = readString(label);
			try {
				ret = Double.parseDouble(line);
				finished = true;
			} catch (NumberFormatException e) {
				System.err.println("Ungültige Eingabe: Bitte geben Sie eine Zahl an!");
			}
		}
		return ret;
    }

	public static boolean readBoolean(String label) {
		boolean ret = false;
		boolean finished = false;

		while(!finished) {
			String line = readString(label + " (y/n)");
			if ("y".equals(line)) {
				ret = true;
				finished = true;
			} else if ("n".equals(line)) {
				finished = true;
			} else {
				System.err.println("Ungültige Eingabe: Bitte geben Sie eine Zahl an!");
			}
		}

		return ret;
	}

	public static Date readDate(String contractDate) {
		// TODO implement me
		return new Date();
	}

	public static Duration readDuration(String contractDuration) {
		// TODO implement me
		return Duration.ofHours(2);
	}

	public static void waitForInput() {
		try {
			System.in.read();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
