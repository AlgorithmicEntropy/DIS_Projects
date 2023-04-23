package de.dis.cli;

import de.dis.core.EstateService;
import de.dis.data.EstateAgent;

public class RootMenu extends AbstractMenu {
    final int BACK = 0;

    final int NEW_AGENT = 1;

    final int EDIT_AGENT = 2;

    final int DELETE_AGENT = 3;

    final int LOGOUT = 4;

    public void showMenu() {
        Menu menu = new Menu("Root menu");

        while (true) {
            menu.clear();
            menu.addEntry("Back", BACK);
            if (Session.getInstance().isRoot()) {
                menu.addEntry("Logout", this.LOGOUT);
                menu.addEntry("Add agent", this.NEW_AGENT);
                menu.addEntry("Edit agent", this.EDIT_AGENT);
                menu.addEntry("Delete agent", this.DELETE_AGENT);
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
                    this.logout();
                    return;
                }
                case EDIT_AGENT -> {
                    var agent = this.showAgentMenu();
                    if (agent == null) {
                        continue;
                    }
                    this.editAgent(agent);
                }
                case NEW_AGENT -> {
                    this.addAgent();
                }
                case DELETE_AGENT -> {
                    var agent = this.showAgentMenu();
                    this.deleteAgent(agent);
                }
            }

        }
    }

    private void deleteAgent(EstateAgent agent) {
        EstateService service = EstateService.getInstance();
        if(!service.getAllHousesForEstateAgent(agent).isEmpty()) {
            System.out.println("The agent has at least one house and therefore can't be deleted.");
        } else if(!service.getAllApartmentsForEstateAgent(agent).isEmpty()) {
            System.out.println("The agent has at least one apartment and therefore can't be deleted.");
        } else{
            service.delete(agent);
        }
    }

    private void logout() {
        Session.getInstance().logoutRoot();
        System.out.println("Logged out");
    }

    private void login() {
        var password = FormUtil.readString("Password");
        if (!password.equals("password")) {
            System.out.println("Invalid password");
            return;
        }
        Session.getInstance().loginRoot();
        System.out.println("Logged in as root");
    }

    private void addAgent() {
        EstateAgent m = new EstateAgent();

        m.setName(FormUtil.readString("Name"));
        m.setAddress(FormUtil.readString("Address"));
        m.setLogin(FormUtil.readString("Login"));
        m.setPassword(FormUtil.readString("Password"));
        EstateService.getInstance().persist(m);

        System.out.println("Created agent with id "+m.getId());
    }

    private EstateAgent showAgentMenu() {
        var agents = EstateService.getInstance().getAllEstateAgents();
        if (agents.size() == 0) {
            System.out.println("No agents found");
            return null;
        }
        var menu = new Menu("Select an agent");
        for (int i = 0; i < agents.size(); i++) {
            menu.addEntry(agents.get(i).getName(), i);
        }
        var response = menu.show();
        return agents.get(response);
    }

    private void editAgent(EstateAgent agent) {
        String newName = FormUtil.changeString("Name", agent.getName());
        String newAddress = FormUtil.changeString("Address", agent.getAddress());
        String newLogin = FormUtil.changeString("Login", agent.getLogin());
        String newPassword = FormUtil.changeString("Password", agent.getPassword());

        agent.setName(newName);
        agent.setAddress(newAddress);
        agent.setLogin(newLogin);
        agent.setPassword(newPassword);

        EstateService.getInstance().update(agent);
        System.out.println("Updated user values");
    }

}
