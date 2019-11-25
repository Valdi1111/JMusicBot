package com.jagrosh.jmusicbot.commands.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.audio.AudioHandler;
import com.jagrosh.jmusicbot.audio.QueuedTrack;
import com.jagrosh.jmusicbot.commands.MusicCommand;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import org.valdi.jmusicbot.data.Settings;
import org.valdi.jmusicbot.data.music.Track;
import org.valdi.jmusicbot.utils.TrackUtils;

import java.util.Optional;

/**
 * @author John Grosh <john.a.grosh@gmail.com>
 */
public class RemoveCmd extends MusicCommand {
    public RemoveCmd(Bot bot) {
        super(bot);
        this.name = "remove";
        this.help = "removes a song from the queue";
        this.arguments = "<position|ALL>";
        this.aliases = new String[]{"delete"};
        this.beListening = true;
        this.bePlaying = true;
    }

    @Override
    public void doCommand(CommandEvent event) {
        AudioHandler handler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
        if (handler.getQueue().isEmpty()) {
            event.replyError("There is nothing in the queue!");
            return;
        }
        if (event.getArgs().equalsIgnoreCase("all")) {
            int count = handler.getQueue().removeAll(event.getAuthor().getIdLong());
            if (count == 0)
                event.replyWarning("You don't have any songs in the queue!");
            else
                event.replySuccess("Successfully removed your " + count + " entries.");
            return;
        }
        int pos;
        try {
            pos = Integer.parseInt(event.getArgs());
        } catch (NumberFormatException e) {
            pos = 0;
        }
        if (pos < 1 || pos > handler.getQueue().size()) {
            event.replyError("Position must be a valid integer between 1 and " + handler.getQueue().size() + "!");
            return;
        }
        Settings settings = event.getClient().getSettingsFor(event.getGuild());
        boolean isDJ = event.getMember().hasPermission(Permission.MANAGE_SERVER);
        Optional<Role> role = settings.getDjRole();
        if (!isDJ && role.isPresent()) {
            isDJ = event.getMember().getRoles().contains(role.get());
        }
        QueuedTrack qt = handler.getQueue().get(pos - 1);
        String title = qt.getTrack().getInfo().title;
        Optional<Track> t = TrackUtils.getTrack(bot.getDataManager().getDatabase(event.getGuild()), qt.getTrack());
        if(t.isPresent()) {
            title = t.get().getTitle();
        }
        if (qt.getIdentifier() == event.getAuthor().getIdLong()) {
            handler.getQueue().remove(pos - 1);
            event.replySuccess("Removed **" + title + "** from the queue");
        } else if (isDJ) {
            handler.getQueue().remove(pos - 1);
            User u;
            try {
                u = event.getJDA().getUserById(qt.getIdentifier());
            } catch (Exception e) {
                u = null;
            }
            event.replySuccess("Removed **" + title
                    + "** from the queue (requested by " + (u == null ? "someone" : "**" + u.getName() + "**") + ")");
        } else {
            event.replyError("You cannot remove **" + title + "** because you didn't add it!");
        }
    }
}
