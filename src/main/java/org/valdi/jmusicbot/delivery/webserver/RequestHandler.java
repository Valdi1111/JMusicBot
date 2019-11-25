package org.valdi.jmusicbot.delivery.webserver;

import bell.oauth.discord.main.OAuthBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.valdi.jmusicbot.JMusicBot;
import org.valdi.jmusicbot.delivery.webserver.auth.Authentication;
import org.valdi.jmusicbot.delivery.webserver.auth.DiscordAuthentication;
import org.valdi.jmusicbot.delivery.webserver.response.Response;
import org.valdi.jmusicbot.delivery.webserver.response.ResponseFactory;

import java.util.*;

/**
 * HttpHandler for WebServer request management.
 *
 * @author Rsl1122
 */
public class RequestHandler implements HttpHandler {
    private final Logger logger = LogManager.getLogger("WebServer RequestHandler");

    private final JMusicBot main;
    private final ResponseHandler responseHandler;
    private final ResponseFactory responseFactory;

    public static final Map<String, OAuthBuilder> auths = new HashMap<>();

    RequestHandler(JMusicBot main, ResponseHandler responseHandler, ResponseFactory responseFactory) {
        this.main = main;
        this.responseHandler = responseHandler;
        this.responseFactory = responseFactory;
    }

    @Override
    public void handle(HttpExchange exchange) {
        Headers requestHeaders = exchange.getRequestHeaders();
        Headers responseHeaders = exchange.getResponseHeaders();

        Request request = new Request(exchange, main.getLocale());
        request.setAuth(getAuthorization(requestHeaders));

        try {
            Response response = responseHandler.getResponse(request);
            response.setResponseHeaders(responseHeaders);
            response.send(exchange, main.getLocale(), main.getTheme());
        } catch (Exception e) {
            logger.error("Error in WebServer ResponseHandler", e);
        } finally {
            exchange.close();
        }
    }

    private Authentication getAuthorization(Headers requestHeaders) {
        List<String> cookies = requestHeaders.get("Cookie");
        if (cookies == null || cookies.isEmpty()) {
            return null;
        }

        String[] args = StringUtils.split(cookies.get(0), ";");
        for(String arg : args) {
            if(arg.charAt(0) == ' ') {
                arg = arg.substring(1);
            }

            if (StringUtils.contains(arg, "token=")) {
                String token = arg.substring(6);
                if(token.isEmpty() || token.equals("''")) {
                    return null;
                }

                return new DiscordAuthentication(main, token);
            }
        }
        return null;
    }

    public ResponseHandler getResponseHandler() {
        return responseHandler;
    }

    public ResponseFactory getResponseFactory() {
        return responseFactory;
    }
}
