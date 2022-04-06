package org.keycloak.gitbot.freemarker;

import freemarker.core.*;
import freemarker.template.TemplateDateModel;
import freemarker.template.TemplateModelException;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class PrettyDateFormatFactory extends TemplateDateFormatFactory {

    public static final PrettyDateFormatFactory INSTANCE
            = new PrettyDateFormatFactory();

    private PrettyDateFormatFactory() {
        // Defined to decrease visibility
    }

    @Override
    public TemplateDateFormat get(String params, int dateType,
                                  Locale locale, TimeZone timeZone, boolean zonelessInput,
                                  Environment env)
            throws InvalidFormatParametersException {
        TemplateFormatUtil.checkHasNoParameters(params);
        return PrettyDateFormat.INSTANCE;
    }

    private static class PrettyDateFormat extends TemplateDateFormat {

        PrettyTime p = new PrettyTime();

        private static final PrettyDateFormat INSTANCE
                = new PrettyDateFormat();

        private PrettyDateFormat() { }

        @Override
        public String formatToPlainText(TemplateDateModel dateModel)
                throws TemplateModelException {
            return p.format(TemplateFormatUtil.getNonNullDate(dateModel));
        }

        @Override
        public boolean isLocaleBound() {
            return false;
        }

        @Override
        public boolean isTimeZoneBound() {
            return false;
        }

        @Override
        public Date parse(String s, int dateType) throws UnparsableValueException {
            try {
                return new Date(Long.parseLong(s));
            } catch (NumberFormatException e) {
                throw new UnparsableValueException("Malformed long");
            }
        }

        @Override
        public String getDescription() {
            return "millis since the epoch";
        }

    }
}
