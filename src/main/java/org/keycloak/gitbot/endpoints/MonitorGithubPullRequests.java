package org.keycloak.gitbot.endpoints;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import io.smallrye.graphql.client.GraphQLClient;
import io.smallrye.graphql.client.Response;
import io.smallrye.graphql.client.core.Argument;
import io.smallrye.graphql.client.core.Document;
import io.smallrye.graphql.client.dynamic.api.DynamicGraphQLClient;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.keycloak.gitbot.graphql.Result;
import org.keycloak.gitbot.graphql.Results;
import org.keycloak.gitbot.model.PullRequest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static io.smallrye.graphql.client.core.Document.document;
import static io.smallrye.graphql.client.core.Field.field;
import static io.smallrye.graphql.client.core.Operation.operation;


@Path("/monitor")
public class MonitorGithubPullRequests extends BaseEndpoint {

    private static final Logger LOG = Logger.getLogger(MonitorGithubPullRequests.class);

    @Inject
    @GraphQLClient("github")
    DynamicGraphQLClient dynamicClient;

    @Inject
    Configuration configuration;

    @ConfigProperty(name = "github.login")
    String gitHubLogin;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String listPullRequest() throws IOException, ExecutionException, InterruptedException, TemplateException {

        List<PullRequest> pullrequests = new LinkedList<>();

        for (String project : Arrays.asList("keycloak", "keycloak-benchmark")) {
            processQuery(project, pullrequests, document(
                    operation(
                            field("search",
                                    List.of(
                                            Argument.arg("type", Results.SearchType.ISSUE),
                                            Argument.arg("query", "is:open is:pr assignee:" + gitHubLogin.trim() + " archived:false repo:keycloak/" + project + " draft:false sort:updated-desc"), // reactions:>0
                                            Argument.arg("first", 50)
                                    ),
                                    commonFields()
                            )
                    )
            ));

            processQuery(project, pullrequests, document(
                    operation(
                            field("search",
                                    List.of(
                                            Argument.arg("type", Results.SearchType.ISSUE),
                                            Argument.arg("query", "is:open is:pr assignee:" + gitHubLogin.trim() + " archived:false repo:keycloak/" + project + " user-review-requested:@me"),
                                            Argument.arg("first", 50)
                                    ),
                                    commonFields()
                            )
                    )
            ));
        }

        pullrequests.sort(Comparator.comparing(PullRequest::getUpdatedAt).reversed());

        StringWriter stringWriter = new StringWriter();
        configuration.getTemplate("monitor.html.ftl").process(
                Map.of(
                        "pullrequests", pullrequests,
                        "title", "Pull requests assigned to you"
                ),
                stringWriter);
        return stringWriter.toString();
    }

    private void processQuery(String project, List<PullRequest> pullrequests, Document query) throws ExecutionException, InterruptedException, TemplateException, IOException {
        Response response = dynamicClient.executeSync(query);
        List<Result> search = response.getObject(Results.class, "search").getEdges();

        for (Result result : search) {
            PullRequest pullRequest = new PullRequest(project, result.getNode());
            if (pullrequests.contains(pullRequest)) {
                continue;
            }
            pullRequest.analyze(configuration);
            pullrequests.add(pullRequest);
        }
    }
}
