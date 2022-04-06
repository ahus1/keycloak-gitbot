package org.keycloak.gitbot.freemarker;

import freemarker.core.TemplateDateFormatFactory;
import freemarker.template.Configuration;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.ObjectInputFilter;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class FreemarkerConfiguration {

    @Inject
    Configuration configuration;

    void startup(@Observes StartupEvent event) {
        Map<String, TemplateDateFormatFactory> customDateFormats = new HashMap<>();
        customDateFormats.put("pretty", PrettyDateFormatFactory.INSTANCE);
        configuration.setCustomDateFormats(customDateFormats);
        configuration.setDateTimeFormat("@epoch");
    }
}