package org.valdi.jmusicbot.exceptions.connection;

import org.valdi.jmusicbot.delivery.webserver.response.ResponseCode;

/**
 * Thrown when Connection returns 404, when page is not found.
 *
 * @author Rsl1122
 */
public class NotFoundException extends WebException {
    public NotFoundException(String message) {
        super(message, ResponseCode.NOT_FOUND);
    }
}
