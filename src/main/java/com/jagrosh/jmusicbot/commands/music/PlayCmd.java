package com.jagrosh.jmusicbot.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException.Severity;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.menu.ButtonMenu;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.audio.AudioHandler;
import com.jagrosh.jmusicbot.audio.QueuedTrack;
import com.jagrosh.jmusicbot.commands.MusicCommand;
import com.jagrosh.jmusicbot.utils.FormatUtil;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.exceptions.PermissionException;
import org.valdi.jmusicbot.data.BotDatabase;
import org.valdi.jmusicbot.data.Settings;
import org.valdi.jmusicbot.data.music.Playlist;
import org.valdi.jmusicbot.data.music.Track;
import org.valdi.jmusicbot.utils.ChecksumUtils;
import org.valdi.jmusicbot.utils.TrackUtils;

/**
 * @author John Grosh <john.a.grosh@gmail.com>
 */
public class PlayCmd extends MusicCommand {
    private final static String LOAD = "\uD83D\uDCE5"; // 📥
    private final static String CANCEL = "\uD83D\uDEAB"; // 🚫

    private final String loadingEmoji;

    public PlayCmd(Bot bot, String loadingEmoji) {
        super(bot);
        this.loadingEmoji = loadingEmoji;
        this.name = "play";
        this.arguments = "<title|URL|subcommand>";
        this.help = "plays the provided song";
        this.beListening = true;
        this.bePlaying = false;
        this.children = new Command[]{new PlaylistCmd(bot)};
    }

    @Override
    public void doCommand(CommandEvent event) {
        if (event.getArgs().isEmpty() && event.getMessage().getAttachments().isEmpty()) {
            AudioHandler handler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
            if (handler.getPlayer().getPlayingTrack() != null && handler.getPlayer().isPaused()) {
                boolean isDJ = event.getMember().hasPermission(Permission.MANAGE_SERVER);
                if (!isDJ)
                    isDJ = event.isOwner();
                Settings settings = event.getClient().getSettingsFor(event.getGuild());
                Optional<Role> dj = settings.getDjRole();
                if (!isDJ && dj.isPresent())
                    isDJ = event.getMember().getRoles().contains(dj.get());
                if (!isDJ)
                    event.replyError("Only DJs can unpause the player!");
                else {
                    String title = handler.getPlayer().getPlayingTrack().getInfo().title;
                    Optional<Track> t = TrackUtils.getTrack(bot.getDataManager().getDatabase(event.getGuild()), handler.getPlayer().getPlayingTrack());
                    if(t.isPresent()) {
                        title = t.get().getTitle();
                    }
                    handler.getPlayer().setPaused(false);
                    event.replySuccess("Resumed **" + title + "**.");
                }
                return;
            }
            StringBuilder builder = new StringBuilder(event.getClient().getWarning() + " Play Commands:\n");
            builder.append("\n`").append(event.getClient().getPrefix()).append(name).append(" <song title>` - plays the first result from Youtube");
            builder.append("\n`").append(event.getClient().getPrefix()).append(name).append(" <URL>` - plays the provided song, playlist, or stream");
            for (Command cmd : children)
                builder.append("\n`").append(event.getClient().getPrefix()).append(name).append(" ").append(cmd.getName()).append(" ").append(cmd.getArguments()).append("` - ").append(cmd.getHelp());
            event.reply(builder.toString());
            return;
        }

        String args;
        if(!event.getMessage().getAttachments().isEmpty()) {
            Message.Attachment a = event.getMessage().getAttachments().get(0);
            event.reply(loadingEmoji + " Loading... `[" + a.getFileName() + "]`", m -> bot.getPlayerManager()
                    .loadItemOrdered(event.getGuild(), a.getUrl(), new SearchResultHandler(m, event, false)));
            return;
        }
        if(event.getArgs().startsWith("<") && event.getArgs().endsWith(">")) {
            args = event.getArgs().substring(1, event.getArgs().length() - 1);
            event.reply(loadingEmoji + " Loading... `[" + args + "]`", m -> bot.getPlayerManager()
                    .loadItemOrdered(event.getGuild(), args, new SearchResultHandler(m, event, false)));
            return;
        }

        args = event.getArgs();
        Collection<Track> tracks = bot.getDataManager().getDatabase(event.getGuild()).getTracks().values();
        for(Track track : tracks) {
            if(!track.getTitle().toLowerCase().contains(args.toLowerCase())) {
                continue;
            }

            switch (track.getType()) {
                case FILE:
                    try {
                        File file = new File(bot.getDataManager().getStoreFolder(event.getGuild()), track.getPath());
                        if(!ChecksumUtils.checkFileHash(file, "SHA-256")) {
                            event.replyError("File corrupted in the database! Check console for errors.");
                            return;
                        }
                        event.reply(loadingEmoji + " Loading... `[" + track.getTitle() + "]`", m -> bot.getPlayerManager()
                                .loadItemOrdered(event.getGuild(), file.getPath(), new DataHandler(m, event, track)));
                    } catch (IOException | NoSuchAlgorithmException e) {
                        event.replyError("Error loading track! Check console for errors.");
                        e.printStackTrace();
                        return;
                    }
                    return;
                case YOUTUBE:
                    event.reply(loadingEmoji + " Loading... `[" + track.getTitle() + "]`", m -> bot.getPlayerManager()
                            .loadItemOrdered(event.getGuild(), track.getFile(), new DataHandler(m, event, track)));
                    return;
                case SOUNDCLOUD:
                    return;
            }
        }
        event.reply(FormatUtil.filter(event.getClient().getWarning() + " No results found for `" + event.getArgs() + "`."));
    }

