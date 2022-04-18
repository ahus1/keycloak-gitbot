package org.keycloak.gitbot.freemarker;

import freemarker.template.Configuration;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class FreemarkerConfiguration {

    @Inject
    Configuration configuration;

    void startup(@Observes StartupEvent event) {
        configuration.setDateTimeFormat("@epoch");
    }
}