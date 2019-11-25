package org.valdi.jmusicbot.data.music;

import org.valdi.jmusicbot.data.BotDatabase;

import java.sql.Timestamp;
import java.util.Objects;

public class Track {
    protected final BotDatabase database;

    protected final String id;
    protected TrackType type;
    protected String file;
    protected String path;
    protected long duration;

    protected String thumbnail;
    protected String title;
    protected String artist;
    protected String album;
    protected int track;
    protected String genre;

    protected int volume;
    protected Timestamp created;
    protected long createdby;
    protected int playcount;

    public Track(BotDatabase database, String id) {
        this.database = database;

        this.id = id;
        this.type = TrackType.FILE;
        this.file = "";
        this.path = "";
        this.duration = 0L;

        this.thumbnail = "";
        this.title = "";
        this.artist = "";
        this.album = "";
        this.track = -1;
        this.genre = "";

        this.volume = -1;
        this.created = new Timestamp(new java.util.Date().getTime());
        this.createdby = 0L;
        this.playcount = 0;
    }

    public String getId() {
        return id;
    }

    public TrackType getType() {
        return type;
    }

    public Track setType(TrackType type) {
        this.type = type;
        return this;
    }

    public String getFile() {
        return file;
    }

    public Track setFile(String file) {
        this.file = file;
        return this;
    }

    public String getPath() {
        return path;
    }

    public Track setPath(String path) {
        this.path = path;
        return this;
    }

    public long getDuration() {
        return duration;
    }

    public Track setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public Track setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Track setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getArtist() {
        return artist;
    }

    public Track setArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public String getAlbum() {
        return album;
    }

    public Track setAlbum(String album) {
        this.album = album;
        return this;
    }

    public int getTrack() {
        return track;
    }

    public Track setTrack(int track) {
        this.track = track;
        return this;
    }

    public String getGenre() {
        return genre;
    }

    public Track setGenre(String genre) {
        this.genre = genre;
        return this;
    }

    public int getVolume() {
        return volume;
    }

    public Track setVolume(int volume) {
        this.volume = volume;
        return this;
    }

    public Timestamp getCreated() {
        return created;
    }

    public Track setCreated(Timestamp created) {
        this.created = created;
        return this;
    }

    public long getCreatedby() {
        return createdby;
    }

    public Track setCreatedby(long createdby) {
        this.createdby = createdby;
        return this;
    }

    public int getPlaycount() {
        return playcount;
    }

    public Track setPlaycount(int playcount) {
        this.playcount = playcount;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Track)) {
            return false;
        }
        Track track = (Track) o;
        return Objects.equals(id, track.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Track{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", path='" + path + '\'' +
                ", duration=" + duration +
                ", thumbnail='" + thumbnail + '\'' +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", track=" + track +
                ", genre='" + genre + '\'' +
                ", volume=" + volume +
                ", created=" + created +
                ", createdby=" + createdby +
                ", playcount=" + playcount +
                '}';
    }
}
