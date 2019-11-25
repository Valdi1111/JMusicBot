package org.valdi.jmusicbot.delivery.webserver.pages;

import org.valdi.jmusicbot.delivery.webserver.Request;
import org.valdi.jmusicbot.delivery.webserver.RequestTarget;
import org.valdi.jmusicbot.delivery.webserver.auth.Authentication;
import org.valdi.jmusicbot.delivery.webserver.response.Response;
import org.valdi.jmusicbot.exceptions.WebUserAuthException;
import org.valdi.jmusicbot.exceptions.connection.WebException;

/**
 * PageHandlers are used for easier Response management and authorization checking.
 *
 * @author Rsl1122
 */
public interface PageHandler {

    /**
     * Get the Response of a PageHandler.
     *
     * @param request Request in case it is useful for choosing page.
     * @param target  Rest of the target coordinates after this page has been solved.
     * @return Response appropriate to the PageHandler.
     */
    Response getResponse(Request request, RequestTarget target) throws WebException;

    default boolean isAuthorized(Authentication auth, RequestTarget target) throws WebUserAuthException {
        return true;
    }

}
