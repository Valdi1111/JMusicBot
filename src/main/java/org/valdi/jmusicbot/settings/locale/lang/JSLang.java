package org.valdi.jmusicbot.settings.locale.lang;

/**
 * Lang enum for all text included in the javascript files.
 *
 * @author Rsl1122
 */
public enum JSLang implements Lang {
    TEXT_PREDICTED_RETENTION("This value is a prediction based on previous players"),
    TEXT_NO_SERVERS("No servers found in the database"),
    TEXT_NO_SERVER("No server to display online activity for"),
    LABEL_REGISTERED_PLAYERS("Registered Players"),
    LINK_SERVER_ANALYSIS("Server Analysis"),
    LINK_QUICK_VIEW("Quick view"),
    TEXT_FIRST_SESSION("First session"),
    LABEL_SESSION_ENDED("Ended"),
    LINK_PLAYER_PAGE("Player Page"),
    LABEL_NO_SESSION_KILLS("None"),
    UNIT_ENTITIES("Entities"),
    UNIT_CHUNKS("Chunks"),
    LABEL_RELATIVE_JOIN_ACTIVITY("Relative Join Activity"),
    LABEL_DAY_OF_WEEK("Day of the Week"),
    LABEL_WEEK_DAYS("'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'");

    private final String defaultValue;

    JSLang(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String getIdentifier() {
        return "HTML - " + name();
    }

    @Override
    public String getDefault() {
        return defaultValue;
    }
}