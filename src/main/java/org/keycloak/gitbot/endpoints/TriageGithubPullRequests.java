package org.keycloak.gitbot.endpoints;

import freemarker.core.TemplateDateFormatFactory;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import io.smallrye.graphql.client.GraphQLClient;
import io.smallrye.graphql.client.Response;
import io.smallrye.graphql.client.core.Argument;
import io.smallrye.graphql.client.core.Document;
import io.smallrye.graphql.client.dynamic.api.DynamicGraphQLClient;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.keycloak.gitbot.commands.AddCommentCommand;
import org.keycloak.gitbot.commands.AddLabelCommand;
import org.keycloak.gitbot.freemarker.PrettyDateFormatFactory;
import org.keycloak.gitbot.graphql.Result;
import org.keycloak.gitbot.graphql.Results;
import org.keycloak.gitbot.model.PullRequest;
import org.kohsuke.github.*;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static io.smallrye.graphql.client.core.Document.document;
import static io.smallrye.graphql.client.core.Field.field;
import static io.smallrye.graphql.client.core.Operation.operation;


@Path("/triage")
public class TriageGithubPullRequests extends BaseEndpoint {

    private static final Logger LOG = Logger.getLogger(TriageGithubPullRequests.class);

    @Inject
    @GraphQLClient("github")
    DynamicGraphQLClient dynamicClient;

    @Inject
    Configuration configuration;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String listPullRequest() throws IOException, ExecutionException, InterruptedException, TemplateException {
        Document query = document(
                operation(
                        field("search",
                                List.of(
                                        Argument.arg("type", Results.SearchType.ISSUE),
                                        Argument.arg("query", "is:open is:pr archived:false repo:keycloak/keycloak draft:false sort:updated-desc no:assignee"),
                                        Argument.arg("first", 100)
                                ),
                                commonFields()
                        )
                )
        );
        Response response = dynamicClient.executeSync(query);
        List<Result> search = response.getObject(Results.class, "search").getEdges();

        List<PullRequest> pullrequests = new ArrayList<>();

        for (Result result : search) {
            PullRequest pullRequest = new PullRequest(result.getNode());
            pullRequest.analyze(configuration);
            if (pullRequest.hasCommands()) {
                pullrequests.add(pullRequest);
            } else if (pullRequest.getFiles().count() == 1) {
                // these are small PRs, list them as well
                pullrequests.add(pullRequest);
            }
        }

        StringWriter stringWriter = new StringWriter();
        configuration.getTemplate("monitor.html.ftl").process(
                Map.of(
                        "pullrequests", pullrequests,
                        "title", "Pull requests with suggested actions"
                ),
                stringWriter);
        return stringWriter.toString();
    }

}
