package de.dis.cli;

public class MainMenu {
    final AgentMenu agentMenu = new AgentMenu();
    final RootMenu rootMenu = new RootMenu();

    final int QUIT = 0;
    final int LOGIN_AGENT = 1;

    final int LOGIN_ROOT = 2;

    public void showMenu() {
        var menu = new Menu("Estate Management Tool v0.1");

        while (true) {
            menu.clear();
            menu.addEntry("Quit", QUIT);
            var session = Session.getInstance();
            if (session.isRoot()) {
                menu.addEntry("Root menu", LOGIN_ROOT);
            } else {
                menu.addEntry("Login as ROOT", LOGIN_ROOT);
            }
            if (session.isAgent()) {
                menu.addEntry("Agent Menu", LOGIN_AGENT);
            } else {
                menu.addEntry("Login as Agent", LOGIN_AGENT);
            }

            var response = menu.show();
            switch (response) {
                case QUIT -> {
                    return;
                }
                case LOGIN_AGENT -> {
                    agentMenu.showMenu();
                }
                case LOGIN_ROOT -> {
                    rootMenu.showMenu();
                }
            }
        }
    }
}
