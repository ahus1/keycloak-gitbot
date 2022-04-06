package org.keycloak.gitbot.application;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.io.IOException;
import java.util.Properties;

@ApplicationScoped
public class GitHubRepositoryFactory {
    @ConfigProperty(name = "github.login")
    String gitHubLogin;
    @ConfigProperty(name = "github.oauth")
    String gitHubOauth;

    private synchronized GitHub getGitHub() throws IOException {
        Properties props = new Properties();
        props.put("login", gitHubLogin);
        props.put("oauth", gitHubOauth);
        return GitHubBuilder.fromProperties(props).build();
    }

    @Produces
    public synchronized GHRepository getRepository() throws IOException {
        return getGitHub().getOrganization("keycloak").getRepository("keycloak");
    }

}
