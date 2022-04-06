package org.keycloak.gitbot.graphql;

import java.util.List;

public class Assignees {

    private List<Assignee> nodes;

    public List<Assignee> getNodes() {
        return nodes;
    }

    public void setNodes(List<Assignee> nodes) {
        this.nodes = nodes;
    }
}
