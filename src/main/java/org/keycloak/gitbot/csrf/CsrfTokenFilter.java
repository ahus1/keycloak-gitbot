package org.keycloak.gitbot.csrf;

import org.jboss.logging.Logger;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 * Prevents Cross-Site-Request-Forgery (CSRF) attacks using the double-submit cookie pattern.
 *
 * On requests the server sets a cookie that is only readable by the client using JavaScript executed on the same domain.
 * The server checks that the same information is presented in an HTTP Header that is provided in an HTTP header of the request.
 * See the
 * <a href="https://cheatsheetseries.owasp.org/cheatsheets/Cross-Site_Request_Forgery_Prevention_Cheat_Sheet.html#double-submit-cookie">OWASP Cross-Site Request Forgery Prevention Cheat Sheet</a>
 * for more information.
 *
 * The solution was inspired by this blog post: <a href="https://marcelkliemannel.com/articles/2021/cross-site-request-forgery-csrf-xsrf-protection-in-quarkus/">Cross-Site Request Forgery (CSRF/XSRF) Protection in Quarkus</a>
 *
 * TODO: Add defense in depth.
 */
@Provider
public class CsrfTokenFilter implements ContainerResponseFilter, ContainerRequestFilter {
    private static final Logger LOG = Logger.getLogger(CsrfTokenFilter.class);

    // Both names match the AngularJS specification
    private static final String CSRF_TOKEN_HEADER_NAME = "X-XSRF-TOKEN";
    private static final String CSRF_TOKEN_COOKIE_NAME = "X-CSRF-TOKEN";

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        // Check if cookie already exists
        if (requestContext.getCookies().containsKey(CSRF_TOKEN_COOKIE_NAME)) {
            return;
        }

        // Issue a new token
        String randomToken = UUID.randomUUID().toString();
        var tokenCookie = new NewCookie(CSRF_TOKEN_COOKIE_NAME, randomToken, "/", null, null, -1, false, false);
        responseContext.getHeaders().add(HttpHeaders.SET_COOKIE, tokenCookie);
    }

    private static final Response INVALID_CSRF_TOKEN_RESPONSE = Response.status(Response.Status.BAD_REQUEST)
            .build();
    private static final HashSet<String> SECURE_HTTP_METHODS = new HashSet<>();
    static {
        SECURE_HTTP_METHODS.add(HttpMethod.GET);
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        // No check for "secure" HTTP methods
        if (SECURE_HTTP_METHODS.contains(requestContext.getMethod())) {
            return;
        }

        Cookie csrfTokenCookie = requestContext.getCookies().get(CSRF_TOKEN_COOKIE_NAME);
        List<String> csrfTokenHeader = requestContext.getHeaders().get(CSRF_TOKEN_HEADER_NAME);

        // Check if the CSRF token header and cookie is present,
        // the header has an unambiguous value and both values
        // must match.
        if (csrfTokenCookie == null || csrfTokenHeader == null
                || csrfTokenHeader.size() != 1
                || !csrfTokenHeader.get(0).equals(csrfTokenCookie.getValue())) {
            LOG.errorf("A valid CSRF token must be provided via the unambiguous header field: %s and cookie: %s", CSRF_TOKEN_HEADER_NAME, CSRF_TOKEN_COOKIE_NAME);
            requestContext.abortWith(INVALID_CSRF_TOKEN_RESPONSE);
        }
    }

}
