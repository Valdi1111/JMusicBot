package com.jagrosh.jmusicbot;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.ShutdownEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.valdi.SuperApiX.common.databases.DatabaseException;
import org.valdi.jmusicbot.JMusicBot;
import org.valdi.jmusicbot.data.music.Playlist;

/**
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class JDAListener extends ListenerAdapter {
    private final Bot bot;

    public JDAListener(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void onReady(ReadyEvent event) {
        if (event.getJDA().getGuilds().isEmpty()) {
            bot.getLogger().warn("This bot is not on any guilds! Use the following link to add the bot to your guilds!");
            bot.getLogger().warn(event.getJDA().asBot().getInviteUrl(JMusicBot.RECOMMENDED_PERMS));
        }
        credit(event.getJDA());
        event.getJDA().getGuilds().forEach(guild -> {
            try {
                bot.getDataManager().loadData(guild);
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
            try {
                Optional<Playlist> playlist = bot.getDataManager().getSettings(guild).getDefaultPlaylist();
                Optional<VoiceChannel> voice = bot.getDataManager().getSettings(guild).getVoiceChannel();
                if (playlist.isPresent() && voice.isPresent() && bot.getPlayerManager().setUpHandler(guild).playFromDefault()) {
                    guild.getAudioManager().openAudioConnection(voice.get());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        bot.getMain().getVersionCheckSystem().getNewVersionAvailable().ifPresent(v -> {
            if(!bot.getConfig().isUpdateAlertsOwner()) {
                return;
            }

            bot.getThreadpool().scheduleWithFixedDelay(() -> {
                User owner = bot.getJDA().getUserById(bot.getConfig().getOwner());
                if (owner != null) {
                    String msg = String.format(JMusicBot.NEW_VERSION_AVAILABLE, bot.getMain().getVersion(), v.getVersion().getVersionString());
                    owner.openPrivateChannel().queue(pc -> pc.sendMessage(msg).queue());
                }
            }, 0, 24, TimeUnit.HOURS);
        });
    }

    @Override
    public void onGuildMessageDelete(GuildMessageDeleteEvent event) {
        bot.getNowplayingHandler().onMessageDelete(event.getGuild(), event.getMessageIdLong());
    }

    @Override
    public void onShutdown(ShutdownEvent event) {
        bot.getMain().stopAll();
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        credit(event.getJDA());
    }

    // make sure people aren't adding clones to dbots
    private void credit(JDA jda) {
        Guild dbots = jda.getGuildById(110373943822540800L);
        if (dbots == null)
            return;
        if (bot.getConfig().isDBots())
            return;
        jda.getTextChannelById(119222314964353025L)
                .sendMessage("This account is running JMusicBot. Please do not list bot clones on this server, <@" + bot.getConfig().getOwner() + ">.").complete();
        dbots.leave().queue();
    }
}
