package org.valdi.jmusicbot.delivery.webserver;

import com.sun.net.httpserver.HttpExchange;
import org.valdi.jmusicbot.delivery.webserver.auth.Authentication;
import org.valdi.jmusicbot.settings.locale.Locale;

import java.io.InputStream;
import java.net.URI;
import java.util.Optional;

/**
 * Represents a HttpExchange Request.
 * <p>
 * Automatically gets the Basic Auth from headers.
 *
 * @author Rsl1122
 */
public class Request {
    private final String requestMethod;

    private URI requestURI;

    private final HttpExchange exchange;
    private final String remoteAddress;
    private final Locale locale;
    private Authentication auth;

    public Request(HttpExchange exchange, Locale locale) {
        this.requestMethod = exchange.getRequestMethod();
        requestURI = exchange.getRequestURI();

        remoteAddress = exchange.getRemoteAddress().getAddress().getHostAddress();

        this.exchange = exchange;

        this.locale = locale;
    }

    public Optional<Authentication> getAuth() {
        return Optional.ofNullable(auth);
    }

    public void setAuth(Authentication authentication) {
        auth = authentication;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getTargetString() {
        return requestURI.getPath() + '?' + requestURI.getQuery();
    }

    public RequestTarget getTarget() {
        return new RequestTarget(requestURI);
    }

    public InputStream getRequestBody() {
        return exchange.getRequestBody();
    }

    @Override
    public String toString() {
        return "Request:" + requestMethod + " " + requestURI.getPath();
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public Locale getLocale() {
        return locale;
    }
}
