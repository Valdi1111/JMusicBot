package org.valdi.jmusicbot;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import org.valdi.SuperApiX.common.config.advanced.ConfigComment;
import org.valdi.SuperApiX.common.config.advanced.ConfigEntry;
import org.valdi.SuperApiX.common.config.advanced.FileType;
import org.valdi.SuperApiX.common.config.advanced.StoreTo;
import org.valdi.SuperApiX.common.config.advanced.adapters.Adapter;
import org.valdi.SuperApiX.common.config.types.ConfigType;
import org.valdi.jmusicbot.settings.locale.LangCode;

@ConfigComment("##########################################################")
@ConfigComment("#  Config for the JMusicBot                              #")
@ConfigComment("##########################################################")
@ConfigComment("#  Any line starting with # is ignored                   #")
@ConfigComment("#  You MUST set the token and owner                      #")
@ConfigComment("#  All other items have defaults if you don't set them   #")
@ConfigComment("#  Open in Notepad++ for best results                    #")
@ConfigComment("##########################################################")
@StoreTo(filename = "config.yml")
@FileType(ConfigType.YAML)
public class BotConfig {

    @ConfigComment("This sets the token for the bot to log in with")
    @ConfigComment("This MUST be a bot token (user tokens will not work)")
    @ConfigComment("If you don't know how to get a bot token, please see the guide here:")
    @ConfigComment("https://github.com/jagrosh/MusicBot/wiki/Getting-a-Bot-Token")
    @ConfigEntry(path = "token")
    private String token = "BOT_TOKEN_HERE";

    @ConfigEntry(path = "client-id")
    private long clientId = 0L;

    @ConfigEntry(path = "client-secret")
    private String clientSecret = "CLIENT_SECRET_HERE";

    @ConfigComment("This sets the owner of the bot")
    @ConfigComment("This needs to be the owner's ID (a 17-18 digit number)")
    @ConfigComment("https://github.com/jagrosh/MusicBot/wiki/Finding-Your-User-ID")
    @ConfigEntry(path = "owner")
    private long owner = 0L;

    @ConfigEntry(path = "youtube-dl-path")
    private String youtubeDLPath = "%datafolder%/youtube-dl";

    @ConfigEntry(path = "prefix")
    private String prefix = "@mention";

    @Adapter(GameConfigAdapter.class)
    @ConfigEntry(path = "game")
    private Game game = null;

    @ConfigEntry(path = "status")
    private OnlineStatus status = OnlineStatus.ONLINE;

    @ConfigEntry(path = "song-in-status")
    private boolean songInStatus = false;

    @ConfigEntry(path = "alt-prefix")
    private String altPrefix = "m!";

    @ConfigEntry(path = "emojis.success")
    private String emojiSuccess = "\uD83C\uDFB6";

    @ConfigEntry(path = "emojis.warning")
    private String emojiWarning = "\uD83D\uDCA1";

    @ConfigEntry(path = "emojis.error")
    private String emojiError = "\uD83D\uDEAB";

    @ConfigEntry(path = "emojis.loading")
    private String emojiLoading = "⌚";

    @ConfigEntry(path = "emojis.searching")
    private String emojiSearching = "\uD83D\uDD0E";

    @ConfigEntry(path = "help")
    private String help = "help";

    @ConfigEntry(path = "now-playing-images")
    private boolean nowPlayingImages = true;

    @ConfigEntry(path = "stay-in-channel")
    private boolean stayInChannel = false;

    @ConfigEntry(path = "max-time")
    private int maxTime = 0;

    @ConfigComment("Enable or disable update checking")
    @ConfigEntry(path = "update.check")
    private boolean updateCheck = true;

    @ConfigComment("Display update notification on the website")
    @ConfigEntry(path = "update.alerts-owner")
    private boolean updateAlertsOwner = true;

    @ConfigComment("Display update notification on the website")
    @ConfigEntry(path = "update.alerts-web")
    private boolean updateAlertsWeb = true;

    @ConfigComment("Also notify about dev versions")
    @ConfigEntry(path = "update.dev-versions")
    private boolean updateDevVersions = true;

    @ConfigEntry(path = "lyrics.default")
    private String lyricsDefault = "A-Z Lyrics";

    @ConfigEntry(path = "eval")
    private boolean eval = false;

