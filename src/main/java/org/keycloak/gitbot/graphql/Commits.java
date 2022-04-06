package org.keycloak.gitbot.graphql;

import java.util.List;

public class Commits {
    private List<CommitStatus> nodes;

    public List<CommitStatus> getNodes() {
        return nodes;
    }

    public void setNodes(List<CommitStatus> nodes) {
        this.nodes = nodes;
    }
}
