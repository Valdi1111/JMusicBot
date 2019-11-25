package org.valdi.jmusicbot.exceptions.connection;

import org.valdi.jmusicbot.delivery.webserver.response.ResponseCode;

/**
 * Thrown when Connection gets a 403 response.
 *
 * @author Rsl1122
 */
public class ForbiddenException extends WebException {
    public ForbiddenException(String url) {
        super("Forbidden: " + url, ResponseCode.FORBIDDEN);
    }
}
