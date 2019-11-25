package com.jagrosh.jmusicbot.commands.dj.addtrack;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.commands.DJCommand;
import com.jagrosh.jmusicbot.utils.FormatUtil;
import com.sapher.youtubedl.YoutubeDL;
import com.sapher.youtubedl.YoutubeDLRequest;
import com.sapher.youtubedl.YoutubeDLResponse;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Message;
import org.valdi.jmusicbot.data.music.Track;
import org.valdi.jmusicbot.data.music.TrackType;
import org.valdi.jmusicbot.utils.ChecksumUtils;
import org.valdi.jmusicbot.utils.YoutubeLinkUtils;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class AddTrackDownload extends DJCommand {
    private final String loadingEmoji;

    public AddTrackDownload(Bot bot, String loadingEmoji) {
        super(bot);
        this.loadingEmoji = loadingEmoji;
        this.name = "download";
        this.help = "add a track to the database downloading it from a link";
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

        if(!bot.getMain().isYtdlEnabled()) {
            event.replyError("You can't download from youtube links since youtube-dl isn't setup correctly.");
            return;
        }

        String finalLink = link;
        event.reply(loadingEmoji + " Downloading... `[" + finalLink + "]`", m -> bot.getPlayerManager().loadItemOrdered(event.getGuild(), finalLink, new ResultHandler(m, event)));
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
            if (!(audioTrack.getSourceManager() instanceof YoutubeAudioSourceManager)) {
                m.editMessage(event.getClient().getError() + " Invalid url source! Only youtube is currently supported!").queue();
                return;
            }

            for (Track track : bot.getDataManager().getDatabase(event.getGuild()).getTracks().values()) {
                if (track.getFile().equals(audioTrack.getInfo().uri)) {
                    m.editMessage(event.getClient().getError() + " A track with the same link is already present in the database! **"
                            + track.getTitle() + "** (`" + FormatUtil.formatTime(track.getDuration()) + "`)").queue();
                    return;
                }
            }

            File file;
            try {
                // Build request
                YoutubeDLRequest request = new YoutubeDLRequest(audioTrack.getInfo().uri, bot.getDataManager().getTempFolder().getPath());
                request.setOption("no-mark-watched");
                request.setOption("ignore-errors");
                request.setOption("no-playlist");
                request.setOption("extract-audio");
                request.setOption("audio-format",  "mp3");
                request.setOption("output", "%(id)s.%(ext)s");

                // Make request and return response
                YoutubeDLResponse response = YoutubeDL.execute(request);

                // Response
                String stdOut = response.getOut(); // Executable output
                //System.out.println(stdOut);

                File ytFile = new File(bot.getDataManager().getTempFolder().getPath(), audioTrack.getIdentifier() + ".mp3");
                file = downloadFile(ytFile);
                if(file == null) {
                    return;
                }
            } catch (Exception e) {
                event.replyError("Error while downloading file from the provided link. Check console for details.");
                e.printStackTrace();
                return;
            }

            File thumbnail = downloadThumbnail(YoutubeLinkUtils.getThumbnailUrl(audioTrack.getIdentifier()));

            Track track = bot.getDataManager().getDatabase(event.getGuild()).newTrack(UUID.randomUUID().toString());
            track.setType(TrackType.FILE);
            track.setFile(audioTrack.getInfo().uri);
            track.setPath(file.getName());
            track.setDuration(audioTrack.getDuration());

            if (thumbnail != null) {
                track.setThumbnail(thumbnail.getName());
            }
            track.setTitle(audioTrack.getInfo().title);
            track.setCreatedby(event.getAuthor().getIdLong());

            try {
                String argsNoLink = event.getArgs().replace(audioTrack.getInfo().uri, "");
                argsNoLink = argsNoLink.trim();

                List<String> args = new ArrayList<>(Arrays.asList(argsNoLink.split(",")));
                args.add(argsNoLink);

                for (String arg : args) {
                    arg = arg.trim();
                    if (!arg.contains("=")) {
                        continue;
                    }
                    String[] cmd = arg.split("=", 2);
                    switch (cmd[0]) {
                        case "title":
                            if (cmd.length == 1) {
                                continue;
                            }
                            track.setTitle(cmd[1]);
                            break;
                        case "artist":
                            if (cmd.length == 1) {
                                continue;
                            }
                            track.setArtist(cmd[1]);
                            break;
                        case "album":
                            if (cmd.length == 1) {
                                continue;
                            }
                            track.setAlbum(cmd[1]);
                            break;
                        case "track":
                            if (cmd.length == 1) {
                                continue;
                            }
                            track.setTrack(Integer.parseInt(cmd[1]));
                            break;
                        case "genre":
                            if (cmd.length == 1) {
                                continue;
                            }
                            track.setGenre(cmd[1]);
                            break;
                        case "volume":
                            if (cmd.length == 1) {
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
                if (thumbnail != null) {
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

        private File downloadFile(File ytFile) {
            try {
                String hash = ChecksumUtils.getFileChecksum(ytFile, "SHA-256");
                //System.out.println("File hash" + hash);
                File file = new File(bot.getDataManager().getStoreFolder(event.getGuild()), hash);
                if (file.exists()) {
                    event.replyError("This file is already present in the database!");
                    return null;
                }

                Files.copy(ytFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                return file;
            } catch (IOException | NoSuchAlgorithmException e) {
                event.replyError("Error while downloading file from the provided link. Check console for details.");
                e.printStackTrace();
                return null;
            } finally {
                ytFile.delete();
            }
        }

        private File downloadThumbnail(String urlRaw) {
            try {
                URL url = new URL(urlRaw);
                String hash = ChecksumUtils.getChecksum(url.openStream(), "SHA-256");
                File file = new File(bot.getDataManager().getCacheFolder(event.getGuild()), hash + ".jpg");
                if (file.exists()) {
                    event.replyError("This thumbnail is already present in the database!");
                    return null;
                }

                try (InputStream is = url.openStream()) {
                    Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
                return file;
            } catch (IOException | NoSuchAlgorithmException e) {
                event.replyError("Error while downloading thumbnail from the provided link. Check console for details.");
                e.printStackTrace();
                return null;
            }
        }
    }
}
