package org.valdi.jmusicbot.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.valdi.SuperApiX.common.databases.DatabaseException;
import org.valdi.SuperApiX.common.databases.types.H2Database;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class GlobalDatabase extends H2Database {
    private final Logger logger = LogManager.getLogger("Global Database");

    private final DataManager manager;
    private final Gson gson;

    private final Map<Long, Settings> settings;

    public GlobalDatabase(DataManager manager, File file) throws DatabaseException {
        super(manager.getMain(), file, ";MODE=MYSQL", "username",
                "password", 5, "JMusicBot-Global");
        this.manager = manager;

        // excludeFieldsWithoutExposeAnnotation - this means that every field to be stored should use @Expose
        // enableComplexMapKeySerialization - forces GSON to use TypeAdapters even for Map keys
        GsonBuilder builder = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .enableComplexMapKeySerialization();
        // Keep null in the database
        builder.serializeNulls();
        // Allow characters like < or > without escaping them
        builder.disableHtmlEscaping();
        this.gson = builder.create();

        this.settings = new HashMap<>();
    }

    public void createTables() throws DatabaseException {
        String query = "CREATE TABLE IF NOT EXISTS bots(uuid BIGINT PRIMARY KEY, textchannel LONGTEXT, voicechannel LONGTEXT, " +
                "djrole LONGTEXT, volume INT, defaultplaylist VARCHAR(36), repeatmode BOOLEAN, created DATETIME, lastchange DATETIME)";
        try(Connection conn = this.getConnection();
            PreparedStatement statement = conn.prepareStatement(query)) {
            statement.execute();
        } catch(Exception e) {
            throw new DatabaseException(e);
        }
    }

    public void loadSettings() {
        String query = "SELECT * FROM bots";
        try(Connection conn = this.getConnection();
            PreparedStatement statement = conn.prepareStatement(query)) {
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                Settings setting = new Settings(manager, result.getLong("uuid"))
                        .setTextChannelId(result.getLong("textchannel"))
                        .setVoiceChannelId(result.getLong("voicechannel"))
                        .setDjRoleId(result.getLong("djrole"))
                        .setVolume(result.getInt("volume"))
                        .setDefaultPlaylistId(result.getString("defaultplaylist"))
                        .setRepeatMode(result.getBoolean("repeatmode"))
                        .setCreated(result.getTimestamp("created"))
                        .setLastChange(result.getTimestamp("lastchange"));
                settings.put(setting.getGuildId(), setting);
            }
        } catch (DatabaseException | SQLException e) {
            logger.error("Failed load settings from the database...", e);
        }
    }

    public void saveSettings(long guildId) {
        if(!settings.containsKey(guildId)) {
            settings.put(guildId, new Settings(manager, guildId));
        }
        Settings setting = settings.get(guildId);
        String query = "INSERT INTO bots(uuid, textchannel, voicechannel, djrole, volume, defaultplaylist, repeatmode, created, " +
                "lastchange) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE textchannel = VALUES(textchannel), " +
                "voicechannel = VALUES(voicechannel), djrole = VALUES(djrole), volume = VALUES(volume), defaultplaylist = VALUES(defaultplaylist), " +
                "repeatmode = VALUES(repeatmode), created = VALUES(created), lastchange = VALUES(lastchange)";
        try (Connection conn = this.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, setting.getGuildId());
            statement.setLong(2, setting.getTextChannelId());
            statement.setLong(3, setting.getVoiceChannelId());
            statement.setLong(4, setting.getDjRoleId());
            statement.setInt(5, setting.getVolume());
            statement.setString(6, setting.getDefaultPlaylistId());
            statement.setBoolean(7, setting.isRepeatMode());
            statement.setTimestamp(8, setting.getCreated());
            statement.setTimestamp(9, setting.getLastChange());
            statement.execute();
        } catch (DatabaseException | SQLException e) {
            logger.error(" Failed to save settings to the database...", e);
        }
    }

    public Map<Long, Settings> getSettings() {
        return settings;
    }
}
