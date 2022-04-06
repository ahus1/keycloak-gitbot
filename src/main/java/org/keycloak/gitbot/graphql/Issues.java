package org.keycloak.gitbot.graphql;

import java.util.List;

public class Issues {
    private List<Issue> nodes;

    public List<Issue> getNodes() {
        return nodes;
    }

    public void setNodes(List<Issue> nodes) {
        this.nodes = nodes;
    }
}
