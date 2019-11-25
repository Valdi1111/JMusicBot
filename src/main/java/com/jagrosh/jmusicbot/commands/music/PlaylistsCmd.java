package com.jagrosh.jmusicbot.commands.music;

import java.util.Collection;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.commands.MusicCommand;
import org.valdi.jmusicbot.data.music.Playlist;

/**
 * @author John Grosh <john.a.grosh@gmail.com>
 */
public class PlaylistsCmd extends MusicCommand {
    public PlaylistsCmd(Bot bot) {
        super(bot);
        this.name = "playlists";
        this.help = "shows the available playlists";
        this.aliases = new String[]{"pls"};
        this.guildOnly = true;
        this.beListening = false;
        this.beListening = false;
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
        for(Playlist p : playlists) {
            builder.append(++count).append(". Id: `").append(p.getId()).append("`").append(" Name: `").append(p.getName()).append("`\n");
        }
        builder.append("\nType `").append(event.getClient().getTextualPrefix()).append("play playlist <id>` to play a playlist");
        event.reply(builder.toString());
    }
}
