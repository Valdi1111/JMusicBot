package org.valdi.jmusicbot.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.valdi.SuperApiX.common.databases.DatabaseException;
import org.valdi.SuperApiX.common.databases.types.H2Database;
import org.valdi.jmusicbot.data.music.Playlist;
import org.valdi.jmusicbot.data.music.PlaylistTrack;
import org.valdi.jmusicbot.data.music.Track;
import org.valdi.jmusicbot.data.music.TrackType;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class BotDatabase extends H2Database {
    private final Logger logger = LogManager.getLogger("Bot Database");

    private final DataManager manager;
    private final long guildId;

    private final Map<String, Playlist> playlists;
    private final Map<String, Track> tracks;

    public BotDatabase(DataManager manager, long guildId, File file) throws DatabaseException {
        super(manager.getMain(), file, ";MODE=MYSQL", "username",
                "password", 5, "JMusicBot-Bot-" + guildId);
        this.manager = manager;
        this.guildId = guildId;

        this.playlists = new HashMap<>();
        this.tracks = new HashMap<>();
    }

    public void createTables() throws DatabaseException {
        this.createStorageFilesTable();
        this.createPlaylistsTable();
        this.createPlaylistsFilesTable();
    }

    public void createStorageFilesTable() throws DatabaseException {
        String query = "CREATE TABLE IF NOT EXISTS tracks(uuid VARCHAR(36) PRIMARY KEY, type VARCHAR(16), file VARCHAR(255), " +
                "path VARCHAR(255), duration BIGINT, thumbnail VARCHAR(255), title VARCHAR(255), artist VARCHAR(255), album VARCHAR(255), " +
                "track INT, genre VARCHAR(255), volume INT, created DATETIME, createdby BIGINT, playcount INT)";
        try (Connection conn = this.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.execute();
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    public void createPlaylistsTable() throws DatabaseException {
        String query = "CREATE TABLE IF NOT EXISTS playlists(uuid VARCHAR(36) PRIMARY KEY, name VARCHAR(32), created DATETIME, createdby BIGINT)";
        try (Connection conn = this.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.execute();
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    public void createPlaylistsFilesTable() throws DatabaseException {
        String query = "CREATE TABLE IF NOT EXISTS playlist_tracks(playlist VARCHAR(36), track VARCHAR(36), position INT, primary key (playlist, track))";
        try (Connection conn = this.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.execute();
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    public void loadTracks() {
        String query = "SELECT * FROM tracks";
        try (Connection conn = this.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Track track = new Track(this, result.getString("uuid"))
                        .setType(TrackType.valueOf(result.getString("type")))
                        .setFile(result.getString("file"))
                        .setPath(result.getString("path"))
                        .setDuration(result.getLong("duration"))
                        .setThumbnail(result.getString("thumbnail"))
                        .setTitle(result.getString("title"))
                        .setArtist(result.getString("artist"))
                        .setAlbum(result.getString("album"))
                        .setTrack(result.getInt("track"))
                        .setGenre(result.getString("genre"))
                        .setVolume(result.getInt("volume"))
                        .setCreated(result.getTimestamp("created"))
                        .setCreatedby(result.getLong("createdby"))
                        .setPlaycount(result.getInt("playcount"));
                tracks.put(track.getId(), track);
            }
        } catch (DatabaseException | SQLException e) {
            logger.error("[" + guildId + "] Failed to load track from the database...", e);
        }
    }

    public void loadPlaylists() {
        String query = "SELECT * FROM playlists";
        try (Connection conn = this.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Playlist playlist = new Playlist(this, result.getString("uuid"))
                        .setName(result.getString("name"))
                        .setCreated(result.getTimestamp("created"))
                        .setCreatedby(result.getLong("createdby"));
                playlists.put(playlist.getId(), playlist);
            }
        } catch (DatabaseException | SQLException e) {
            logger.error("[" + guildId + "] Failed to load playlist from the database...", e);
        }
    }

    public void loadPlaylistsTracks() {
        String query = "SELECT * FROM playlist_tracks";
        try (Connection conn = this.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Playlist playlist = playlists.get(result.getString("playlist"));
                playlist.addTrack(result.getString("track"), result.getInt("position"));
            }
        } catch (DatabaseException | SQLException e) {
            logger.error("[" + guildId + "] Failed to load playlist-track from the database...", e);
        }
    }

    public void saveTracks() {
        tracks.values().forEach(this::saveTrack);
    }

    public void saveTrack(Track track) {
        String query = "INSERT INTO tracks(uuid, type, file, path, duration, thumbnail, title, artist, album, track, genre, volume, " +
                "created, createdby, playcount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE " +
                "type = VALUES(type), file = VALUES(file), path = VALUES(path), duration = VALUES(duration), thumbnail = VALUES(thumbnail), " +
                "title = VALUES(title), artist = VALUES(artist), album = VALUES(album), track = VALUES(track), genre = VALUES(genre), " +
                "volume = VALUES(volume), created = VALUES(created), createdby = VALUES(createdby), playcount = VALUES(playcount)";
        try (Connection conn = this.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, track.getId());
            statement.setString(2, track.getType().name());
            statement.setString(3, track.getFile());
            statement.setString(4, track.getPath());
            statement.setLong(5, track.getDuration());
            statement.setString(6, track.getThumbnail());
            statement.setString(7, track.getTitle());
            statement.setString(8, track.getArtist());
            statement.setString(9, track.getAlbum());
            statement.setInt(10, track.getTrack());
            statement.setString(11, track.getGenre());
            statement.setInt(12, track.getVolume());
            statement.setTimestamp(13, track.getCreated());
            statement.setLong(14, track.getCreatedby());
            statement.setInt(15, track.getPlaycount());
            statement.execute();
        } catch (DatabaseException | SQLException e) {
            logger.error("[" + guildId + "] Failed to save track to the database...", e);
        }
    }

    public void savePlaylists() {
        playlists.values().forEach(this::savePlaylist);
    }

    public void savePlaylist(Playlist playlist) {
        String query1 = "INSERT INTO playlists(uuid, name, created, createdby) VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE name = VALUES(name), created = VALUES(created), createdby = VALUES(createdby)";
        try (Connection conn = this.getConnection();
             PreparedStatement statement = conn.prepareStatement(query1)) {
            statement.setString(1, playlist.getId());
            statement.setString(2, playlist.getName());
            statement.setTimestamp(3, playlist.getCreated());
            statement.setLong(4, playlist.getCreatedby());
            statement.execute();
        } catch (DatabaseException | SQLException e) {
            logger.error("[" + guildId + "] Failed to save playlist to the database...", e);
        }

        for (PlaylistTrack track : playlist.getTracks()) {
            String query2 = "INSERT INTO playlist_tracks(playlist, track, position) VALUES (?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE position = VALUES(position)";
            try (Connection conn = this.getConnection();
                 PreparedStatement statement = conn.prepareStatement(query2)) {
                statement.setString(1, playlist.getId());
                statement.setString(2, track.getTrackId());
                statement.setInt(3, track.getPos());
                statement.execute();
            } catch (DatabaseException | SQLException e) {
                logger.error("[" + guildId + "] Failed to save playlist-track to the database...", e);
            }
        }
    }

    public void deleteTrack(Track track) {
        deleteTrack(track.getId());
    }

    public void deleteTrack(String file) {
        String query1 = "DELETE * FROM tracks WHERE uuid = ?";
        try (Connection conn = this.getConnection();
             PreparedStatement statement = conn.prepareStatement(query1)) {
            statement.setString(1, file);
            statement.execute();
        } catch (DatabaseException | SQLException e) {
            logger.error("[" + guildId + "] Failed to delete track from the database...", e);
        }

        String query2 = "DELETE * FROM playlist_tracks WHERE track = ?";
        try (Connection conn = this.getConnection();
             PreparedStatement statement = conn.prepareStatement(query2)) {
            statement.setString(1, file);
            statement.execute();
        } catch (DatabaseException | SQLException e) {
            logger.error("[" + guildId + "] Failed to delete playlist-track from the database...", e);
        }
    }

    public void deletePlaylist(Playlist playlist) {
        deletePlaylist(playlist.getId());
    }

    public void deletePlaylist(String playlist) {
        String query1 = "DELETE * FROM playlists WHERE uuid = ?";
        try (Connection conn = this.getConnection();
             PreparedStatement statement = conn.prepareStatement(query1)) {
            statement.setString(1, playlist);
            statement.execute();
        } catch (DatabaseException | SQLException e) {
            logger.error("[" + guildId + "] Failed to delete playlist from the database...", e);
        }

        String query2 = "DELETE * FROM playlist_tracks WHERE uuid = ?";
        try (Connection conn = this.getConnection();
             PreparedStatement statement = conn.prepareStatement(query2)) {
            statement.setString(1, playlist);
            statement.execute();
        } catch (DatabaseException | SQLException e) {
            logger.error("[" + guildId + "] Failed to delete playlist-track from the database...", e);
        }
    }

    public DataManager getManager() {
        return manager;
    }

    public long getGuildId() {
        return guildId;
    }

    public Map<String, Track> getTracks() {
        return tracks;
    }

    public Map<String, Playlist> getPlaylists() {
        return playlists;
    }

    public Track newTrack(String id) {
        Track track = new Track(this, id);
        tracks.put(track.getId(), track);
        return track;
    }

    public Playlist newPlaylist(String id) {
        Playlist playlist = new Playlist(this, id);
        playlists.put(playlist.getId(), playlist);
        return playlist;
    }
}
