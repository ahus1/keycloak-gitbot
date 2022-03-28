package org.keycloak.gitbot.graphql;

public class SearchEdges {
    private PullRequest node;

    public PullRequest getNode() {
        return node;
    }

    public void setNode(PullRequest node) {
        this.node = node;
    }
}
