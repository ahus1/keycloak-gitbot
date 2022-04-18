package org.keycloak.gitbot.pullrequests;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.ExecutionException;


@Path("/")
public class Index {

    private static final Logger LOG = Logger.getLogger(Index.class);

    @Inject
    Configuration configuration;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String index() throws IOException, ExecutionException, InterruptedException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        configuration.getTemplate("index.html.ftl").process(Map.of(), stringWriter);
        return stringWriter.toString();
    }
}