    private class DataHandler implements AudioLoadResultHandler {
        private final Message m;
        private final CommandEvent event;
        private final Track track;

        private DataHandler(Message m, CommandEvent event, Track track) {
            this.m = m;
            this.event = event;
            this.track = track;
        }

        @Override
        public void trackLoaded(AudioTrack audioTrack) {
            AudioHandler handler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
            int pos = handler.addTrack(new QueuedTrack(audioTrack, event.getAuthor())) + 1;
            String addMsg = FormatUtil.filter(event.getClient().getSuccess() + " Added **" + track.getTitle()
                    + "** (`" + FormatUtil.formatTime(track.getDuration()) + "`) " + (pos == 0 ? "to begin playing" : " to the queue at position " + pos));
            m.editMessage(addMsg).queue();
        }

        @Override
        public void playlistLoaded(AudioPlaylist audioPlaylist) {
            m.editMessage(event.getClient().getError() + " You must provide a track url, playlist not allowed!").queue();
        }

        @Override
        public void noMatches() {
            m.editMessage(FormatUtil.filter(event.getClient().getWarning() + " No results found for `" + event.getArgs() + "`'s link.")).queue();
        }

        @Override
        public void loadFailed(FriendlyException e) {
            m.editMessage(event.getClient().getError() + " Error loading track.").queue();
        }
    }

    private class SearchResultHandler implements AudioLoadResultHandler {
        private final Message m;
        private final CommandEvent event;
        private final boolean ytsearch;

        private SearchResultHandler(Message m, CommandEvent event, boolean ytsearch) {
            this.m = m;
            this.event = event;
            this.ytsearch = ytsearch;
        }

        private void loadSingle(AudioTrack track, AudioPlaylist playlist) {
            if (bot.getConfig().isTooLong(track)) {
                m.editMessage(FormatUtil.filter(event.getClient().getWarning() + " This track (**" + track.getInfo().title + "**) is longer than the allowed maximum: `"
                        + FormatUtil.formatTime(track.getDuration()) + "` > `" + FormatUtil.formatTime(bot.getConfig().getMaxTime() * 1000) + "`")).queue();
                return;
            }
            AudioHandler handler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
            int pos = handler.addTrack(new QueuedTrack(track, event.getAuthor())) + 1;
            String addMsg = FormatUtil.filter(event.getClient().getSuccess() + " Added **" + track.getInfo().title
                    + "** (`" + FormatUtil.formatTime(track.getDuration()) + "`) " + (pos == 0 ? "to begin playing" : " to the queue at position " + pos));
            if (playlist == null || !event.getSelfMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_ADD_REACTION))
                m.editMessage(addMsg).queue();
            else {
                new ButtonMenu.Builder()
                        .setText(addMsg + "\n" + event.getClient().getWarning() + " This track has a playlist of **" + playlist.getTracks().size() + "** tracks attached. Select " + LOAD + " to load playlist.")
                        .setChoices(LOAD, CANCEL)
                        .setEventWaiter(bot.getWaiter())
                        .setTimeout(30, TimeUnit.SECONDS)
                        .setAction(re -> {
                            if (re.getName().equals(LOAD))
                                m.editMessage(addMsg + "\n" + event.getClient().getSuccess() + " Loaded **" + loadPlaylist(playlist, track) + "** additional tracks!").queue();
                            else
                                m.editMessage(addMsg).queue();
                        }).setFinalAction(m -> {
                            try {
                                m.clearReactions().queue();
                            } catch (PermissionException ignore) {
                            }
                        }).build()
                        .display(m);
            }
        }

