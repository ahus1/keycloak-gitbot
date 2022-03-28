package org.keycloak.gitbot.graphql;

import java.util.List;

public class Reactions {
    private List<Reaction> nodes;

    public List<Reaction> getNodes() {
        return nodes;
    }

    public void setNodes(List<Reaction> nodes) {
        this.nodes = nodes;
    }
}
