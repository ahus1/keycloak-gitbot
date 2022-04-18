package org.keycloak.gitbot.pullrequests;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.jboss.logging.Logger;
import org.kohsuke.github.GHRepository;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

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
