package com.jagrosh.jmusicbot.commands.dj;


import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.audio.AudioHandler;
import com.jagrosh.jmusicbot.audio.QueuedTrack;
import com.jagrosh.jmusicbot.commands.DJCommand;
import com.jagrosh.jmusicbot.queue.FairQueue;
import org.valdi.jmusicbot.data.music.Track;
import org.valdi.jmusicbot.utils.TrackUtils;

import java.util.Optional;

/**
 * Command that provides users the ability to move a track in the playlist.
 */
public class MoveTrackCmd extends DJCommand {

    public MoveTrackCmd(Bot bot) {
        super(bot);
        this.name = "movetrack";
        this.help = "move a track in the current queue to a different position";
        this.arguments = "<from> <to>";
        this.aliases = new String[]{"move"};
        this.bePlaying = true;
    }

    @Override
    public void doCommand(CommandEvent event) {
        int from;
        int to;

        String[] parts = event.getArgs().split("\\s+", 2);
        if (parts.length < 2) {
            event.replyError("Please include two valid indexes.");
            return;
        }

        try {
            // Validate the args
            from = Integer.parseInt(parts[0]);
            to = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            event.replyError("Please provide two valid indexes.");
            return;
        }

        if (from == to) {
            event.replyError("Can't move a track to the same position.");
            return;
        }

        // Validate that from and to are available
        AudioHandler handler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
        FairQueue<QueuedTrack> queue = handler.getQueue();
        if (isUnavailablePosition(queue, from)) {
            String reply = String.format("`%d` is not a valid position in the queue!", from);
            event.replyError(reply);
            return;
        }
        if (isUnavailablePosition(queue, to)) {
            String reply = String.format("`%d` is not a valid position in the queue!", to);
            event.replyError(reply);
            return;
        }

        // Move the track
        QueuedTrack track = queue.moveItem(from - 1, to - 1);
        String title = handler.getPlayer().getPlayingTrack().getInfo().title;
        Optional<Track> t = TrackUtils.getTrack(bot.getDataManager().getDatabase(event.getGuild()), handler.getPlayer().getPlayingTrack());
        if(t.isPresent()) {
            title = t.get().getTitle();
        }
        String reply = String.format("Moved **%s** from position `%d` to `%d`.", title, from, to);
        event.replySuccess(reply);
    }

    private static boolean isUnavailablePosition(FairQueue<QueuedTrack> queue, int position) {
        return (position < 1 || position > queue.size());
    }
}