    @ConfigEntry(path = "webserver.active")
    private boolean webServerActive = true;

    @ConfigEntry(path = "webserver.port")
    private int webServerPort = 12125;

    @ConfigComment("InternalIP usually does not need to be changed, only change it if you know what you're doing!")
    @ConfigComment("0.0.0.0 allocates Internal (local) IP automatically for the WebServer.")
    @ConfigEntry(path = "webserver.internal-ip")
    private String webServerInternalIp = "0.0.0.0";

    @ConfigComment("%port% is replaced automatically with webserver.port")
    @ConfigEntry(path = "webserver.external-ip")
    private String webServerExternalIp = "your.domain.here:%port%";

    @ConfigEntry(path = "webserver.external-address")
    private String webServerExternalAddress = "https://www.example.address";

    @ConfigEntry(path = "ssl.keystore-path")
    private String webServerSslPath = "Cert.jks";

    @ConfigEntry(path = "ssl.key-pass")
    private String webServerSslKeyPass = "default";

    @ConfigEntry(path = "ssl.store-pass")
    private String webServerSslStorePass = "default";

    @ConfigEntry(path = "ssl.alias")
    private String webServerSslAlias = "alias";

    @ConfigEntry(path = "locale")
    private LangCode locale = LangCode.EN;

    @ConfigEntry(path = "theme")
    private String theme = "default";

    @ConfigComment("UTC used if false. Only affects Timestamps and graphs.")
    @ConfigEntry(path = "use-server-timezone")
    private boolean useServerTimezone = true;

    @ConfigEntry(path = "formatting.decimal-points")
    private String formattingDecimals = "#.##";

    @ConfigEntry(path = "formatting.time-amount.year")
    private String formattingTimeYear = "1 year, ";

    @ConfigEntry(path = "formatting.time-amount.years")
    private String formattingTimeYears = "%years% years, ";

    @ConfigEntry(path = "formatting.time-amount.month")
    private String formattingTimeMonth = "1 month, ";

    @ConfigEntry(path = "formatting.time-amount.months")
    private String formattingTimeMonths = "%months% months, ";

    @ConfigEntry(path = "formatting.time-amount.day")
    private String formattingTimeDay = "1d ";

    @ConfigEntry(path = "formatting.time-amount.days")
    private String formattingTimeDays = "%days%d ";

    @ConfigEntry(path = "formatting.time-amount.hours")
    private String formattingTimeHours = "%hours%:";

    @ConfigEntry(path = "formatting.time-amount.minutes")
    private String formattingTimeMinutes = "%minutes%:";

    @ConfigEntry(path = "formatting.time-amount.seconds")
    private String formattingTimeSeconds = "%seconds%";

    @ConfigEntry(path = "formatting.time-amount.zero")
    private String formattingTimeZero = "0s";

    @ConfigComment("Dates settings use Java SimpleDateFormat.")
    @ConfigComment("You can find the patterns & examples here:")
    @ConfigComment("https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html")
    @ConfigEntry(path = "formatting.dates.full")
    private String formattingDatesFull = "MMM d YYYY, HH:mm:ss";

    @ConfigEntry(path = "formatting.dates.no-seconds")
    private String formattingDatesNoSeconds = "MMM d YYYY, HH:mm";

    @ConfigEntry(path = "formatting.dates.just-clock")
    private String formattingDatesJustClock = "HH:mm:ss";

    @ConfigComment("Replaces day number with Today, Yesterday, Wednesday etc.")
    @ConfigEntry(path = "formatting.dates.show-day-names.active")
    private boolean formattingDatesDayNames = true;

    @ConfigComment("Non-regex pattern to replace.")
    @ConfigEntry(path = "formatting.dates.show-day-names.pattern")
    private String formattingDatesDayNamesPattern = "MMM d YYYY";

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public long getOwner() {
        return owner;
    }

    public void setOwner(long owner) {
        this.owner = owner;
    }

    public String getYoutubeDLPath() {
        return youtubeDLPath;
    }

