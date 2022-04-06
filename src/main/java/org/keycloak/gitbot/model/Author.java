package org.keycloak.gitbot.model;

public class Author {
    private final org.keycloak.gitbot.graphql.Author delegate;

    public Author(org.keycloak.gitbot.graphql.Author delegate) {
        this.delegate = delegate;
    }

    public String getLogin() {
        return delegate.getLogin();
    }

    public String getAvatarUrl() {
        return delegate.getAvatarUrl();
    }

}
