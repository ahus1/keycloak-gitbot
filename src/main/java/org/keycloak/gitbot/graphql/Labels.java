package org.keycloak.gitbot.graphql;

import java.util.List;

public class Labels {
    private List<Label> nodes;

    public List<Label> getNodes() {
        return nodes;
    }

    public void setNodes(List<Label> nodes) {
        this.nodes = nodes;
    }
}
