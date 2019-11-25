package com.jagrosh.jmusicbot.commands.dj;

import java.util.Collection;
import java.util.Optional;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.commands.DJCommand;
import org.valdi.jmusicbot.data.BotDatabase;
import org.valdi.jmusicbot.data.Settings;
import org.valdi.jmusicbot.data.music.Playlist;
import org.valdi.jmusicbot.data.music.Track;
import org.valdi.jmusicbot.utils.TrackUtils;

/**
 * @author John Grosh <john.a.grosh@gmail.com>
 */
public class PlaylistCmd extends DJCommand {

    public PlaylistCmd(Bot bot) {
        super(bot);

        this.guildOnly = true;
        this.beListening = false;
        this.bePlaying = false;

        this.name = "playlist";
        this.arguments = "<append|delete|make|setdefault>";
        this.help = "playlist management";
        this.children = new DJCommand[]{
                new ListCmd(bot),
                new AppendlistCmd(bot),
                new DeletelistCmd(bot),
                new MakelistCmd(bot),
                new DefaultlistCmd(bot)
        };
    }

    @Override
    public void doCommand(CommandEvent event) {
        StringBuilder builder = new StringBuilder(event.getClient().getWarning() + " Playlist Management Commands:\n");
        for (Command cmd : this.children)
            builder.append("\n`").append(event.getClient().getPrefix()).append(" ").append(name).append(" ").append(cmd.getName())
                    .append(" ").append(cmd.getArguments() == null ? "" : cmd.getArguments()).append("` - ").append(cmd.getHelp());
        event.reply(builder.toString());
    }

    public class MakelistCmd extends DJCommand {
        public MakelistCmd(Bot bot) {
            super(bot);

            this.guildOnly = true;
            this.beListening = false;
            this.bePlaying = false;

            this.name = "make";
            this.aliases = new String[]{"create"};
            this.arguments = "<name>";
            this.help = "makes a new playlist";
        }

        @Override
        public void doCommand(CommandEvent event) {
            BotDatabase db = bot.getDataManager().getDatabase(event.getGuild());
            String pName = event.getArgs();

            Optional<Playlist> opt = TrackUtils.exactPlaylist(db, pName);
            if (!opt.isPresent()) {
                try {
                    Playlist playlist = db.newPlaylist(pName);
                    db.savePlaylist(playlist);
                    event.reply(event.getClient().getSuccess() + " Successfully created playlist `" + playlist.getName() + "`!");
                } catch (Exception e) {
                    event.reply(event.getClient().getError() + " I was unable to create the playlist: " + e.getLocalizedMessage());
                }
                return;
            }

            event.reply(event.getClient().getError() + " Playlist `" + opt.get().getName() + "` already exists! (`" + opt.get().getId() + "`)");
        }
    }

    public class DeletelistCmd extends DJCommand {
        public DeletelistCmd(Bot bot) {
            super(bot);

            this.guildOnly = true;
            this.beListening = false;
            this.bePlaying = false;

            this.name = "delete";
            this.aliases = new String[]{"remove"};
            this.arguments = "<id>";
            this.help = "deletes an existing playlist";
        }

        @Override
        public void doCommand(CommandEvent event) {
            BotDatabase db = bot.getDataManager().getDatabase(event.getGuild());
            String pName = event.getArgs();

            Optional<Playlist> playlist;
            if(pName.startsWith("<") && pName.endsWith(">")) {
                pName = pName.substring(1, pName.length() - 1);
                playlist = TrackUtils.getPlaylist(db, pName);
            } else {
                playlist = TrackUtils.exactPlaylist(db, pName);
            }
            if (playlist.isPresent()) {
                try {
                    db.deletePlaylist(playlist.get());
                    event.reply(event.getClient().getSuccess() + " Successfully deleted playlist `" + playlist.get().getName() + "`!");
                } catch (Exception e) {
                    event.reply(event.getClient().getError() + " I was unable to delete the playlist: " + e.getLocalizedMessage());
                }
                return;
            }

            event.reply(event.getClient().getError() + " Playlist with id `" + pName + "` doesn't exist!");
        }
    }

    public class AppendlistCmd extends DJCommand {
        public AppendlistCmd(Bot bot) {
            super(bot);

            this.guildOnly = true;
            this.beListening = false;
            this.bePlaying = false;

            this.name = "append";
            this.aliases = new String[]{"add"};
            this.arguments = "<playlist> <track>|<track>|...";
            this.help = "appends songs to an existing playlist";
        }

