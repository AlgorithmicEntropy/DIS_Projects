package de.dis;

import de.dis.cli.MainMenu;

/**
 * Main class showing the main menu
 */
public class Main {
	private static final MainMenu mainMenu = new MainMenu();
	/**
	 * Startet die Anwendung
	 */
	public static void main(String[] args) {
		mainMenu.showMenu();
		System.exit(0);
	}
}
