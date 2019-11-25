package org.valdi.jmusicbot.data;

import com.jagrosh.jmusicbot.Bot;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import org.valdi.jmusicbot.data.music.Playlist;
import org.valdi.jmusicbot.utils.TrackUtils;

import java.sql.Timestamp;
import java.util.Optional;

public class Settings {
    protected final DataManager manager;
    protected final long guildId;

    protected long textChannel;
    protected long voiceChannel;
    protected long djRole;
    protected int volume;
    protected String defaultPlaylist;
    protected boolean repeatMode;
    protected Timestamp created;
    protected Timestamp lastChange;

    public Settings(DataManager manager, long guildId) {
        this.manager = manager;
        this.guildId = guildId;

        this.textChannel = 0L;
        this.voiceChannel = 0L;
        this.djRole = 0L;
        this.volume = -1;
        this.defaultPlaylist = "";
        this.repeatMode = false;
        this.created = new Timestamp(new java.util.Date().getTime());
        this.lastChange = new Timestamp(new java.util.Date().getTime());
    }

    public long getGuildId() {
        return guildId;
    }

    public long getTextChannelId() {
        return textChannel;
    }

    public Optional<TextChannel> getTextChannel() {
        return Optional.ofNullable(manager.getMain().getBot().getJDA().getGuildById(guildId).getTextChannelById(textChannel));
    }

    public Settings setTextChannelId(long textChannel) {
        this.textChannel = textChannel;
        return this;
    }

    public long getVoiceChannelId() {
        return voiceChannel;
    }

    public Optional<VoiceChannel> getVoiceChannel() {
        return Optional.ofNullable(manager.getMain().getBot().getJDA().getGuildById(guildId).getVoiceChannelById(voiceChannel));
    }

    public Settings setVoiceChannelId(long voiceChannel) {
        this.voiceChannel = voiceChannel;
        return this;
    }

    public long getDjRoleId() {
        return djRole;
    }

    public Optional<Role> getDjRole() {
        return Optional.ofNullable(manager.getMain().getBot().getJDA().getGuildById(guildId).getRoleById(djRole));
    }

    public Settings setDjRoleId(long djRole) {
        this.djRole = djRole;
        return this;
    }

    public int getVolume() {
        return volume;
    }

    public Settings setVolume(int volume) {
        this.volume = volume;
        return this;
    }

    public String getDefaultPlaylistId() {
        return defaultPlaylist;
    }

    public Optional<Playlist> getDefaultPlaylist() {
        return TrackUtils.getPlaylist(manager.getDatabase(guildId), defaultPlaylist);
    }

    public Settings setDefaultPlaylistId(String defaultPlaylist) {
        this.defaultPlaylist = defaultPlaylist;
        return this;
    }

    public boolean isRepeatMode() {
        return repeatMode;
    }

    public Settings setRepeatMode(boolean repeatMode) {
        this.repeatMode = repeatMode;
        return this;
    }

    public Timestamp getCreated() {
        return created;
    }

    public Settings setCreated(Timestamp created) {
        this.created = created;
        return this;
    }

    public Timestamp getLastChange() {
        return lastChange;
    }

    public Settings setLastChange(Timestamp lastChange) {
        this.lastChange = lastChange;
        return this;
    }

    public void save() {
        this.lastChange = new Timestamp(new java.util.Date().getTime());
        manager.saveSettings(guildId);
    }
}
