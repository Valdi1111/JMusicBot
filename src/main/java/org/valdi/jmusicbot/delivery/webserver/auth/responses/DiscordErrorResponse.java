package org.valdi.jmusicbot.delivery.webserver.auth.responses;

public class DiscordErrorResponse {
    private String error;
    private String message;

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
