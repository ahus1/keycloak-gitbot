package org.keycloak.gitbot.model;

public class Comment {
    private final org.keycloak.gitbot.graphql.Comment delegate;

    public Comment(org.keycloak.gitbot.graphql.Comment delegate) {
        this.delegate = delegate;
    }

    public String getBody() {
        return delegate.getBody();
    }

    public Author getAuthor() {
        return new Author(delegate.getAuthor());
    }
}
