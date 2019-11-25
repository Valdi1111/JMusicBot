package org.valdi.jmusicbot;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.examples.command.*;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.JDAListener;
import com.jagrosh.jmusicbot.commands.admin.*;
import com.jagrosh.jmusicbot.commands.dj.*;
import com.jagrosh.jmusicbot.commands.general.*;
import com.jagrosh.jmusicbot.commands.music.*;
import com.jagrosh.jmusicbot.commands.owner.*;
import com.jagrosh.jmusicbot.utils.OtherUtil;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import javax.security.auth.login.LoginException;

import com.sapher.youtubedl.YoutubeDL;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Game;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.valdi.SuperApiX.common.config.FilesProvider;
import org.valdi.SuperApiX.common.config.IFilesProvider;
import org.valdi.SuperApiX.common.config.advanced.ConfigLoader;
import org.valdi.SuperApiX.common.config.serializers.SetSerializer;
import org.valdi.SuperApiX.common.databases.DatabaseException;
import org.valdi.SuperApiX.common.databases.DatabasesProvider;
import org.valdi.SuperApiX.common.databases.IDatabasesProvider;
import org.valdi.SuperApiX.common.dependencies.UniversalDependencyManager;
import org.valdi.SuperApiX.common.logging.CompatibilityLogger;
import org.valdi.SuperApiX.common.logging.SuperLogger;
import org.valdi.SuperApiX.common.plugin.StoreLoader;
import org.valdi.SuperApiX.common.scheduler.SimpleScheduler;
import org.valdi.SuperApiX.common.scheduler.SuperScheduler;
import org.valdi.jmusicbot.benchmarking.Timings;
import org.valdi.jmusicbot.data.DataManager;
import org.valdi.jmusicbot.delivery.formatting.Formatters;
import org.valdi.jmusicbot.delivery.webserver.WebServer;
import org.valdi.jmusicbot.delivery.webserver.WebServerSystem;
import org.valdi.jmusicbot.exceptions.EnableException;
import org.valdi.jmusicbot.file.FileResource;
import org.valdi.jmusicbot.file.JarResource;
import org.valdi.jmusicbot.file.Resource;
import org.valdi.jmusicbot.file.ResourceCache;
import org.valdi.jmusicbot.prompt.IPrompt;
import org.valdi.jmusicbot.prompt.TerminalPrompt;
import org.valdi.jmusicbot.settings.locale.Locale;
import org.valdi.jmusicbot.settings.locale.LocaleSystem;
import org.valdi.jmusicbot.settings.theme.Theme;
import org.valdi.jmusicbot.version.VersionCheckSystem;

/**
 * @author John Grosh (jagrosh)
 */
