package org.valdi.jmusicbot.data.music;

public class PlaylistLoadError {
    private final int number;
    private final String item;
    private final String reason;

    public PlaylistLoadError(int number, String item, String reason) {
        this.number = number;
        this.item = item;
        this.reason = reason;
    }

    public int getIndex() {
        return number;
    }

    public String getItem() {
        return item;
    }

    public String getReason() {
        return reason;
    }
}