        private int loadPlaylist(AudioPlaylist playlist, AudioTrack exclude) {
            int[] count = {0};
            playlist.getTracks().forEach((track) -> {
                if (!bot.getConfig().isTooLong(track) && !track.equals(exclude)) {
                    AudioHandler handler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
                    handler.addTrack(new QueuedTrack(track, event.getAuthor()));
                    count[0]++;
                }
            });
            return count[0];
        }

        @Override
        public void trackLoaded(AudioTrack track) {
            loadSingle(track, null);
        }

        @Override
        public void playlistLoaded(AudioPlaylist playlist) {
            if (playlist.getTracks().size() == 1 || playlist.isSearchResult()) {
                AudioTrack single = playlist.getSelectedTrack() == null ? playlist.getTracks().get(0) : playlist.getSelectedTrack();
                loadSingle(single, null);
            } else if (playlist.getSelectedTrack() != null) {
                AudioTrack single = playlist.getSelectedTrack();
                loadSingle(single, playlist);
            } else {
                int count = loadPlaylist(playlist, null);
                if (count == 0) {
                    m.editMessage(FormatUtil.filter(event.getClient().getWarning() + " All entries in this playlist " + (playlist.getName() == null ? "" : "(**" + playlist.getName()
                            + "**) ") + "were longer than the allowed maximum (`" + bot.getConfig().getMaxTime() + "`)")).queue();
                } else {
                    m.editMessage(FormatUtil.filter(event.getClient().getSuccess() + " Found "
                            + (playlist.getName() == null ? "a playlist" : "playlist **" + playlist.getName() + "**") + " with `"
                            + playlist.getTracks().size() + "` entries; added to the queue!"
                            + (count < playlist.getTracks().size() ? "\n" + event.getClient().getWarning() + " Tracks longer than the allowed maximum (`"
                            + bot.getConfig().getMaxTime() + "`) have been omitted." : ""))).queue();
                }
            }
        }

        @Override
        public void noMatches() {
            if (ytsearch)
                m.editMessage(FormatUtil.filter(event.getClient().getWarning() + " No results found for `" + event.getArgs() + "`.")).queue();
            else
                bot.getPlayerManager().loadItemOrdered(event.getGuild(), "ytsearch:" + event.getArgs(), new SearchResultHandler(m, event, true));
        }

        @Override
        public void loadFailed(FriendlyException throwable) {
            if (throwable.severity == Severity.COMMON)
                m.editMessage(event.getClient().getError() + " Error loading: " + throwable.getMessage()).queue();
            else
                m.editMessage(event.getClient().getError() + " Error loading track.").queue();
        }
    }

    public class PlaylistCmd extends MusicCommand {
        public PlaylistCmd(Bot bot) {
            super(bot);
            this.name = "playlist";
            this.aliases = new String[]{"pl"};
            this.arguments = "<playlist>";
            this.help = "plays the provided playlist";
            this.beListening = true;
            this.bePlaying = false;
        }

        @Override
        public void doCommand(CommandEvent event) {
            if (event.getArgs().isEmpty()) {
                event.reply(event.getClient().getError() + " Please include a playlist id.");
                return;
            }
            BotDatabase db = bot.getDataManager().getDatabase(event.getGuild());
            String pName = event.getArgs();

            Optional<Playlist> playlist;
            if(pName.startsWith("<") && pName.endsWith(">")) {
                pName = pName.substring(1, pName.length() - 1);
                playlist = TrackUtils.getPlaylist(db, pName);
            } else {
                playlist = TrackUtils.firstMatchPlaylists(db, pName);
            }
            if(playlist.isPresent()) {
                event.getChannel().sendMessage(loadingEmoji + " Loading playlist **" + playlist.get().getName() + "**... (" + playlist.get().getTracks().size() + " items)").queue(m -> {
                    AudioHandler handler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
                    playlist.get().loadTracks(event.getGuild(), bot.getPlayerManager(), at -> handler.addTrack(new QueuedTrack(at, event.getAuthor())), () -> {
                        StringBuilder builder = new StringBuilder(playlist.get().getTracks().isEmpty()
                                ? event.getClient().getWarning() + " No tracks were loaded!"
                                : event.getClient().getSuccess() + " Loaded **" + playlist.get().getTracks().size() + "** tracks!");
                        if (!playlist.get().getErrors().isEmpty())
                            builder.append("\nThe following tracks failed to load:");
                        playlist.get().getErrors().forEach(err -> builder.append("\n`[").append(err.getIndex() + 1).append("]` **").append(err.getItem()).append("**: ").append(err.getReason()));
                        String str = builder.toString();
                        if (str.length() > 2000)
                            str = str.substring(0, 1994) + " (...)";
                        m.editMessage(FormatUtil.filter(str)).queue();
                    });
                });
                return;
            }

            event.replyError("I could not find playlist `" + event.getArgs() + "`.");
        }
    }
}
