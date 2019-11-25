package org.valdi.jmusicbot.webserver;

import java.util.Arrays;
import java.util.List;

public enum ResponseType {
    HTML("text/html; charset=utf-8", ".html", ".htm"),
    CSS("text/css", ".css"),
    JSON("application/json", ".json"),
    JAVASCRIPT("application/javascript", ".js"),
    IMAGE_PNG("image/png", ".png"),
    IMAGE_JPEG("image/jpeg", ".jpeg"),
    IMAGE_GIF("image/gif", ".gif"),
    X_ICON("image/x-icon", ".ico"),
    DEFAULT("text/plain");

    private final String type;
    private final List<String> extensions;

    ResponseType(String type, String... extensions) {
        this.type = type;
        this.extensions = Arrays.asList(extensions);
    }

    public String getType() {
        return type;
    }

    public List<String> getExtensions() {
        return extensions;
    }
}
