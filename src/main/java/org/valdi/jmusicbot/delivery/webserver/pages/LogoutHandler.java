package org.valdi.jmusicbot.delivery.webserver.pages;

import org.valdi.jmusicbot.delivery.webserver.Request;
import org.valdi.jmusicbot.delivery.webserver.RequestTarget;
import org.valdi.jmusicbot.delivery.webserver.auth.Authentication;
import org.valdi.jmusicbot.delivery.webserver.response.Response;
import org.valdi.jmusicbot.delivery.webserver.response.ResponseFactory;
import org.valdi.jmusicbot.exceptions.WebUserAuthException;

public class LogoutHandler implements PageHandler {
    private final ResponseFactory responseFactory;

    public LogoutHandler(ResponseFactory responseFactory) {
        this.responseFactory = responseFactory;
    }

    @Override
    public Response getResponse(Request request, RequestTarget target) {
        return responseFactory.logoutRedirectResponse();
    }

    @Override
    public boolean isAuthorized(Authentication auth, RequestTarget target) throws WebUserAuthException {
        return true;
    }
}
