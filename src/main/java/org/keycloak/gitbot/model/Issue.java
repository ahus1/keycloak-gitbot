package org.keycloak.gitbot.model;

import org.keycloak.gitbot.graphql.Labels;

import java.util.List;
import java.util.stream.Collectors;

public class Issue {
    private final org.keycloak.gitbot.graphql.Issue delegate;

    public Issue(org.keycloak.gitbot.graphql.Issue delegate) {
        this.delegate = delegate;
    }

    public String getName() {
        return delegate.getName();
    }

    public Integer getNumber() {
        return delegate.getNumber();
    }

    public String getUrl() {
        return delegate.getUrl();
    }

    public List<Label> getLabels() {
        return delegate.getLabels().getNodes().stream().map(Label::new).collect(Collectors.toList());
    }
}
