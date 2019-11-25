package com.jagrosh.jmusicbot.commands.dj.addtrack;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.commands.DJCommand;
import com.jagrosh.jmusicbot.utils.FormatUtil;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Message;
import org.valdi.jmusicbot.data.music.Track;
import org.valdi.jmusicbot.data.music.TrackType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AddTrackStream extends DJCommand  {
    private final String loadingEmoji;

    public AddTrackStream(Bot bot, String loadingEmoji) {
        super(bot);
        this.loadingEmoji = loadingEmoji;
        this.name = "stream";
        this.help = "add a stream track to the database";
        this.arguments = "<link>";
        this.beListening = false;
        this.bePlaying = false;
    }

    @Override
    public void doCommand(CommandEvent event) {
        if (event.getArgs().isEmpty()) {
            event.replyError("You need to specify at least a valid link!");
            return;
        }

        String link = event.getArgs();
        String[] args = event.getArgs().split("\\s+");
        if (!event.getArgs().isEmpty()) {
            link = args[0];
        }

        String finalLink = link;
        event.reply(loadingEmoji + " Loading... `[" + finalLink + "]`", m -> bot.getPlayerManager().loadItemOrdered(event.getGuild(), finalLink, new ResultHandler(m, event)));
    }

    private class ResultHandler implements AudioLoadResultHandler {
        private final Message m;
        private final CommandEvent event;

        private ResultHandler(Message m, CommandEvent event) {
            this.m = m;
            this.event = event;
        }

        @Override
        public void trackLoaded(AudioTrack audioTrack) {
            if(!(audioTrack.getSourceManager() instanceof YoutubeAudioSourceManager)) {
                m.editMessage(event.getClient().getError() + " Invalid url source! Only youtube is currently supported!").queue();
                return;
            }

            for(Track track : bot.getDataManager().getDatabase(event.getGuild()).getTracks().values()) {
                if(track.getFile().equals(audioTrack.getInfo().uri)) {
                    m.editMessage(event.getClient().getError() + " A track with the same link is already present in the database! **"
                            + track.getTitle() + "** (`" + FormatUtil.formatTime(track.getDuration()) + "`)").queue();
                    return;
                }
            }

            Track track = bot.getDataManager().getDatabase(event.getGuild()).newTrack(UUID.randomUUID().toString());
            track.setFile(audioTrack.getInfo().uri);
            track.setDuration(audioTrack.getDuration());

            track.setTitle(audioTrack.getInfo().title);
            //track.setArtist(audioTrack.getInfo().author);
            track.setCreatedby(event.getAuthor().getIdLong());

            if(audioTrack.getSourceManager() instanceof YoutubeAudioSourceManager) {
                track.setType(TrackType.YOUTUBE);
            }

            /*if(audioTrack.getSourceManager() instanceof SoundCloudAudioSourceManager) {
                track.setType(TrackType.SOUNDCLOUD);
            }*/

            try {
                String argsNoLink = event.getArgs().replace(audioTrack.getInfo().uri, "");
                argsNoLink = argsNoLink.trim();

                List<String> args = new ArrayList<>(Arrays.asList(argsNoLink.split(",")));
                args.add(argsNoLink);

                for (String arg : args) {
                    arg = arg.trim();
                    if(!arg.contains("=")) {
                        continue;
                    }
                    String[] cmd = arg.split("=", 2);
                    switch (cmd[0]) {
                        case "title":
                            if(cmd.length == 1) {
                                continue;
                            }
                            track.setTitle(cmd[1]);
                            break;
                        case "artist":
                            if(cmd.length == 1) {
                                continue;
                            }
                            track.setArtist(cmd[1]);
                            break;
                        case "album":
                            if(cmd.length == 1) {
                                continue;
                            }
                            track.setAlbum(cmd[1]);
                            break;
                        case "track":
                            if(cmd.length == 1) {
                                continue;
                            }
                            track.setTrack(Integer.parseInt(cmd[1]));
                            break;
                        case "genre":
                            if(cmd.length == 1) {
                                continue;
                            }
                            track.setGenre(cmd[1]);
                            break;
                        case "volume":
                            if(cmd.length == 1) {
                                continue;
                            }
                            track.setVolume(Integer.parseInt(cmd[1]));
                            break;
                    }
                }
                String addMsg = FormatUtil.filter(event.getClient().getSuccess() + " Added **" + track.getTitle()
                        + "** (`" + FormatUtil.formatTime(track.getDuration()) + "`) to the database.");
                m.editMessage(addMsg).queue();
                bot.getDataManager().getDatabase(event.getGuild()).saveTrack(track);
            } catch (Exception e) {
                bot.getDataManager().getDatabase(event.getGuild()).getTracks().remove(track.getId());
                m.editMessage(event.getClient().getError() + " Error loading track. Check console!").queue();
                e.printStackTrace();
            }
        }

        @Override
        public void playlistLoaded(AudioPlaylist audioPlaylist) {
            m.editMessage(event.getClient().getError() + " You must provide a track url, playlist not allowed!").queue();
        }

        @Override
        public void noMatches() {
            m.editMessage(FormatUtil.filter(event.getClient().getWarning() + " No results found for `" + event.getArgs() + "`.")).queue();
        }

        @Override
        public void loadFailed(FriendlyException e) {
            m.editMessage(event.getClient().getError() + " Error loading track.").queue();
        }
    }
}
