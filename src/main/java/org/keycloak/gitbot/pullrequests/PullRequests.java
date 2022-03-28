package org.keycloak.gitbot.pullrequests;

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
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static io.smallrye.graphql.client.core.Document.document;
import static io.smallrye.graphql.client.core.Field.field;
import static io.smallrye.graphql.client.core.Operation.operation;


@Path("/prs")
public class PullRequests {

    private static final Logger LOG = Logger.getLogger(PullRequests.class);

    @Inject
    Configuration configuration;

    @POST
    @Path("{pr}/click")
    @Produces(MediaType.TEXT_HTML)
    public String click(@PathParam("pr") Integer pr) throws IOException, ExecutionException, InterruptedException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        configuration.getTemplate("completed.html.ftl").process(Map.of(), stringWriter);
        return stringWriter.toString();
    }
}
