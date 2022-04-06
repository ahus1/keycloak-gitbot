package org.keycloak.gitbot.graphql;

public class Author {
    private String login;
    private String avatarUrl;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public enum CommentAuthorAssociation {
        COLLABORATOR("Collaborator",
                "Author has been invited to collaborate on the repository."),
        CONTRIBUTOR("Contributor",
                "Author has previously committed to the repository."),
        FIRST_TIMER("First Timer",
                "Author has not previously committed to GitHub."),
        FIRST_TIME_CONTRIBUTOR("First Time Contributor", "Author has not previously committed to the repository."),
        MANNEQUIN("Mannequin",
                "Author is a placeholder for an unclaimed user."),
        MEMBER("Member",
                "Author is a member of the organization that owns the repository."),
        NONE("No association",
                "Author has no association with the repository."),
        OWNER("Owner",
                "Author is the owner of the repository.");

        private final String role;
        private final String description;

        CommentAuthorAssociation(String role, String description) {
            this.role = role;
            this.description = description;
        }

        public String getRole() {
            return role;
        }

        public String getDescription() {
            return description;
        }
    }

}
