package org.valdi.jmusicbot.delivery.webserver.pages;

import org.valdi.jmusicbot.delivery.webserver.Request;
import org.valdi.jmusicbot.delivery.webserver.RequestTarget;
import org.valdi.jmusicbot.delivery.webserver.WebServer;
import org.valdi.jmusicbot.delivery.webserver.auth.Authentication;
import org.valdi.jmusicbot.delivery.webserver.response.Response;
import org.valdi.jmusicbot.delivery.webserver.response.ResponseFactory;
import org.valdi.jmusicbot.exceptions.connection.WebException;

import java.util.Optional;

/**
 * PageHandler for / page (Address root).
 * <p>
 * Not Available if Authentication is not enabled.
 *
 * @author Rsl1122
 */
public class RootPageHandler implements PageHandler {
    private final ResponseFactory responseFactory;
    private final WebServer webServer;

    public RootPageHandler(ResponseFactory responseFactory, WebServer webServer) {
        this.responseFactory = responseFactory;
        this.webServer = webServer;
    }

    @Override
    public Response getResponse(Request request, RequestTarget target) throws WebException {
        if (!webServer.isAuthRequired()) {
            return responseFactory.redirectResponse("/play/files");
        }

        Optional<Authentication> auth = request.getAuth();
        if (!auth.isPresent()) {
            return responseFactory.discordAuth();
        }
        return responseFactory.redirectResponse("/play/files");
    }

    @Override
    public boolean isAuthorized(Authentication auth, RequestTarget target) {
        return true;
    }
}
