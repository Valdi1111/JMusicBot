package org.valdi.jmusicbot.delivery.webserver.response.errors;

import org.valdi.jmusicbot.delivery.webserver.response.Response;

/**
 * @author Fuzzlemann
 */
public class BadRequestResponse extends Response {

    public BadRequestResponse(String error) {
        super.setHeader("HTTP/1.1 400 Bad Request " + error);
        super.setContent("400 Bad Request: " + error);
    }
}
