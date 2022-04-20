package org.keycloak.gitbot.model;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.keycloak.gitbot.commands.AddCommentCommand;
import org.keycloak.gitbot.commands.AddLabelCommand;
import org.keycloak.gitbot.commands.Command;
import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PullRequest {
    private static final Pattern KEYCLOAK_ISSUE = Pattern.compile("KEYCLOAK-\\d{3,4}");
    private static final PrettyTime PRETTY_TIME = new PrettyTime();
    private final org.keycloak.gitbot.graphql.PullRequest delegate;

    public Set<Command> getCommands() {
        return commands;
    }

    private final Set<Command> commands = new HashSet<>();

    public PullRequest(org.keycloak.gitbot.graphql.PullRequest delegate) {
        this.delegate = delegate;
    }

    public Integer getNumber() {
        return delegate.getNumber();
    }

    /**
     * Two PRs are the same if their number is the same.
     * This holds true as long as we don't mix projects.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PullRequest that = (PullRequest) o;
        return Objects.equals(delegate.getNumber(), that.delegate.getNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(delegate);
    }

    public String getTitle() {
        return delegate.getTitle();
    }

    public ZonedDateTime getCreatedAt() {
        return delegate.getCreatedAt();
    }

    public ZonedDateTime getUpdatedAt() {
        return delegate.getUpdatedAt();
    }

    public String getCreatedAtPretty() {
        return PRETTY_TIME.format(delegate.getCreatedAt());
    }

    public String getUpdatedAtPretty() {
        return PRETTY_TIME.format(delegate.getUpdatedAt());
    }

    public Author getAuthor() {
        return new Author(delegate.getAuthor());
    }

    public org.keycloak.gitbot.graphql.Author.CommentAuthorAssociation getAuthorAssociation() {
        return delegate.getAuthorAssociation();
    }

    public Stream<File> getFiles() {
        return delegate.getFiles().getNodes().stream().map(File::new);
    }

    public List<Issue> getClosingIssuesReferences() {
        return delegate.getClosingIssuesReferences().getNodes().stream().map(Issue::new).collect(Collectors.toList());
    }

    public Stream<Comment> getComments() {
        return delegate.getComments().getNodes().stream().map(Comment::new);
    }

    public List<Assignee> getAssignees() {
        return delegate.getAssignees().getNodes().stream().map(Assignee::new).collect(Collectors.toList());
    }

    public List<Label> getLabels() {
        return delegate.getLabels().getNodes().stream().map(Label::new).collect(Collectors.toList());
    }

    public boolean linkedIssueHasLabelGroupButPullRequestHasnt(String labelGroup) {
        List<Label> kindLabelsOfPr = getLabelGroup(labelGroup);
        if (kindLabelsOfPr.size() > 0) {
            return false;
        }
        List<Label> kindLabelsOfLinkedClosingIssue = getLabelGroupOfLinkedClosingIssue(labelGroup);
        return kindLabelsOfLinkedClosingIssue.size() > 0;
    }

    public List<Label> getLabelGroup(String labelGroup) {
        return getLabels().stream().filter(label -> label.getName().startsWith(labelGroup + "/")).collect(Collectors.toList());
    }

    public List<Label> getLabelGroupOfLinkedClosingIssue(String labelGroup) {
        return getClosingIssuesReferences().stream().flatMap(issue -> issue.getLabels().stream()).filter(label -> label.getName().startsWith(labelGroup + "/")).collect(Collectors.toList());
    }

    public void addCommand(Command command) {
        commands.add(command);
    }

    public boolean hasLinkedIssue() {
        return delegate.getClosingIssuesReferences().getNodes().size() > 0
                || hasLinkedJiraIssue();
    }

    public boolean hasLinkedJiraIssue() {
        return KEYCLOAK_ISSUE.matcher(delegate.getTitle()).find() || KEYCLOAK_ISSUE.matcher(delegate.getBody()).find();
    }

    public boolean hasCommands() {
        return commands.size() > 0;
    }

    public boolean hasSingleCommit() {
        return delegate.getCommits().getNodes().size() == 1;
    }

    public boolean hasFirstReviewComment() {
        return delegate.getComments() != null
                && delegate.getComments().getNodes().stream().anyMatch(comment -> comment.getBody().contains("Thanks for contributing to this project"));
    }

    public void analyze(Configuration configuration) throws TemplateException, IOException {
        AddLabelCommand.checkLinkedIssueHasAreaButPullRequestHasnt(this, "area");
        AddLabelCommand.checkLinkedIssueHasAreaButPullRequestHasnt(this, "kind");
        AddLabelCommand.checkPullRequestContainsOnlyDocFilesAndHasNoArea(this);
        AddLabelCommand.checkPullRequestContainsQuarkusChangesAndHasNoArea(this);
        AddLabelCommand.checkPullRequestTouchesSamlAndHasNoArea(this);
        AddLabelCommand.checkPullRequestContainsOnlyTranslations(this);
        AddCommentCommand.checkSingleCommitAndLinkedIssue(this, configuration);
    }
}