public class JMusicBot extends Thread implements StoreLoader {
    static {
        org.apache.logging.log4j.core.Logger logger = (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
        PatternLayout layout = PatternLayout.newBuilder().withPattern("[%d{HH:mm:ss}] [%t/%level] [%logger{36}]: %msg%n").build();
        TextAreaAppender appender = TextAreaAppender.createAppender("Gui", false, layout, null);
        appender.start();
        logger.getContext().getConfiguration().addLoggerAppender(logger, appender);
    }

    public final static String NEW_VERSION_AVAILABLE = "There is a new version of JMusicBot available!\n"
            + "Current version: %s\n"
            + "New Version: %s\n\n"
            + "Please visit https://github.com/jagrosh/MusicBot/releases/latest to get the latest release.";

    public final static String PLAY_EMOJI = "\u25B6"; // ▶
    public final static String PAUSE_EMOJI = "\u23F8"; // ⏸
    public final static String STOP_EMOJI = "\u23F9"; // ⏹
    public final static Permission[] RECOMMENDED_PERMS = new Permission[]{Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.MESSAGE_HISTORY, Permission.MESSAGE_ADD_REACTION,
            Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_MANAGE, Permission.MESSAGE_EXT_EMOJI,
            Permission.MANAGE_CHANNEL, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK, Permission.NICKNAME_CHANGE};

    private static org.apache.logging.log4j.Logger logger = LogManager.getLogger("Main");
    private static JMusicBot instance;
    private static String[] startArgs;

    private final ScheduledExecutorService executors;
    private final UniversalDependencyManager dependencyManager;
    private final DatabasesProvider databasesProvider;
    private final FilesProvider filesProvider;
    private final SuperLogger simpleLogger;
    private final SimpleScheduler scheduler;
    private final Timings timings;

    private boolean ytdlEnabled = false;
    private IPrompt prompt;
    private Bot bot;

    private boolean shuttingDown = false;

    private ConfigLoader<BotConfig> config;
    private Formatters formatters;
    private LocaleSystem localeSystem;
    private Theme themeSystem;
    private VersionCheckSystem versionCheckSystem;
    private WebServerSystem webServerSystem;
    private DataManager dataManager;

    public JMusicBot() {
        super("JMusicBot");

        this.simpleLogger = new CompatibilityLogger(logger);
        this.dependencyManager = UniversalDependencyManager.init(this);
        dependencyManager.loadDependencies(
                Dependencies.GOOGLE_GUAVA,
                Dependencies.GOOGLE_GUAVA_FAILUREACCESS,
                Dependencies.GOOGLE_GSON,
                Dependencies.COMMONS_LANG3,
                Dependencies.JAR_RELOCATOR,
                Dependencies.LOG4J_SLF4J_BINDING,
                Dependencies.CAFFEINE,
                Dependencies.HTML_COMPRESSOR,
                Dependencies.HTTP_REQUEST);
        dependencyManager.loadDependencies(
                Dependencies.YAML,
                Dependencies.HOCON_CONFIG,
                Dependencies.TOML4J,
                Dependencies.CONFIGURATE_CORE,
                Dependencies.CONFIGURATE_YAML,
                Dependencies.CONFIGURATE_GSON,
                Dependencies.CONFIGURATE_HOCON,
                Dependencies.CONFIGURATE_TOML);
        dependencyManager.loadDependencies(
                Dependencies.MONGODB_DRIVER,
                Dependencies.MARIADB_DRIVER,
                Dependencies.MYSQL_DRIVER,
                Dependencies.POSTGRESQL_DRIVER,
                Dependencies.SQLITE_DRIVER,
                Dependencies.H2_DRIVER,
                Dependencies.HIKARI);

        this.executors = Executors.newScheduledThreadPool(10, new ThreadFactoryBuilder().setNameFormat(this.getName() + " Thread - %d").build());
        this.databasesProvider = new DatabasesProvider();
        this.filesProvider = new FilesProvider();
        this.scheduler = new SimpleScheduler(this);

        this.timings = new Timings();

        start();
    }

    @Override
    public void run() {
        // Register common serializers
        new SetSerializer().register();

        SimpleScheduler.startAcceptingTasks();
        ScheduledExecutorService single = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat(this.getName() + " Thread").build());
        single.scheduleWithFixedDelay(new Runnable() {
            int alteredTicks = 0;

            @Override
            public void run() {
                ++alteredTicks;
                scheduler.tick(alteredTicks);
                //logger.info("Ticking scheduler... Ticks: " + alteredTicks);
            }
        }, 0L, 50L, TimeUnit.MILLISECONDS);

        this.preStartAll(startArgs);
    }

    public static void main(String[] args) {
        startArgs = args;
        instance = new JMusicBot();
    }

    public static JMusicBot getInstance() {
        return instance;
    }

    public URL getResourceUrl(String url) {
        return getJarLoader().getResource(url);
    }

    @Override
    public InputStream getResource(String filename) {
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be null or empty!");
        }

        try {
            URL url = getJarLoader().getResource(filename);

            if (url == null) {
                return null;
            }

            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    public void saveResource(String resourcePath, boolean replace) {
        if (resourcePath == null || resourcePath.equals("")) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }

        resourcePath = resourcePath.replace('\\', '/');
        try (InputStream in = getResource(resourcePath)) {
            if (in == null) {
                throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in " + getJarFile());
            }

            File outFile = new File(getDataFolder(), resourcePath);
            // Make any dirs that need to be made
            outFile.getParentFile().mkdirs();
            if (!outFile.exists() || replace) {
                Files.copy(in, outFile.toPath());
            }
        } catch (IOException e) {
            logger.error("Could not save from jar file. " + resourcePath, e);
        }
    }

