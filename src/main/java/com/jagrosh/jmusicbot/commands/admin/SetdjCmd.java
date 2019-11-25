package com.jagrosh.jmusicbot.commands.admin;

import java.util.List;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import com.jagrosh.jmusicbot.commands.AdminCommand;
import com.jagrosh.jmusicbot.utils.FormatUtil;
import net.dv8tion.jda.core.entities.Role;
import org.valdi.jmusicbot.data.Settings;

/**
 * @author John Grosh <john.a.grosh@gmail.com>
 */
public class SetdjCmd extends AdminCommand {
    public SetdjCmd() {
        this.name = "setdj";
        this.help = "sets the DJ role for certain music commands";
        this.arguments = "<rolename|NONE>";
    }

    @Override
    protected void execute(CommandEvent event) {
        if (event.getArgs().isEmpty()) {
            event.reply(event.getClient().getError() + " Please include a role name or NONE");
            return;
        }
        Settings settings = event.getClient().getSettingsFor(event.getGuild());
        if (event.getArgs().equalsIgnoreCase("none")) {
            settings.setDjRoleId(0L);
            settings.save();
            event.reply(event.getClient().getSuccess() + " DJ role cleared; Only Admins can use the DJ commands.");
        } else {
            List<Role> list = FinderUtil.findRoles(event.getArgs(), event.getGuild());
            if (list.isEmpty())
                event.reply(event.getClient().getWarning() + " No Roles found matching \"" + event.getArgs() + "\"");
            else if (list.size() > 1)
                event.reply(event.getClient().getWarning() + FormatUtil.listOfRoles(list, event.getArgs()));
            else {
                settings.setDjRoleId(list.get(0).getIdLong());
                settings.save();
                event.reply(event.getClient().getSuccess() + " DJ commands can now be used by users with the **" + list.get(0).getName() + "** role.");
            }
        }
    }

}
