package org.keycloak.gitbot.graphql;

import org.eclipse.microprofile.graphql.Enum;

import java.util.List;

public class Search {

    private List<SearchEdges> edges;

    public List<SearchEdges> getEdges() {
        return edges;
    }

    public void setEdges(List<SearchEdges> edges) {
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