    @Override
    public ThreadFactory getThreadFactory() {
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) this.getExecutorService();
        return threadPoolExecutor.getThreadFactory();
    }

    @Override
    public ScheduledExecutorService getExecutorService() {
        return this.executors;
    }

    @Override
    public UniversalDependencyManager getDependencyManager() {
        return dependencyManager;
    }

    @Override
    public Optional<IDatabasesProvider> getDatabasesProvider() {
        return Optional.of(databasesProvider);
    }

    @Override
    public Optional<IFilesProvider> getFilesProvider() {
        return Optional.of(filesProvider);
    }

    @Override
    public String getVersion() {
        return this.getVersionCheckSystem().getCurrentVersion();
    }

    @Override
    public List<String> getAuthors() {
        return Collections.singletonList("Valdi_1111");
    }

    @Override
    public SuperLogger getLogger() {
        return simpleLogger;
    }

    @Override
    public SuperScheduler getScheduler() {
        return scheduler;
    }

    public Timings getTimings() {
        return timings;
    }

    @Override
    public File getDataFolder() {
        return getJarFile().getParentFile();
    }

    @Override
    public File getJarFile() {
        try {
            return new File(JMusicBot.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ClassLoader getJarLoader() {
        return JMusicBot.class.getClassLoader();
    }

    public File getFileFromPluginFolder(String name) {
        return new File(getDataFolder(), name.replace("/", File.separator));
    }

    /**
     * Get a file in the jar as a {@link Resource}.
     *
     * @param resourceName Path to the file inside jar/assets/plan/ folder.
     * @return a {@link Resource} for accessing the resource.
     */
    public Resource getResourceFromJar(String resourceName) {
        return new JarResource(resourceName, () -> getResource(resourceName));
    }

    /**
     * Get a file from plugin folder as a {@link Resource}.
     *
     * @param resourceName Path to the file inside the plugin folder.
     * @return a {@link Resource} for accessing the resource.
     */
    public Resource getResourceFromPluginFolder(String resourceName) {
        return new FileResource(resourceName, getFileFromPluginFolder(resourceName));
    }

    /**
     * Get a customizable resource from the plugin files or from the jar if one doesn't exist.
     *
     * @param resourceName Path to the file inside the plugin folder.
     * @return a {@link Resource} for accessing the resource, either from the plugin folder or jar.
     */
    public Resource getCustomizableResourceOrDefault(String resourceName) {
        return ResourceCache.getOrCache(resourceName, () ->
                attemptToFind(resourceName).map(file -> (Resource) new FileResource(resourceName, file))
                        .orElse(getResourceFromJar(resourceName))
        );
    }

    private Optional<File> attemptToFind(String resourceName) {
        if (getDataFolder().exists() && getDataFolder().isDirectory()) {

            String[] path = StringUtils.split(resourceName, '/');

            Path toFile = getDataFolder().getAbsoluteFile().toPath().toAbsolutePath();
            for (String next : path) {
                toFile = toFile.resolve(next);
            }

            File found = toFile.toFile();
            if (found.exists()) {
                return Optional.of(found);
            }
        }
        return Optional.empty();
    }

    public void preStartAll(String[] args) {
        // Startup log
        Logger log = LoggerFactory.getLogger("Startup");

        if (Boolean.getBoolean("nogui")) {
            setPrompt(new TerminalPrompt("JMusicBot"));
            startAll();
            return;
        }

        try {
            Gui.main(this, args);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("Could not start GUI. If you are running on a server or in a location where you " +
                    "cannot display a window, please run in nogui mode using the -Dnogui=true flag.", e);
        }
    }

    public void startAll() {
        // Startup log
        Logger log = LoggerFactory.getLogger("Startup");

        this.config = new ConfigLoader<>(this, BotConfig.class);
        config.loadAnnotated();

        this.formatters = new Formatters(this);

        this.localeSystem = new LocaleSystem(this);
        try {
            localeSystem.enable();
        } catch (EnableException e) {
            logger.error("Error on Locales system enabling...", e);
        }

        this.themeSystem = new Theme(this);
        try {
            themeSystem.enable();
        } catch (EnableException e) {
            logger.error("Error on Themes system enabling...", e);
        }

        try {
            this.dataManager = new DataManager(this);
        } catch (DatabaseException e) {
            logger.error("Error on Database system enabling...", e);
        }

        boolean updateConfig = false;

        // Validate bot token
        String token = getConfig().getToken();
        if (token == null || token.isEmpty() || token.equalsIgnoreCase("BOT_TOKEN_HERE")) {
            token = prompt.prompt("Invalid Token!",
                    "Please provide the Token of the bot."
                            + "\nInstructions for obtaining a token can be found here:"
                            + "\nhttps://github.com/jagrosh/MusicBot/wiki/Getting-a-Bot-Token.");
            getConfig().setToken(token);
            if (token == null) {
                prompt.alert(IPrompt.Level.WARNING, "Error!",
                        "JMusicBot config", "No Token provided! Exiting.\n" +
                                "\nConfig Location: " + config.getFileStorage().getFile());
                return;
            } else {
                updateConfig = true;
            }
        }

        // Validate client id
        long id = getConfig().getClientId();
        if (id <= 0) {
            try {
                id = Long.parseLong(prompt.prompt("Invalid Client ID!",
                        "Please provide the Client ID of the bot."
                                + "\nInstructions for obtaining the bot's Client ID can be found here:"
                                + "\nhttps://github.com/jagrosh/MusicBot/wiki/Finding-Your-User-ID"));
                getConfig().setClientId(id);
            } catch (NumberFormatException | NullPointerException ex) {
                id = 0;
            }
            if (id <= 0) {
                prompt.alert(IPrompt.Level.ERROR, "Error!",
                        "JMusicBot config", "No Client ID provided! Exiting.\n" +
                                "\nConfig Location: " + config.getFileStorage().getFile());
                System.exit(0);
            } else {
                updateConfig = true;
            }
        }

        // Validate client secret
        String secret = getConfig().getClientSecret();
        if (secret == null || secret.isEmpty() || secret.equalsIgnoreCase("BOT_TOKEN_HERE")) {
            secret = prompt.prompt("Invalid Client Secret!",
                    "Please provide the Client Secret of the bot."
                            + "\nInstructions for obtaining the bot's Client Secret can be found here:"
                            + "\nhttps://github.com/jagrosh/MusicBot/wiki/Getting-a-Bot-Token.");
            getConfig().setClientSecret(secret);
            if (secret == null) {
                prompt.alert(IPrompt.Level.WARNING, "Error!",
                        "JMusicBot config", "No Client Secret provided! Exiting.\n" +
                                "\nConfig Location: " + config.getFileStorage().getFile());
                return;
            } else {
                updateConfig = true;
            }
        }

        // Validate bot owner
        long owner = getConfig().getOwner();
        if (owner <= 0) {
            try {
                owner = Long.parseLong(prompt.prompt("Invalid Owner ID!",
                        "Please provide the User ID of the bot's owner."
                                + "\nInstructions for obtaining your User ID can be found here:"
                                + "\nhttps://github.com/jagrosh/MusicBot/wiki/Finding-Your-User-ID"));
                getConfig().setOwner(owner);
            } catch (NumberFormatException | NullPointerException ex) {
                owner = 0;
            }
            if (owner <= 0) {
                prompt.alert(IPrompt.Level.ERROR, "Error!",
                        "JMusicBot config", "No Owner ID provided! Exiting.\n" +
                                "\nConfig Location: " + config.getFileStorage().getFile());
                System.exit(0);
            } else {
                updateConfig = true;
            }
        }

        config.save();
        /*if (updateConfig) {
            config.save();
        }*/

        // Setup youtube-dl
        File ytdlFile = new File(getConfig().getYoutubeDLPath().replace("%datafolder%", this.getDataFolder().getPath()));
        if (ytdlFile.exists()) {
            YoutubeDL.setExecutablePath(ytdlFile.getPath());
            ytdlEnabled = true;
        } else {
            logger.error("Youtube-dl not found at the provided path ({})!", ytdlFile.getPath());
            logger.error("Downloading tracks from youtube will not be possible!");
            logger.error("You can enable youtube-dl setting up 'youtube-dl-path' in config.yml");
        }

        // Get and check latest version
        String version = OtherUtil.getCurrentVersion();
        this.versionCheckSystem = new VersionCheckSystem(this, version);
        versionCheckSystem.enable();
        versionCheckSystem.getNewVersionAvailable().ifPresent(v ->
                prompt.alert(IPrompt.Level.WARNING, "New version available!", "Version",
                        String.format(NEW_VERSION_AVAILABLE, version, v.getVersion().getVersionString())));

        // Set up the listener
        EventWaiter waiter = new EventWaiter();
        try {
            dataManager.loadSettings();
        } catch (DatabaseException e) {
            logger.error("Error loading settings from Global Database...", e);
        }

        this.bot = new Bot(this, waiter, getConfig());

        AboutCommand aboutCommand = new AboutCommand(Color.BLUE.brighter(),
                "a music bot that is [easy to host yourself!](https://github.com/jagrosh/MusicBot) (v" + version + ")",
                new String[]{"High-quality music playback", "FairQueue™ Technology", "Easy to host yourself"},
                RECOMMENDED_PERMS);
        aboutCommand.setIsAuthor(false);
        aboutCommand.setReplacementCharacter("\uD83C\uDFB6"); // 🎶

        // set up the command client
        CommandClientBuilder cb = new CommandClientBuilder()
                .setPrefix(getConfig().getPrefix())
                .setAlternativePrefix(getConfig().getAltPrefix())
                .setOwnerId(Long.toString(getConfig().getOwner()))
                .setEmojis(getConfig().getEmojiSuccess(), getConfig().getEmojiWarning(), getConfig().getEmojiError())
                .setHelpWord(getConfig().getHelp())
                .setLinkedCacheSize(200)
                .setGuildSettingsManager(dataManager)
                .addCommands(aboutCommand,
                        new PingCommand(),
                        new SettingsCmd(bot),

                        new LyricsCmd(bot),
                        new NowplayingCmd(bot),
                        new PlayCmd(bot, getConfig().getEmojiLoading()),
                        new PlaylistsCmd(bot),
                        new QueueCmd(bot),
                        new RemoveCmd(bot),
                        new SearchCmd(bot, getConfig().getEmojiSearching()),
                        new SCSearchCmd(bot, getConfig().getEmojiSearching()),
                        new ShuffleCmd(bot),
                        new SkipCmd(bot),

                        new AddTrackCmd(bot, getConfig().getEmojiLoading()),
                        new ForceRemoveCmd(bot),
                        new ForceskipCmd(bot),
                        new MoveTrackCmd(bot),
                        new PauseCmd(bot),
                        new PlaylistCmd(bot),
                        new PlaynextCmd(bot, getConfig().getEmojiLoading()),
                        new RepeatCmd(bot),
                        new SkiptoCmd(bot),
                        new StopCmd(bot),
                        new VolumeCmd(bot),

                        new SetdjCmd(),
                        new SettcCmd(),
                        new SetvcCmd(),

                        new SetavatarCmd(),
                        new SetgameCmd(),
                        new SetnameCmd(),
                        new SetstatusCmd(),
                        new ShutdownCmd(bot)
                );

        if (getConfig().isEval()) {
            cb.addCommand(new EvalCmd(bot));
        }

        boolean nogame = false;
        if (getConfig().getStatus() != OnlineStatus.UNKNOWN) {
            cb.setStatus(getConfig().getStatus());
        }

        if (getConfig().getGame() == null) {
            cb.useDefaultGame();
        } else if (getConfig().getGame().getName().equalsIgnoreCase("none")) {
            cb.setGame(null);
            nogame = true;
        } else {
            cb.setGame(getConfig().getGame());
        }
        CommandClient client = cb.build();

        log.info("Loaded config from " + config.getFileStorage().getFile());

        //new HttpServer(this).start();
        this.webServerSystem = new WebServerSystem(this);
        try {
            webServerSystem.enable();
        } catch (EnableException e) {
            logger.error("Error on WebServer system enabling...", e);
        }

        // Attempt to log in and start
        try {
            JDA jda = new JDABuilder(AccountType.BOT)
                    .setToken(getConfig().getToken())
                    .setAudioEnabled(true)
                    .setGame(nogame ? null : Game.playing("loading..."))
                    .setStatus(getConfig().getStatus() == OnlineStatus.INVISIBLE || getConfig().getStatus() == OnlineStatus.OFFLINE ? OnlineStatus.INVISIBLE : OnlineStatus.DO_NOT_DISTURB)
                    .addEventListener(client, waiter, new JDAListener(bot))
                    .setBulkDeleteSplittingEnabled(true)
                    .build();
            bot.setJDA(jda);
        } catch (LoginException ex) {
            prompt.alert(IPrompt.Level.ERROR, "Fatal error!", "JMusicBot", ex + "\nPlease make sure you are "
                    + "editing the correct config.txt file, and that you have used the "
                    + "correct token (not the 'secret'!)\nConfig Location: " + config.getFileStorage().getFile());
            System.exit(1);
        } catch (IllegalArgumentException ex) {
            prompt.alert(IPrompt.Level.ERROR, "Fatal error!", "JMusicBot", "Some aspect of the configuration is "
                    + "invalid: " + ex + "\nConfig Location: " + config.getFileStorage().getFile());
            System.exit(1);
        }
    }

    public void preStopAll() {
        if (shuttingDown) {
            return;
        }

        scheduler.shutdown();
        shuttingDown = true;

        bot.disable();
        localeSystem.disable();
        themeSystem.disable();
        versionCheckSystem.disable();
        webServerSystem.disable();
    }

    public void stopAll() {
        preStopAll();
        System.exit(0);
    }

    public BotConfig getConfig() {
        return config.getConfig();
    }

    public Formatters getFormatters() {
        return formatters;
    }

    public boolean isYtdlEnabled() {
        return ytdlEnabled;
    }

    public IPrompt getPrompt() {
        return prompt;
    }

    public void setPrompt(IPrompt prompt) {
        this.prompt = prompt;
    }

    public Bot getBot() {
        return bot;
    }

    public Locale getLocale() {
        return localeSystem.getLocale();
    }

    public Theme getTheme() {
        return themeSystem;
    }

    public VersionCheckSystem getVersionCheckSystem() {
        return versionCheckSystem;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public WebServer getWebServer() {
        return webServerSystem.getWebServer();
    }
}
