package de.dis.cli;

import de.dis.FormUtil;
import de.dis.data.EstateAgent;

public class AgentMenu {

    public static void newAgent() {
        EstateAgent m = new EstateAgent();

        m.setName(FormUtil.readString("Name"));
        m.setAddress(FormUtil.readString("Address"));
        m.setLogin(FormUtil.readString("Login"));
        m.setPassword(FormUtil.readString("Password"));
        m.save();

        System.out.println("Created agent with id "+m.getId());
    }

    public void editAgent(int id) {
        EstateAgent agent = EstateAgent.load(id);
        String newName = MenuUtils.ChangeString("Name", agent.getName());
        String newAddress = MenuUtils.ChangeString("Address", agent.getAddress());
        String newLogin = MenuUtils.ChangeString("Login", agent.getLogin());
        String newPassword = MenuUtils.ChangeString("Password", agent.getPassword());

        agent.setName(newName);
        agent.setAddress(newAddress);
        agent.setLogin(newLogin);
        agent.setPassword(newPassword);

        agent.save();
    }
}
