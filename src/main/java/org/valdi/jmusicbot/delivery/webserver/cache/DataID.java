package org.valdi.jmusicbot.delivery.webserver.cache;

import java.util.UUID;

/**
 * Enum for different JSON data entries that can be stored in {@link JSONCache}.
 *
 * @author Rsl1122
 */
public enum DataID {
    PLAYERS,
    SESSIONS,
    SERVERS,
    KILLS,
    PING_TABLE,
    GRAPH_PERFORMANCE,
    GRAPH_ONLINE,
    GRAPH_UNIQUE_NEW,
    GRAPH_CALENDAR,
    GRAPH_WORLD_PIE,
    GRAPH_WORLD_MAP,
    GRAPH_ACTIVITY,
    GRAPH_PING,
    GRAPH_SERVER_PIE,
    GRAPH_PUNCHCARD,
    SERVER_OVERVIEW,
    ONLINE_OVERVIEW,
    SESSIONS_OVERVIEW,
    PVP_PVE,
    PLAYERBASE_OVERVIEW,
    PERFORMANCE_OVERVIEW,
    EXTENSION_NAV,
    EXTENSION_TABS
    ;

    public String of(UUID serverUUID) {
        return name() + '-' + serverUUID;
    }

}
