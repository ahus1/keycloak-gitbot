package org.keycloak.gitbot.commands;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.keycloak.gitbot.model.PullRequest;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

public class AddCommentCommand extends Command {


    private final String comment;

    public AddCommentCommand(PullRequest pr, String comment) {
        super(pr);
        this.comment = comment;
        pr.addCommand(this);
    }

    @Override
    public String getDescription() {
        return "Add comment";
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public String getUrl() {
        return "/prs/" + getPr().getNumber() + "/addComment";
    }

    public static void checkSingleCommitAndLinkedIssue(PullRequest pullRequest, Configuration configuration) throws IOException, TemplateException {
        if (pullRequest.getAuthor().getLogin().equals("dependabot")) {
            // ignore these automated pull requests as this will have no effect.
            return;
        }
        boolean missingLinkedIssue = !pullRequest.hasLinkedIssue();
        boolean notSingleCommit = !pullRequest.hasSingleCommit();
        boolean notHadFirstReviewComment = !pullRequest.hasFirstReviewComment();
        if ((missingLinkedIssue || notSingleCommit) && notHadFirstReviewComment) {
            StringWriter stringWriter = new StringWriter();
            configuration.getTemplate("formalReviewPullRequest.md.ftl").process(Map.of(
                    "missingLinkedIssue", missingLinkedIssue,
                    "hasLinkedJiraIssue", pullRequest.hasLinkedJiraIssue(),
                    "notSingleCommit", notSingleCommit
            ), stringWriter);
            new AddCommentCommand(pullRequest, stringWriter.toString().replaceAll("\n{4,99}", "\n"));
        }
    }

}
