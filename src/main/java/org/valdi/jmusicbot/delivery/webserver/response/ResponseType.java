package org.valdi.jmusicbot.delivery.webserver.response;

/**
 * Enum for HTTP content-type response header Strings.
 *
 * @author Rsl1122
 */
public enum ResponseType {
    HTML("text/html; charset=utf-8"),
    CSS("text/css"),
    JSON("application/json"),
    JAVASCRIPT("application/javascript"),
    IMAGE("image/gif"),
    X_ICON("image/x-icon");

    private final String type;

    ResponseType(String type) {
        this.type = type;
    }

    public String get() {
        return type;
    }
}
