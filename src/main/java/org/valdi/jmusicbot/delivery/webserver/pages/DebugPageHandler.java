package org.valdi.jmusicbot.delivery.webserver.pages;

import org.valdi.jmusicbot.delivery.domain.WebUser;
import org.valdi.jmusicbot.delivery.webserver.Request;
import org.valdi.jmusicbot.delivery.webserver.RequestTarget;
import org.valdi.jmusicbot.delivery.webserver.auth.Authentication;
import org.valdi.jmusicbot.delivery.webserver.response.Response;
import org.valdi.jmusicbot.delivery.webserver.response.ResponseFactory;
import org.valdi.jmusicbot.exceptions.WebUserAuthException;

/**
 * PageHandler for /debug page.
 *
 * @author Rsl1122
 */
public class DebugPageHandler implements PageHandler {
    private final ResponseFactory responseFactory;

    public DebugPageHandler(ResponseFactory responseFactory) {
        this.responseFactory = responseFactory;
    }

    @Override
    public Response getResponse(Request request, RequestTarget target) {
        return responseFactory.debugPageResponse();
    }

    @Override
    public boolean isAuthorized(Authentication auth, RequestTarget target) throws WebUserAuthException {
        WebUser webUser = auth.getWebUser();
        return webUser.getPermLevel() <= 0;
    }
}
