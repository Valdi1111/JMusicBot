package com.jagrosh.jmusicbot;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jmusicbot.audio.AudioHandler;
import com.jagrosh.jmusicbot.audio.NowplayingHandler;
import com.jagrosh.jmusicbot.audio.PlayerManager;

import java.util.Objects;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.valdi.jmusicbot.BotConfig;
import org.valdi.jmusicbot.JMusicBot;
import org.valdi.jmusicbot.data.DataManager;

/**
 * @author John Grosh <john.a.grosh@gmail.com>
 */
public class Bot {
    private final Logger logger = LoggerFactory.getLogger("MusicBot");

    private final JMusicBot main;
    private final EventWaiter waiter;
    private final ScheduledExecutorService threadpool;
    private final BotConfig config;
    private final PlayerManager players;
    private final NowplayingHandler nowplaying;

    private JDA jda;

    public Bot(JMusicBot main, EventWaiter waiter, BotConfig config) {
        this.main = main;
        this.waiter = waiter;
        this.config = config;
        this.threadpool = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder()
                .setNameFormat(main.getName() + " Bot Thread - %d").build());
        this.players = new PlayerManager(this);
        this.players.init();
        this.nowplaying = new NowplayingHandler(this);
        this.nowplaying.init();
    }

    public BotConfig getConfig() {
        return config;
    }

    public EventWaiter getWaiter() {
        return waiter;
    }

    public ScheduledExecutorService getThreadpool() {
        return threadpool;
    }

    public PlayerManager getPlayerManager() {
        return players;
    }

    public NowplayingHandler getNowplayingHandler() {
        return nowplaying;
    }

    public JDA getJDA() {
        return jda;
    }

    public void closeAudioConnection(long guildId) {
        Guild guild = jda.getGuildById(guildId);
        if (guild != null) {
            threadpool.submit(() -> guild.getAudioManager().closeAudioConnection());
        }
    }

    public void resetGame() {
        Game game = config.getGame() == null || config.getGame().getName().equalsIgnoreCase("none") ? null : config.getGame();
        if (!Objects.equals(jda.getPresence().getGame(), game))
            jda.getPresence().setGame(game);
    }

    public void disable() {
        if (jda.getStatus() != JDA.Status.SHUTTING_DOWN) {
            jda.getGuilds().forEach(g -> {
                g.getAudioManager().closeAudioConnection();
                AudioHandler ah = (AudioHandler) g.getAudioManager().getSendingHandler();
                if (ah != null) {
                    ah.stopAndClear();
                    ah.getPlayer().destroy();
                    nowplaying.updateTopic(g.getIdLong(), ah, true);
                }
            });
            jda.shutdown();
        }
        threadpool.shutdownNow();
    }

    public void setJDA(JDA jda) {
        this.jda = jda;
    }

    public Logger getLogger() {
        return logger;
    }

    public JMusicBot getMain() {
        return main;
    }

    public DataManager getDataManager() {
        return main.getDataManager();
    }
}
