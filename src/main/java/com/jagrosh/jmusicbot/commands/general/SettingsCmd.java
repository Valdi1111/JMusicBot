package com.jagrosh.jmusicbot.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import org.valdi.jmusicbot.data.Settings;
import org.valdi.jmusicbot.data.music.Playlist;

import java.util.Optional;

/**
 * @author John Grosh <john.a.grosh@gmail.com>
 */
public class SettingsCmd extends Command {
    private final static String EMOJI = "\uD83C\uDFA7"; // 🎧
    private final Bot bot;

    public SettingsCmd(Bot bot) {
        this.bot = bot;
        this.name = "settings";
        this.help = "shows the bots settings";
        this.aliases = new String[]{"status"};
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        Settings s = event.getClient().getSettingsFor(event.getGuild());
        Optional<TextChannel> tchan = s.getTextChannel();
        Optional<VoiceChannel> vchan = s.getVoiceChannel();
        Optional<Role> role = s.getDjRole();
        Optional<Playlist> playlist = s.getDefaultPlaylist();

        MessageBuilder builder = new MessageBuilder();
        EmbedBuilder ebuilder = new EmbedBuilder()
                .setTitle(EMOJI + " **" + event.getSelfUser().getName() + "** settings:")
                .setColor(event.getSelfMember().getColor())
                .setDescription("Text Channel: " + tchan.map(c -> "**#" + c.getName() + "**").orElse("Any")
                        + "\nVoice Channel: " + vchan.map(c -> "**" + c.getName() + "**").orElse("Any")
                        + "\nDJ Role: " + role.map(r -> "**" + r.getName() + "**").orElse("None")
                        + "\nRepeat Mode: **" + (s.isRepeatMode() ? "On" : "Off") + "**"
                        + "\nDefault Playlist: " + playlist.map(p -> "**" + p.getName() + "**").orElse("None"))
                .setFooter(event.getJDA().getGuilds().size() + " servers | "
                        + event.getJDA().getGuilds().stream().filter(g -> g.getSelfMember().getVoiceState().inVoiceChannel()).count()
                        + " audio connections", null);
        event.getChannel().sendMessage(builder.setEmbed(ebuilder.build()).build()).queue();
    }

}