    public void setYoutubeDLPath(String youtubeDLPath) {
        this.youtubeDLPath = youtubeDLPath;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public OnlineStatus getStatus() {
        return status;
    }

    public void setStatus(OnlineStatus status) {
        this.status = status;
    }

    public boolean isSongInStatus() {
        return songInStatus;
    }

    public void setSongInStatus(boolean songInStatus) {
        this.songInStatus = songInStatus;
    }

    public String getAltPrefix() {
        return altPrefix;
    }

    public void setAltPrefix(String altPrefix) {
        this.altPrefix = altPrefix;
    }

    public String getEmojiSuccess() {
        return emojiSuccess;
    }

    public void setEmojiSuccess(String emojiSuccess) {
        this.emojiSuccess = emojiSuccess;
    }

    public String getEmojiWarning() {
        return emojiWarning;
    }

    public void setEmojiWarning(String emojiWarning) {
        this.emojiWarning = emojiWarning;
    }

    public String getEmojiError() {
        return emojiError;
    }

    public void setEmojiError(String emojiError) {
        this.emojiError = emojiError;
    }

    public String getEmojiLoading() {
        return emojiLoading;
    }

    public void setEmojiLoading(String emojiLoading) {
        this.emojiLoading = emojiLoading;
    }

    public String getEmojiSearching() {
        return emojiSearching;
    }

    public void setEmojiSearching(String emojiSearching) {
        this.emojiSearching = emojiSearching;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public boolean isNowPlayingImages() {
        return nowPlayingImages;
    }

    public void setNowPlayingImages(boolean nowPlayingImages) {
        this.nowPlayingImages = nowPlayingImages;
    }

    public boolean isStayInChannel() {
        return stayInChannel;
    }

    public void setStayInChannel(boolean stayInChannel) {
        this.stayInChannel = stayInChannel;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

    public boolean isUpdateCheck() {
        return updateCheck;
    }

    public void setUpdateCheck(boolean updateCheck) {
        this.updateCheck = updateCheck;
    }

    public boolean isUpdateAlertsOwner() {
        return updateAlertsOwner;
    }

    public void setUpdateAlertsOwner(boolean updateAlertsOwner) {
        this.updateAlertsOwner = updateAlertsOwner;
    }

    public boolean isUpdateAlertsWeb() {
        return updateAlertsWeb;
    }

    public void setUpdateAlertsWeb(boolean updateAlertsWeb) {
        this.updateAlertsWeb = updateAlertsWeb;
    }

    public boolean isUpdateDevVersions() {
        return updateDevVersions;
    }

    public void setUpdateDevVersions(boolean updateDevVersions) {
        this.updateDevVersions = updateDevVersions;
    }

    public String getLyricsDefault() {
        return lyricsDefault;
    }

    public void setLyricsDefault(String lyricsDefault) {
        this.lyricsDefault = lyricsDefault;
    }

    public boolean isEval() {
        return eval;
    }

    public void setEval(boolean eval) {
        this.eval = eval;
    }

    public boolean isTooLong(AudioTrack track) {
        if (maxTime <= 0)
            return false;
        return Math.round(track.getDuration() / 1000.0) > maxTime;
    }

    public boolean isDBots() {
        return owner == 113156185389092864L;
    }

    public boolean isWebServerActive() {
        return webServerActive;
    }

    public void setWebServerActive(boolean webServerActive) {
        this.webServerActive = webServerActive;
    }

    public int getWebServerPort() {
        return webServerPort;
    }

    public void setWebServerPort(int webServerPort) {
        this.webServerPort = webServerPort;
    }

    public String getWebServerInternalIp() {
        return webServerInternalIp;
    }

    public void setWebServerInternalIp(String webServerInternalIp) {
        this.webServerInternalIp = webServerInternalIp;
    }

    public String getWebServerExternalIp() {
        return webServerExternalIp;
    }

    public void setWebServerExternalIp(String webServerExternalIp) {
        this.webServerExternalIp = webServerExternalIp;
    }

    public String getWebServerExternalAddress() {
        return webServerExternalAddress;
    }

    public void setWebServerExternalAddress(String webServerExternalAddress) {
        this.webServerExternalAddress = webServerExternalAddress;
    }

    public String getWebServerSslPath() {
        return webServerSslPath;
    }

    public void setWebServerSslPath(String webServerSslPath) {
        this.webServerSslPath = webServerSslPath;
    }

    public String getWebServerSslKeyPass() {
        return webServerSslKeyPass;
    }

    public void setWebServerSslKeyPass(String webServerSslKeyPass) {
        this.webServerSslKeyPass = webServerSslKeyPass;
    }

    public String getWebServerSslStorePass() {
        return webServerSslStorePass;
    }

    public void setWebServerSslStorePass(String webServerSslStorePass) {
        this.webServerSslStorePass = webServerSslStorePass;
    }

    public String getWebServerSslAlias() {
        return webServerSslAlias;
    }

    public void setWebServerSslAlias(String webServerSslAlias) {
        this.webServerSslAlias = webServerSslAlias;
    }

    public LangCode getLocale() {
        return locale;
    }

    public void setLocale(LangCode locale) {
        this.locale = locale;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public boolean isUseServerTimezone() {
        return useServerTimezone;
    }

    public void setUseServerTimezone(boolean useServerTimezone) {
        this.useServerTimezone = useServerTimezone;
    }

    public String getFormattingDecimals() {
        return formattingDecimals;
    }

    public void setFormattingDecimals(String formattingDecimals) {
        this.formattingDecimals = formattingDecimals;
    }

    public String getFormattingTimeYear() {
        return formattingTimeYear;
    }

    public void setFormattingTimeYear(String formattingTimeYear) {
        this.formattingTimeYear = formattingTimeYear;
    }

    public String getFormattingTimeYears() {
        return formattingTimeYears;
    }

    public void setFormattingTimeYears(String formattingTimeYears) {
        this.formattingTimeYears = formattingTimeYears;
    }

    public String getFormattingTimeMonth() {
        return formattingTimeMonth;
    }

    public void setFormattingTimeMonth(String formattingTimeMonth) {
        this.formattingTimeMonth = formattingTimeMonth;
    }

    public String getFormattingTimeMonths() {
        return formattingTimeMonths;
    }

    public void setFormattingTimeMonths(String formattingTimeMonths) {
        this.formattingTimeMonths = formattingTimeMonths;
    }

    public String getFormattingTimeDay() {
        return formattingTimeDay;
    }

    public void setFormattingTimeDay(String formattingTimeDay) {
        this.formattingTimeDay = formattingTimeDay;
    }

    public String getFormattingTimeDays() {
        return formattingTimeDays;
    }

    public void setFormattingTimeDays(String formattingTimeDays) {
        this.formattingTimeDays = formattingTimeDays;
    }

    public String getFormattingTimeHours() {
        return formattingTimeHours;
    }

    public void setFormattingTimeHours(String formattingTimeHours) {
        this.formattingTimeHours = formattingTimeHours;
    }

    public String getFormattingTimeMinutes() {
        return formattingTimeMinutes;
    }

    public void setFormattingTimeMinutes(String formattingTimeMinutes) {
        this.formattingTimeMinutes = formattingTimeMinutes;
    }

    public String getFormattingTimeSeconds() {
        return formattingTimeSeconds;
    }

    public void setFormattingTimeSeconds(String formattingTimeSeconds) {
        this.formattingTimeSeconds = formattingTimeSeconds;
    }

    public String getFormattingTimeZero() {
        return formattingTimeZero;
    }

    public void setFormattingTimeZero(String formattingTimeZero) {
        this.formattingTimeZero = formattingTimeZero;
    }

    public String getFormattingDatesFull() {
        return formattingDatesFull;
    }

    public void setFormattingDatesFull(String formattingDatesFull) {
        this.formattingDatesFull = formattingDatesFull;
    }

    public String getFormattingDatesNoSeconds() {
        return formattingDatesNoSeconds;
    }

    public void setFormattingDatesNoSeconds(String formattingDatesNoSeconds) {
        this.formattingDatesNoSeconds = formattingDatesNoSeconds;
    }

    public String getFormattingDatesJustClock() {
        return formattingDatesJustClock;
    }

    public void setFormattingDatesJustClock(String formattingDatesJustClock) {
        this.formattingDatesJustClock = formattingDatesJustClock;
    }

    public boolean isFormattingDatesDayNames() {
        return formattingDatesDayNames;
    }

    public void setFormattingDatesDayNames(boolean formattingDatesDayNames) {
        this.formattingDatesDayNames = formattingDatesDayNames;
    }

    public String getFormattingDatesDayNamesPattern() {
        return formattingDatesDayNamesPattern;
    }

    public void setFormattingDatesDayNamesPattern(String formattingDatesDayNamesPattern) {
        this.formattingDatesDayNamesPattern = formattingDatesDayNamesPattern;
    }
}
