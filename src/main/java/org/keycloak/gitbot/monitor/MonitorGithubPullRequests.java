package org.keycloak.gitbot.monitor;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import io.smallrye.graphql.client.GraphQLClient;
import io.smallrye.graphql.client.Response;
import io.smallrye.graphql.client.core.Argument;
import io.smallrye.graphql.client.core.Document;
import io.smallrye.graphql.client.dynamic.api.DynamicGraphQLClient;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.keycloak.gitbot.graphql.PullRequest;
import org.keycloak.gitbot.graphql.Search;
import org.keycloak.gitbot.graphql.SearchEdges;
import org.kohsuke.github.*;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static io.smallrye.graphql.client.core.Document.document;
import static io.smallrye.graphql.client.core.Field.field;
import static io.smallrye.graphql.client.core.Operation.*;


@Path("/monitor")
public class MonitorGithubPullRequests {

    private static final Logger LOG = Logger.getLogger(MonitorGithubPullRequests.class);

    // @Scheduled(concurrentExecution = Scheduled.ConcurrentExecution.SKIP, every = "10s")

    @Inject
    @GraphQLClient("github")
    DynamicGraphQLClient dynamicClient;

    private GitHub gitHub;
    private GHRepository repository;

    @ConfigProperty(name="github.login")
    private String gitHubLogin;
    @ConfigProperty(name="github.oauth")
    private String gitHubOauth;

    @Inject
    Configuration configuration;

    private synchronized GitHub getGitHub() throws IOException {
        if (gitHub == null) {
            Properties props = new Properties();
            props.put("login", gitHubLogin);
            props.put("oauth", gitHubOauth);
            gitHub = GitHubBuilder.fromProperties(props).build();
        }
        return gitHub;
    }

    private synchronized GHRepository getRepository() throws IOException {
        if (repository == null) {
            repository = getGitHub().getOrganization("keycloak").getRepository("keycloak");
        }
        return repository;
    }


    @GET
    @Produces(MediaType.TEXT_HTML)
    public String listPullRequest() throws IOException, ExecutionException, InterruptedException, TemplateException {
        LOG.infof("ho!");
        Document query = document(
                operation(
                        field("search",
                                List.of(
                                        Argument.arg("type", Search.SearchType.ISSUE),
                                        Argument.arg("query", "is:open is:pr assignee:ahus1 archived:false repo:keycloak/keycloak draft:false sort:updated-desc reactions:>0"),
                                        Argument.arg("first", 10)
                                ),
                                field("edges",
                                        field("node",
                                                field("... on PullRequest",
                                                        field("number"),
                                                        field("title"),
                                                        field("reactions", List.of(
                                                                        Argument.arg("first", 10)
                                                                ),
                                                                field("nodes", field("content"),
                                                                        field("user",
                                                                                field("login")
                                                                        )
                                                                )
                                                        ),
                                                        field("closingIssuesReferences", List.of(
                                                                        Argument.arg("first", 10)
                                                                ),
                                                                field("nodes",
                                                                        field("number"),
                                                                        field("url")
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        Response response = dynamicClient.executeSync(query);
        List<SearchEdges> search = response.getObject(Search.class, "search").getEdges();

        for (SearchEdges searchEdges : search) {
            PullRequest pullRequestQL = searchEdges.getNode();
            if (pullRequestQL.getReactions().getNodes().stream().anyMatch(reaction -> reaction.getContent().equals("EYES") && reaction.getUser().getLogin().equals("ahus1"))) {
                LOG.info("URL: https://github.com/keycloak/keycloak/pull/" + pullRequestQL.getNumber());
                GHPullRequest pullRequest = getRepository().getPullRequest(pullRequestQL.getNumber());
                for (GHPullRequestFileDetail file : pullRequest.listFiles()) {
                    LOG.info("contents-url: " + file.getFilename());
                }
                for (GHLabel label : pullRequest.getLabels()) {
                    LOG.info("labels: " + label.getName());
                }
            }
        }

        StringWriter stringWriter = new StringWriter();
        configuration.getTemplate("monitor.html.ftl").process(Map.of("search", search), stringWriter);
        return stringWriter.toString();
    }
}
