package org.keycloak.gitbot.graphql;

import java.time.ZonedDateTime;

public class PullRequest {
    private Integer number;
    private String title;
    private String body;
    private Author author;
    private Reactions reactions;
    private Commits commits;
    private Labels labels;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private Author.CommentAuthorAssociation authorAssociation;
    private Files files;
    private Comments comments;
    private Issues closingIssuesReferences;

    private Assignees assignees;

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

    public Files getFiles() {
        return files;
    }

    public void setFiles(Files files) {
        this.files = files;
    }

    public Comments getComments() {
        return comments;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Author.CommentAuthorAssociation getAuthorAssociation() {
        return authorAssociation;
    }

    public void setAuthorAssociation(Author.CommentAuthorAssociation authorAssociation) {
        this.authorAssociation = authorAssociation;
    }

    public Assignees getAssignees() {
        return assignees;
    }

    public void setAssignees(Assignees assignees) {
        this.assignees = assignees;
    }

    public void setComments(Comments comments) {
        this.comments = comments;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Labels getLabels() {
        return labels;
    }

    public void setLabels(Labels labels) {
        this.labels = labels;
    }

    public Issues getClosingIssuesReferences() {
        return closingIssuesReferences;
    }

    public void setClosingIssuesReferences(Issues closingIssuesReferences) {
        this.closingIssuesReferences = closingIssuesReferences;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Commits getCommits() {
        return commits;
    }

    public void setCommits(Commits commits) {
        this.commits = commits;
    }
}
