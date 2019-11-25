package org.valdi.jmusicbot.delivery.webserver.cache;

import java.util.UUID;

/**
 * Enum class for "magic" ResponseCache identifier values.
 *
 * @author Rsl1122
 */
public enum PageId {

    SERVER("serverPage:"),
    PLAYER("playerPage:"),
    RAW_PLAYER("rawPlayer:"),
    PLAYERS("playersPage"),

    ERROR("error:"),
    FORBIDDEN(ERROR.of("Forbidden")),
    NOT_FOUND(ERROR.of("Not Found")),

    JS("js:"),
    CSS("css:"),

    FAVICON("Favicon");

    private final String id;

    PageId(String id) {
        this.id = id;
    }

    public String of(String additionalInfo) {
        return id + additionalInfo;
    }

    public String of(UUID uuid) {
        return of(uuid.toString());
    }

    public String id() {
        return id;
    }
}
