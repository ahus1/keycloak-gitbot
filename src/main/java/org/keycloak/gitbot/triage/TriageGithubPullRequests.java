package org.keycloak.gitbot.triage;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static io.smallrye.graphql.client.core.Document.document;
import static io.smallrye.graphql.client.core.Field.field;
import static io.smallrye.graphql.client.core.Operation.operation;


@Path("/triage")
public class TriageGithubPullRequests {

    private static final Logger LOG = Logger.getLogger(TriageGithubPullRequests.class);

    @Inject
    @GraphQLClient("github")
    DynamicGraphQLClient dynamicClient;

    private GitHub gitHub;
    private GHRepository repository;

    @ConfigProperty(name = "github.login")
    private String gitHubLogin;
    @ConfigProperty(name = "github.oauth")
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
                                        Argument.arg("query", "is:open is:pr archived:false repo:keycloak/keycloak draft:false sort:updated-desc no:assignee"),
                                        Argument.arg("first", 100)
                                ),
                                field("edges",
                                        field("node",
                                                field("... on PullRequest",
                                                        field("number"),
                                                        field("title"),
                                                        field("files", List.of(
                                                                        Argument.arg("first", 100)
                                                                ),
                                                                field("nodes",
                                                                        field("path")
                                                                )
                                                        ),
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

        List<SearchEdges> filteredSearch = new ArrayList<>();

        for (SearchEdges searchEdges : search) {
            PullRequest pullRequestQL = searchEdges.getNode();
            if (pullRequestQL.getFiles().getNodes().stream().allMatch(file -> file.getPath().endsWith(".adoc"))) {
                // these are documentation only changes
                filteredSearch.add(searchEdges);
            } else if (pullRequestQL.getFiles().getNodes().stream().allMatch(file ->
                    file.getPath().startsWith("themes/src/main/resources-community/theme/") ||
                            file.getPath().matches("themes/src/main/resources/theme/.*/messages_en.properties"))) {
                // these are translation only changes
                filteredSearch.add(searchEdges);
            } else if (pullRequestQL.getFiles().getNodes().size() == 1) {
                // these are small PRs
                filteredSearch.add(searchEdges);
            }
        }

        StringWriter stringWriter = new StringWriter();
        configuration.getTemplate("monitor.html.ftl").process(Map.of("search", filteredSearch), stringWriter);
        return stringWriter.toString();
    }
}
