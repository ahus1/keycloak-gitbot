package org.keycloak.gitbot.graphql;

public class Result {
    private PullRequest node;

    public PullRequest getNode() {
        return node;
    }

    public void setNode(PullRequest node) {
        this.node = node;
    }
}
