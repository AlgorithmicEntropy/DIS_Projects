package de.dis.cli;

import de.dis.data.EstateAgent;

public class Session {
    private static Session instance;
    private EstateAgent agent;
    private boolean isRoot = false;

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public void loginAgent(EstateAgent agent) {
        this.agent = agent;
    }

    public void logoutAgent() {
        this.agent = null;
    }

    public boolean isAgent() {
        return this.agent != null;
    }

    public EstateAgent getAgent() {
        return this.agent;
    }

    public void loginRoot() {
        this.isRoot = true;
    }

    public void logoutRoot() {
        this.isRoot = false;
    }

    public boolean isRoot() {
        return this.isRoot;
    }
}