        @Override
        public void doCommand(CommandEvent event) {
            String[] parts = event.getArgs().split("\\s+");
            if (parts.length < 2) {
                event.reply(event.getClient().getError() + " Please include a playlist id and track id to add!");
                return;
            }

            BotDatabase db = bot.getDataManager().getDatabase(event.getGuild());
            String pName = parts[0];

            Optional<Playlist> playlist;
            if(pName.startsWith("<") && pName.endsWith(">")) {
                pName = pName.substring(1, pName.length() - 1);
                playlist = TrackUtils.getPlaylist(db, pName);
            } else {
                playlist = TrackUtils.exactPlaylist(db, pName);
            }
            if (playlist.isPresent()) {
                String[] tracks = parts[1].split("\\|");
                int count = 0;
                for (String trackId : tracks) {
                    Optional<Track> track = TrackUtils.getTrack(db, trackId);
                    if (track.isPresent()) {
                        playlist.get().addTrack(track.get().getId());
                        count++;
                    }
                }
                try {
                    db.savePlaylist(playlist.get());
                    event.reply(event.getClient().getSuccess() + " Successfully added " + count + " items to playlist `" + playlist.get().getName() + "`! (`" + playlist.get().getId() + "`)");
                } catch (Exception e) {
                    event.reply(event.getClient().getError() + " I was unable to append to the playlist: " + e.getLocalizedMessage());
                }
                return;
            }

            event.reply(event.getClient().getError() + " Playlist with id `" + pName + "` doesn't exist!");
        }
    }

    public class DefaultlistCmd extends DJCommand {
        public DefaultlistCmd(Bot bot) {
            super(bot);

            this.guildOnly = true;
            this.beListening = false;
            this.bePlaying = false;

            this.name = "setdefault";
            this.aliases = new String[]{"default"};
            this.arguments = "<playlist|NONE>";
            this.help = "sets the default playlist for the server";
        }

        @Override
        public void doCommand(CommandEvent event) {
            if (event.getArgs().isEmpty()) {
                event.reply(event.getClient().getError() + " Please include a playlist name or NONE");
                return;
            }
            if (event.getArgs().equalsIgnoreCase("none")) {
                Settings settings = event.getClient().getSettingsFor(event.getGuild());
                settings.setDefaultPlaylistId("");
                settings.save();
                event.reply(event.getClient().getSuccess() + " Cleared the default playlist for **" + event.getGuild().getName() + "**");
                return;
            }
            BotDatabase db = bot.getDataManager().getDatabase(event.getGuild());
            String pName = event.getArgs();

            Optional<Playlist> opt;
            if(pName.startsWith("<") && pName.endsWith(">")) {
                pName = pName.substring(1, pName.length() - 1);
                opt = TrackUtils.getPlaylist(db, pName);
            } else {
                opt = TrackUtils.exactPlaylist(db, pName);
            }
            if (opt.isPresent()) {
                Settings settings = event.getClient().getSettingsFor(event.getGuild());
                settings.setDefaultPlaylistId(opt.get().getId());
                settings.save();
                event.reply(event.getClient().getSuccess() + " The default playlist for **" + event.getGuild().getName() + "** is now `" + opt.get().getName() + "` (`" + opt.get().getId() + "`)");
                return;
            }

            event.reply(event.getClient().getError() + " Could not find playlist with id `" + pName + "`!");
        }
    }

    public class ListCmd extends DJCommand {
        public ListCmd(Bot bot) {
            super(bot);

            this.guildOnly = true;
            this.beListening = false;
            this.bePlaying = false;

            this.name = "all";
            this.aliases = new String[]{"available", "list"};
            this.help = "lists all available playlists";
        }

        @Override
        public void doCommand(CommandEvent event) {
            Collection<Playlist> playlists = bot.getDataManager().getDatabase(event.getGuild()).getPlaylists().values();
            if (playlists.isEmpty()) {
                event.reply(event.getClient().getWarning() + " There are no playlists!");
                return;
            }

            StringBuilder builder = new StringBuilder(event.getClient().getSuccess() + " Available playlists:\n");
            int count = 0;
            for (Playlist p : playlists) {
                builder.append(++count).append(". Id: `").append(p.getId()).append("`").append(" Name: `").append(p.getName()).append("`\n");
            }
            builder.append("\nType `").append(event.getClient().getTextualPrefix()).append("play playlist <id>` to play a playlist");
            event.reply(builder.toString());
        }
    }
}
