package org.valdi.jmusicbot.exceptions.connection;

import org.valdi.jmusicbot.delivery.webserver.response.ResponseCode;

/**
 * Thrown when connection is returned 401 Bad Request.
 *
 * @author Rsl1122
 */
public class BadRequestException extends WebException {

    public BadRequestException(String message) {
        super(message, ResponseCode.BAD_REQUEST);
    }
}
