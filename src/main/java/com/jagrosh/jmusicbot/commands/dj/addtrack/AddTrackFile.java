package com.jagrosh.jmusicbot.commands.dj.addtrack;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.commands.DJCommand;
import com.jagrosh.jmusicbot.utils.FormatUtil;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Message;
import org.apache.commons.io.FilenameUtils;
import org.valdi.jmusicbot.data.music.Track;
import org.valdi.jmusicbot.data.music.TrackType;
import org.valdi.jmusicbot.utils.ChecksumUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AddTrackFile extends DJCommand  {
    private final String loadingEmoji;

    public AddTrackFile(Bot bot, String loadingEmoji) {
        super(bot);
        this.loadingEmoji = loadingEmoji;
        this.name = "file";
        this.help = "add a stream track to the database";
        this.beListening = false;
        this.bePlaying = false;
    }

    @Override
    public void doCommand(CommandEvent event) {
        if (event.getMessage().getAttachments().isEmpty()) {
            event.replyError("You need to attach a file!");
            return;
        }

        File thumbnail = null;
        if(event.getMessage().getAttachments().size() > 1) {
            try {
                Message.Attachment thumbAttach = event.getMessage().getAttachments().get(1);
                String extension = FilenameUtils.getExtension(thumbAttach.getFileName());
                String hash = ChecksumUtils.getChecksum(thumbAttach.getInputStream(), "SHA-256");
                thumbnail = new File(bot.getDataManager().getCacheFolder(event.getGuild()), hash + FilenameUtils.EXTENSION_SEPARATOR + extension);
                if(thumbnail.exists()) {
                    event.replyError("This thumbnail is already present in the database!");
                    return;
                }
                try(InputStream is = thumbAttach.getInputStream()) {
                    Files.copy(is, thumbnail.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException | NoSuchAlgorithmException e) {
                event.replyError("Error creating thumbnail file. Check console!");
                return;
            }
        }

        Message.Attachment fileAttach = event.getMessage().getAttachments().get(0);
        try {
            String hash = ChecksumUtils.getChecksum(fileAttach.getInputStream(), "SHA-256");
            File file = new File(bot.getDataManager().getStoreFolder(event.getGuild()), hash);
            if(file.exists()) {
                event.replyError("This file is already present in the database!");
                if(thumbnail != null) {
                    thumbnail.delete();
                }
                return;
            }
            try(InputStream is = fileAttach.getInputStream()) {
                Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            File finalThumbnail = thumbnail;
            event.reply(loadingEmoji + " Loading... `[" + fileAttach.getFileName() + "]`", m -> bot.getPlayerManager()
                    .loadItemOrdered(event.getGuild(), file.getPath(), new ResultHandler(m, event, file, finalThumbnail)));
        } catch (IOException | NoSuchAlgorithmException e) {
            event.replyError("Error creating file. Check console!");
            if(thumbnail != null) {
                thumbnail.delete();
            }
        }
    }

    private class ResultHandler implements AudioLoadResultHandler {
        private final Message m;
        private final CommandEvent event;
        private final File file;
        private final File thumbnail;

        private ResultHandler(Message m, CommandEvent event, File file, File thumbnail) {
            this.m = m;
            this.event = event;
            this.file = file;
            this.thumbnail = thumbnail;
        }

        @Override
        public void trackLoaded(AudioTrack audioTrack) {
            Track track = bot.getDataManager().getDatabase(event.getGuild()).newTrack(UUID.randomUUID().toString());
            track.setType(TrackType.FILE);
            //track.setFile(audioTrack.getInfo().uri);
            track.setPath(file.getName());
            track.setDuration(audioTrack.getDuration());

            if(thumbnail != null) {
                track.setThumbnail(thumbnail.getName());
            }
            track.setTitle(audioTrack.getInfo().title);
            track.setCreatedby(event.getAuthor().getIdLong());

            try {
                String argsNoLink = event.getArgs().trim();
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
                if(thumbnail != null) {
                    thumbnail.delete();
                }
                file.delete();
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
