package org.keycloak.gitbot.pullrequests;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.jboss.logging.Logger;
import org.kohsuke.github.GHRepository;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import static io.smallrye.graphql.client.core.Field.field;
import static io.smallrye.graphql.client.core.Operation.operation;


@Path("/prs")
public class PullRequests {

    private static final Logger LOG = Logger.getLogger(PullRequests.class);

    @Inject
    Configuration configuration;

    @Inject
    GHRepository repository;

    @POST
    @Path("{pr}/addLabel")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public String addLabel(@PathParam("pr") Integer pr, @NotNull @QueryParam("label") String label) throws IOException, TemplateException {
        repository.getPullRequest(pr).addLabels(label);
        StringWriter stringWriter = new StringWriter();
        configuration.getTemplate("completed.html.ftl").process(Map.of(), stringWriter);
        return stringWriter.toString();
    }

    @POST
    @Path("{pr}/addComment")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public String addComment(@PathParam("pr") Integer pr, @NotNull @FormParam("comment") String comment) throws IOException, TemplateException {
        repository.getPullRequest(pr).comment(comment);
        StringWriter stringWriter = new StringWriter();
        configuration.getTemplate("completed.html.ftl").process(Map.of(), stringWriter);
        return stringWriter.toString();
    }
}
