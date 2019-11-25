package org.valdi.jmusicbot.exceptions.connection;

import org.valdi.jmusicbot.delivery.webserver.response.ResponseCode;

/**
 * Thrown when Connection POST-request fails, general Exception.
 *
 * @author Rsl1122
 */
public class WebException extends Exception {
    private final ResponseCode responseCode;

    public WebException() {
        responseCode = ResponseCode.NONE;
    }

    public WebException(String message) {
        super(message);
        responseCode = ResponseCode.NONE;
    }

    public WebException(String message, Throwable cause) {
        super(message, cause);
        responseCode = ResponseCode.NONE;
    }

    public WebException(Throwable cause) {
        super(cause);
        responseCode = ResponseCode.NONE;
    }

    public WebException(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public WebException(String message, ResponseCode responseCode) {
        super(message);
        this.responseCode = responseCode;
    }

    public WebException(String message, Throwable cause, ResponseCode responseCode) {
        super(message, cause);
        this.responseCode = responseCode;
    }

    public WebException(Throwable cause, ResponseCode responseCode) {
        super(cause);
        this.responseCode = responseCode;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }
}
