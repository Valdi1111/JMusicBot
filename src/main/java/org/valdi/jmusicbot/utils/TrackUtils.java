package org.valdi.jmusicbot.utils;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.valdi.jmusicbot.data.BotDatabase;
import org.valdi.jmusicbot.data.music.Playlist;
import org.valdi.jmusicbot.data.music.Track;
import org.valdi.jmusicbot.data.music.TrackType;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TrackUtils {

    public static Optional<Track> getTrack(BotDatabase db, AudioTrack track) {
        for (Track t : db.getTracks().values()) {
            if (t.getType() == TrackType.YOUTUBE && track.getInfo().uri.equals(t.getFile())) {
                return Optional.of(t);
            }
            if (t.getType() == TrackType.FILE && track.getInfo().uri.endsWith(t.getPath())) {
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }

    public static Optional<Track> getTrack(BotDatabase db, String id) {
        if(id == null || id.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(db.getTracks().getOrDefault(id, null));
    }

    public static Optional<Playlist> getPlaylist(BotDatabase db, String id) {
        if(id == null || id.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(db.getPlaylists().getOrDefault(id, null));
    }

    public static Optional<Playlist> firstMatchPlaylists(BotDatabase db, String name) {
        if(name == null || name.isEmpty()) {
            return Optional.empty();
        }
        return db.getPlaylists().values().stream()
                .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()))
                .findFirst();
    }

    public static List<Playlist> matchPlaylists(BotDatabase db, String name) {
        if(name == null || name.isEmpty()) {
            return Collections.emptyList();
        }
        return db.getPlaylists().values().stream()
                .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public static Optional<Playlist> exactPlaylist(BotDatabase db, String name) {
        if(name == null || name.isEmpty()) {
            return Optional.empty();
        }
        return db.getPlaylists().values().stream()
                .filter(p -> p.getName().toLowerCase().equals(name.toLowerCase()))
                .findFirst();
    }

}
