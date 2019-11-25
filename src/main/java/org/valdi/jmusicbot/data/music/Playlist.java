package org.valdi.jmusicbot.data.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Guild;
import org.valdi.jmusicbot.data.BotDatabase;
import org.valdi.jmusicbot.utils.ChecksumUtils;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.Consumer;

public class Playlist {
    protected final BotDatabase database;

    protected final String id;
    protected String name;
    protected List<PlaylistTrack> tracks;
    protected Timestamp created;
    protected long createdby;

    protected final List<PlaylistLoadError> errors;
    protected boolean loaded;

    public Playlist(BotDatabase database, String id) {
        this.database = database;
        this.id = id;
        this.tracks = new ArrayList<>();

        this.errors = new LinkedList<>();
        this.loaded = false;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Playlist setName(String name) {
        this.name = name;
        return this;
    }

    public List<PlaylistTrack> getTracks() {
        return tracks;
    }

    public List<PlaylistTrack> getOrderedTracks() {
        List<PlaylistTrack> orderedTracks = new ArrayList<>();
        Optional<PlaylistTrack> currentTrack;
        int counter = 1;
        while((currentTrack = getTrack(counter)).isPresent()) {
            orderedTracks.add(currentTrack.get());
            counter++;
        }
        return orderedTracks;
    }

    public Optional<PlaylistTrack> getTrack(int pos) {
        return tracks.stream().filter(t -> t.getPos() == pos).findFirst();
    }

    public Playlist addTrack(String trackId, int pos) {
        PlaylistTrack track = new PlaylistTrack(this, trackId, pos);
        tracks.add(track);
        return this;
    }

    public Playlist addTrack(String trackId) {
        int pos = 0;
        for(PlaylistTrack track : tracks) {
            pos = Math.max(pos, track.getPos());
        }
        this.addTrack(trackId, ++pos);
        return this;
    }

    public Playlist removeTrack(int pos) {
        tracks.removeIf(t -> t.getPos() == pos);
        return this;
    }

    public Timestamp getCreated() {
        return created;
    }

    public Playlist setCreated(Timestamp created) {
        this.created = created;
        return this;
    }

    public long getCreatedby() {
        return createdby;
    }

    public Playlist setCreatedby(long createdby) {
        this.createdby = createdby;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Playlist)) {
            return false;
        }
        Playlist playlist = (Playlist) o;
        return Objects.equals(id, playlist.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", tracks=" + tracks +
                ", created=" + created +
                ", createdby=" + createdby +
                '}';
    }

    int currentPos = 1;
    public void loadNextTrack(Guild guild, AudioPlayerManager manager, Consumer<AudioTrack> consumer, Runnable callback) {
        Optional<PlaylistTrack> pTrack = this.getTrack(currentPos);
        if(!pTrack.isPresent()) {
            currentPos = 1;
            this.loadNextTrack(guild, manager, consumer, callback);
            return;
        }

        currentPos++;

        Track track = pTrack.get().getTrack();
        String toLoad = null;
        switch (track.getType()) {
            case FILE:
                try {
                    File file = new File(database.getManager().getStoreFolder(guild), track.getPath());
                    if (!ChecksumUtils.checkFileHash(file, "SHA-256")) {
                        return;
                    }
                    toLoad = file.getPath();
                } catch (IOException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    return;
                }
                break;
            case YOUTUBE:
                toLoad = track.getFile();
                break;
            case SOUNDCLOUD:
                return;
        }
        manager.loadItem(toLoad, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack at) {
                at.setUserData(0L);
                consumer.accept(at);
            }

            @Override
            public void playlistLoaded(AudioPlaylist ap) {
                if (ap.isSearchResult()) {
                    ap.getTracks().get(0).setUserData(0L);
                    consumer.accept(ap.getTracks().get(0));
                } else if (ap.getSelectedTrack() != null) {
                    ap.getSelectedTrack().setUserData(0L);
                    consumer.accept(ap.getSelectedTrack());
                } else {
                    List<AudioTrack> loaded = new ArrayList<>(ap.getTracks());
                    loaded.forEach(at -> at.setUserData(0L));
                    loaded.forEach(consumer);
                }
            }

            @Override
            public void noMatches() {
                //
            }

            @Override
            public void loadFailed(FriendlyException fe) {
                //
            }
        });
    }

    public void loadTracks(Guild guild, AudioPlayerManager manager, Consumer<AudioTrack> consumer, Runnable callback) {
        Iterator<PlaylistTrack> i = this.getOrderedTracks().iterator();
        while(i.hasNext()) {
            PlaylistTrack pTrack = i.next();
            Track track = pTrack.getTrack();
            int index = pTrack.getPos();

            String toLoad = null;
            switch (track.getType()) {
                case FILE:
                    try {
                        File file = new File(database.getManager().getStoreFolder(guild), track.getPath());
                        if (!ChecksumUtils.checkFileHash(file, "SHA-256")) {
                            errors.add(new PlaylistLoadError(index, track.getTitle(), "File corrupted in the database! Check console for errors."));
                            continue;
                        }
                        toLoad = file.getPath();
                    } catch (IOException | NoSuchAlgorithmException e) {
                        errors.add(new PlaylistLoadError(index, track.getTitle(), "Error loading track! Check console for errors."));
                        e.printStackTrace();
                        continue;
                    }
                    break;
                case YOUTUBE:
                    toLoad = track.getFile();
                    break;
                case SOUNDCLOUD:
                    continue;
            }
            manager.loadItemOrdered(name, toLoad, new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack at) {
                    at.setUserData(0L);
                    consumer.accept(at);
                    if (!i.hasNext() && callback != null)
                        callback.run();
                }

                @Override
                public void playlistLoaded(AudioPlaylist ap) {
                    if (ap.isSearchResult()) {
                        ap.getTracks().get(0).setUserData(0L);
                        consumer.accept(ap.getTracks().get(0));
                    } else if (ap.getSelectedTrack() != null) {
                        ap.getSelectedTrack().setUserData(0L);
                        consumer.accept(ap.getSelectedTrack());
                    } else {
                        List<AudioTrack> loaded = new ArrayList<>(ap.getTracks());
                        loaded.forEach(at -> at.setUserData(0L));
                        loaded.forEach(consumer);
                    }
                    if (!i.hasNext() && callback != null)
                        callback.run();
                }

                @Override
                public void noMatches() {
                    errors.add(new PlaylistLoadError(index, track.getTitle(), "No matches found."));
                    if (!i.hasNext() && callback != null)
                        callback.run();
                }

                @Override
                public void loadFailed(FriendlyException fe) {
                    errors.add(new PlaylistLoadError(index, track.getTitle(), "Failed to load track: " + fe.getLocalizedMessage()));
                    if (!i.hasNext() && callback != null)
                        callback.run();
                }
            });
        }
    }

    public List<PlaylistLoadError> getErrors() {
        return errors;
    }
}
