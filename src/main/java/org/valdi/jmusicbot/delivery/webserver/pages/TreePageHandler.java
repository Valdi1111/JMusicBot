package org.valdi.jmusicbot.delivery.webserver.pages;

import org.valdi.jmusicbot.delivery.webserver.Request;
import org.valdi.jmusicbot.delivery.webserver.RequestTarget;
import org.valdi.jmusicbot.delivery.webserver.auth.Authentication;
import org.valdi.jmusicbot.delivery.webserver.response.Response;
import org.valdi.jmusicbot.delivery.webserver.response.ResponseFactory;
import org.valdi.jmusicbot.exceptions.WebUserAuthException;
import org.valdi.jmusicbot.exceptions.connection.WebException;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract PageHandler that allows Tree-like target deduction.
 *
 * @author Rsl1122
 */
public abstract class TreePageHandler implements PageHandler {
    protected final ResponseFactory responseFactory;

    private Map<String, PageHandler> pages;

    public TreePageHandler(ResponseFactory responseFactory) {
        this.responseFactory = responseFactory;
        pages = new HashMap<>();
    }

    public void registerPage(String targetPage, PageHandler handler) {
        pages.put(targetPage, handler);
    }

    public void registerPage(String targetPage, PageHandler handler, int requiredPerm) {
        pages.put(targetPage, new PageHandler() {
            @Override
            public Response getResponse(Request request, RequestTarget target) throws WebException {
                return handler.getResponse(request, target);
            }

            @Override
            public boolean isAuthorized(Authentication auth, RequestTarget target) throws WebUserAuthException {
                return auth.getWebUser().getPermLevel() <= requiredPerm;
            }
        });
    }
    public void registerPage(String targetPage, Response response, int requiredPerm) {
        pages.put(targetPage, new PageHandler() {
            @Override
            public Response getResponse(Request request, RequestTarget target) {
                return response;
            }

            @Override
            public boolean isAuthorized(Authentication auth, RequestTarget target) throws WebUserAuthException {
                return auth.getWebUser().getPermLevel() <= requiredPerm;
            }
        });
    }

    @Override
    public Response getResponse(Request request, RequestTarget target) throws WebException {
        PageHandler pageHandler = getPageHandler(target);
        return pageHandler != null
                ? pageHandler.getResponse(request, target)
                : responseFactory.pageNotFound404();
    }

    public PageHandler getPageHandler(RequestTarget target) {
        if (target.isEmpty()) {
            return pages.get("");
        }
        String targetPage = target.get(0);
        target.removeFirst();
        return pages.get(targetPage);
    }

    public PageHandler getPageHandler(String targetPage) {
        return pages.get(targetPage);
    }
}
