package org.keycloak.gitbot.commands;

import org.keycloak.gitbot.model.PullRequest;

public abstract class Command {
    private final PullRequest pr;

    public Command(PullRequest pr) {
        this.pr = pr;
    }

    public PullRequest getPr() {
        return pr;
    }
    public abstract String getDescription();

    public String getComment() {
        return null;
    }

    public abstract String getUrl();

}
