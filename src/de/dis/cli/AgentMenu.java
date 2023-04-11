package de.dis.cli;

import de.dis.data.EstateAgent;

public class AgentMenu extends AbstractMenu {

    final int BACK = 0;
    final int LOGOUT = 3;
    final int CREATE_ESTATE = 4;
    final int UPDATE_ESTATE = 5;
    final int DELETE_ESTATE = 6;

    public void showMenu() {
        Menu menu = new Menu("Agent menu");
        while(true) {
            menu.clear();
            menu.addEntry("Back", BACK);
            if (Session.getInstance().isAgent()) {
                menu.addEntry("Logout", LOGOUT);
                menu.addEntry("Add new state", this.CREATE_ESTATE);
                menu.addEntry("Edit estates", this.UPDATE_ESTATE);
                menu.addEntry("Delete estates", this.DELETE_ESTATE);
            } else {
                this.login();
                continue;
            }

            int response = menu.show();

            switch (response) {
                case BACK -> {
                    return;
                }
                case LOGOUT -> {
                    Session.getInstance().logoutAgent();
                    System.out.println("Logged out");
                    return;
                }
                case CREATE_ESTATE -> {
                    this.addEstate();
                }
            }
        }
    }

    private void login() {
        var login = FormUtil.readString("Login");
        var password = FormUtil.readString("Password");

        var agent = EstateAgent.fromLogin(login);
        if (agent == null) {
            System.out.println("Invalid login");
        }
        if (!agent.getPassword().equals(password)) {
            System.out.println("Wrong password");
            return;
        }
        Session.getInstance().loginAgent(agent);
        System.out.println("Logged in as " + agent.getLogin());
    }

    private void addEstate() {

    }


}
