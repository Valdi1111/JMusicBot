package com.jagrosh.jmusicbot.commands;

import com.jagrosh.jmusicbot.Bot;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;
import org.valdi.jmusicbot.data.Settings;

import java.util.Optional;

/**
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public abstract class DJCommand extends MusicCommand {
    public DJCommand(Bot bot) {
        super(bot);
        this.category = new Category("DJ", event -> {
            if (event.getAuthor().getId().equals(event.getClient().getOwnerId()))
                return true;
            if (event.getGuild() == null)
                return true;
            if (event.getMember().hasPermission(Permission.MANAGE_SERVER))
                return true;
            Settings settings = event.getClient().getSettingsFor(event.getGuild());
            Optional<Role> dj = settings.getDjRole();
            return dj.isPresent() && (event.getMember().getRoles().contains(dj.get()) || dj.get().getIdLong() == event.getGuild().getIdLong());
        });
    }
}
