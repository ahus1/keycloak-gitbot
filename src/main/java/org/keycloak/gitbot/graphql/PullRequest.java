package org.keycloak.gitbot.graphql;

import org.eclipse.microprofile.graphql.Name;

public class PullRequest {
    private Integer number;
    private String title;
    private Reactions reactions;

    private FilesNodes files;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Reactions getReactions() {
        return reactions;
    }

    public void setReactions(Reactions reactions) {
        this.reactions = reactions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public FilesNodes getFiles() {
        return files;
    }

    public void setFiles(FilesNodes files) {
        this.files = files;
    }
}
