package org.valdi.jmusicbot.delivery.webserver.auth.responses;

public class DiscordGuildResponse {
    private String id;
    private String name;
    private String icon;
    private boolean owner;
    private int permissions;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public boolean isOwner() {
        return owner;
    }

    public int getPermissions() {
        return permissions;
    }
}
