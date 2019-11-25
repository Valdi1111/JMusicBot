package org.valdi.jmusicbot.data.music;

import java.util.Objects;

public class PlaylistTrack {
    protected Playlist playlist;
    protected String trackId;
    protected int pos;

    public PlaylistTrack(Playlist playlist, String trackId, int pos) {
        this.playlist = playlist;
        this.trackId = trackId;
        this.pos = pos;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public String getTrackId() {
        return trackId;
    }

    public Track getTrack() {
        return playlist.database.getTracks().get(trackId);
    }

    public int getPos() {
        return pos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaylistTrack that = (PlaylistTrack) o;
        return Objects.equals(trackId, that.trackId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trackId);
    }

    @Override
    public String toString() {
        return "PlaylistTrack{" +
                "trackId='" + trackId + '\'' +
                ", pos=" + pos +
                '}';
    }
}
