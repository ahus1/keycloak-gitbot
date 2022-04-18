package org.keycloak.gitbot.commands;

import org.keycloak.gitbot.model.Label;
import org.keycloak.gitbot.model.PullRequest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class AddLabelCommand extends Command {
    private final Label label;

    public AddLabelCommand(PullRequest pullRequest, Label label) {
        super(pullRequest);
        this.label = label;
        pullRequest.addCommand(this);
    }

    public String getDescription() {
        return "Add label '" + label.getName() + "'";
    }

    public String getUrl() {
        return "/prs/" + getPr().getNumber() + "/addLabel?label=" + URLEncoder.encode(label.getName(), StandardCharsets.UTF_8);
    }

    public static void checkPullRequestContainsOnlyTranslations(PullRequest pullRequest) {
        if (pullRequest.getFiles().allMatch(file ->
                file.getPath().startsWith("themes/src/main/resources-community/theme/")
                        || file.getPath().matches("themes/src/main/resources/theme/.*/messages_en.properties")) && pullRequest.getLabelGroup("area").size() == 0) {
            new AddLabelCommand(pullRequest, Label.AREA_TRANSLATIONS);
        }
    }
    public static void checkPullRequestContainsOnlyDocFilesAndHasNoArea(PullRequest pullRequest) {
        if (pullRequest.getFiles().allMatch(file -> file.getPath().endsWith(".adoc")) && pullRequest.getLabelGroup("area").size() == 0) {
            new AddLabelCommand(pullRequest, Label.AREA_DOCS);
        }
    }

    public static void checkLinkedIssueHasAreaButPullRequestHasnt(PullRequest pullRequest, String labelArea) {
        if (pullRequest.linkedIssueHasLabelGroupButPullRequestHasnt(labelArea)) {
            new AddLabelCommand(pullRequest, pullRequest.getLabelGroupOfLinkedClosingIssue(labelArea).get(0));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AddLabelCommand that = (AddLabelCommand) o;
        return Objects.equals(label, that.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label);
    }
}
