package org.valdi.jmusicbot.data;

import com.jagrosh.jdautilities.command.GuildSettingsManager;
import net.dv8tion.jda.core.entities.Guild;
import org.valdi.SuperApiX.common.databases.DatabaseException;
import org.valdi.jmusicbot.JMusicBot;
import org.valdi.jmusicbot.data.music.Playlist;
import org.valdi.jmusicbot.data.music.Track;
import org.valdi.jmusicbot.data.music.TrackType;

import java.io.File;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class DataManager implements GuildSettingsManager<Settings> {
    private final JMusicBot main;
    private final File dataFolder;
    private final GlobalDatabase globalDb;
    private final Map<Long, BotDatabase> botDbs;

    public DataManager(JMusicBot main) throws DatabaseException {
        this.main = main;

        File webFolder = new File(main.getDataFolder(), "web");
        this.dataFolder = new File(webFolder, "data");
        File dbFolder = this.getDbFolder();
        File globalDatabase = new File(dbFolder, "global");

        this.globalDb = new GlobalDatabase(this, globalDatabase);
        this.botDbs = new HashMap<>();
    }

    public void loadSettings() throws DatabaseException {
        globalDb.createTables();
        globalDb.loadSettings();
    }

    public void loadData(Guild guild) throws DatabaseException {
        File guildDb = new File(this.getDbFolder(), String.valueOf(guild.getIdLong()));
        BotDatabase db = new BotDatabase(this, guild.getIdLong(), guildDb);
        botDbs.put(guild.getIdLong(), db);
        db.createTables();

        db.loadTracks();
        db.loadPlaylists();

        Playlist playlist = db.newPlaylist("58e9aa6e-fb4a-11e9-8f0b-362b9e155667")
                .setName("Default Playlist")
                .setCreated(new Timestamp(new java.util.Date().getTime()))
                .setCreatedby(0L);

        for(Track track : db.getTracks().values()) {
            playlist.addTrack(track.getId());
        }

        db.saveTracks();
        db.savePlaylists();

        this.getCacheFolder(guild);
        this.getStoreFolder(guild);
    }

    public File getDbFolder() {
        File dbFolder = new File(dataFolder, "db");
        if(!dbFolder.exists()) {
            dbFolder.mkdirs();
        }
        return dbFolder;
    }

    public File getTempFolder() {
        File tempFolder = new File(dataFolder, "tmp");
        if(!tempFolder.exists()) {
            tempFolder.mkdirs();
        }
        return tempFolder;
    }

    public File getCacheFolder(Guild guild) {
        File cacheFolder = new File(dataFolder, "cache");
        File botCacheFolder = new File(cacheFolder, guild.getId());
        if(!botCacheFolder.exists()) {
            botCacheFolder.mkdirs();
        }
        return botCacheFolder;
    }

    public File getStoreFolder(Guild guild) {
        File storeFolder = new File(dataFolder, "store");
        File botStoreFolder = new File(storeFolder, guild.getId());
        if(!botStoreFolder.exists()) {
            botStoreFolder.mkdirs();
        }
        return botStoreFolder;
    }

    public JMusicBot getMain() {
        return main;
    }

    public GlobalDatabase getGlobalDatabase() {
        return globalDb;
    }

    public Settings getSettings(Guild guild) {
        return getSettings(guild.getIdLong());
    }

    public Settings getSettings(long guildId) {
        if(!globalDb.getSettings().containsKey(guildId)) {
            Settings settings = new Settings(this, guildId);
            globalDb.getSettings().put(guildId, settings);
            settings.save();
        }
        return globalDb.getSettings().get(guildId);
    }

    public void saveSettings(Guild guild) {
        saveSettings(guild.getIdLong());
    }

    public void saveSettings(long guildId) {
        globalDb.saveSettings(guildId);
    }

    public BotDatabase getDatabase(Guild guild) {
        return getDatabase(guild.getIdLong());
    }

    public BotDatabase getDatabase(long guildId) {
        return botDbs.get(guildId);
    }
}
