package org.keycloak.gitbot.graphql;

import java.util.List;

public class Comments {
    private List<Comment> nodes;

    public List<Comment> getNodes() {
        return nodes;
    }

    public void setNodes(List<Comment> nodes) {
        this.nodes = nodes;
    }
}
