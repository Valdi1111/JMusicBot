package com.jagrosh.jmusicbot.commands.admin;

import java.util.List;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import com.jagrosh.jmusicbot.commands.AdminCommand;
import com.jagrosh.jmusicbot.utils.FormatUtil;
import net.dv8tion.jda.core.entities.VoiceChannel;
import org.valdi.jmusicbot.data.Settings;

/**
 * @author John Grosh <john.a.grosh@gmail.com>
 */
public class SetvcCmd extends AdminCommand {
    public SetvcCmd() {
        this.name = "setvc";
        this.help = "sets the voice channel for playing music";
        this.arguments = "<channel|NONE>";
    }

    @Override
    protected void execute(CommandEvent event) {
        if (event.getArgs().isEmpty()) {
            event.reply(event.getClient().getError() + " Please include a voice channel or NONE");
            return;
        }
        Settings settings = event.getClient().getSettingsFor(event.getGuild());
        if (event.getArgs().equalsIgnoreCase("none")) {
            settings.setVoiceChannelId(0L);
            settings.save();
            event.reply(event.getClient().getSuccess() + " Music can now be played in any channel");
        } else {
            List<VoiceChannel> list = FinderUtil.findVoiceChannels(event.getArgs(), event.getGuild());
            if (list.isEmpty())
                event.reply(event.getClient().getWarning() + " No Voice Channels found matching \"" + event.getArgs() + "\"");
            else if (list.size() > 1)
                event.reply(event.getClient().getWarning() + FormatUtil.listOfVChannels(list, event.getArgs()));
            else {
                settings.setVoiceChannelId(list.get(0).getIdLong());
                settings.save();
                event.reply(event.getClient().getSuccess() + " Music can now only be played in **" + list.get(0).getName() + "**");
            }
        }
    }
}
