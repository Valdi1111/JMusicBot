package org.valdi.jmusicbot.exceptions.connection;

import org.valdi.jmusicbot.delivery.webserver.response.ResponseCode;

/**
 * Thrown when Connection returns 500.
 *
 * @author Rsl1122
 */
public class InternalErrorException extends WebException {
    public InternalErrorException() {
        super("Internal Error occurred on receiving server", ResponseCode.INTERNAL_ERROR);
    }

    public InternalErrorException(String message, Throwable cause) {
        super(message, cause, ResponseCode.INTERNAL_ERROR);
    }
}
