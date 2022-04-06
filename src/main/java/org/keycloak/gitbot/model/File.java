package org.keycloak.gitbot.model;

public class File {
    private final org.keycloak.gitbot.graphql.File delegate;

    public File(org.keycloak.gitbot.graphql.File delegate) {
        this.delegate = delegate;
    }

    public String getPath() {
        return delegate.getPath();
    }
}
