package org.keycloak.gitbot.graphql;

import org.eclipse.microprofile.graphql.Enum;

import java.util.List;

public class Results {

    private List<Result> edges;

    public List<Result> getEdges() {
        return edges;
    }

    public void setEdges(List<Result> edges) {
        this.edges = edges;
    }

    @Enum
    public
    enum SearchType {
        DISCUSSION,
        ISSUE,
        REPOSITORY,
        USER
    }


}
