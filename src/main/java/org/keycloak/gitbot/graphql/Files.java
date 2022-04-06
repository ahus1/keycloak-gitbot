package org.keycloak.gitbot.graphql;

import java.util.List;

public class Files {
    private List<File> nodes;

    public List<File> getNodes() {
        return nodes;
    }

    public void setNodes(List<File> nodes) {
        this.nodes = nodes;
    }
}
