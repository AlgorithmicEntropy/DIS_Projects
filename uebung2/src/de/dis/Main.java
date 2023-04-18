package de.dis;

import de.dis.cli.MainMenu;

/**
 * Hauptklasse
 */
public class Main {

	private static final MainMenu mainMenu = new MainMenu();

	/**
	 * Startet die Anwendung
	 */
	public static void main(String[] args) {
		mainMenu.showMenu();
	}

}
