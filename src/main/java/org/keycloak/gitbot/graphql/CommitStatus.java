package org.keycloak.gitbot.graphql;

public class CommitStatus {
    private Commit commit;

    public Commit getCommit() {
        return commit;
    }

    public void setCommit(Commit commit) {
        this.commit = commit;
    }

}
