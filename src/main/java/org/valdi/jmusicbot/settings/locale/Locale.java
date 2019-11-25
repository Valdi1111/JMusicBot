package org.valdi.jmusicbot.settings.locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.valdi.jmusicbot.JMusicBot;
import org.valdi.jmusicbot.file.FileResource;
import org.valdi.jmusicbot.settings.locale.lang.HtmlLang;
import org.valdi.jmusicbot.settings.locale.lang.JSLang;
import org.valdi.jmusicbot.settings.locale.lang.Lang;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents loaded language information.
 *
 * @author Rsl1122
 */
public class Locale extends HashMap<Lang, Message> {
    private final Logger logger = LogManager.getLogger("Locale");
    private final JMusicBot main;
    private final LangCode langCode;

    public Locale(JMusicBot main) {
        this(main, LangCode.EN);
    }

    public Locale(JMusicBot main, LangCode langCode) {
        this.main = main;
        this.langCode = langCode;
    }

    public void load(File file) throws IOException {
        new LocaleFileReader(new FileResource(file.getName(), file)).load(this);
    }

    public LangCode getLangCode() {
        return langCode;
    }

    @Override
    public Message get(Object key) {
        Message storedValue = super.get(key);
        if (key instanceof Lang && storedValue == null) {
            return new Message(((Lang) key).getDefault());
        } else {
            return storedValue;
        }
    }

    public Optional<Message> getNonDefault(Object key) {
        Message storedValue = super.get(key);
        if (key instanceof Lang && storedValue == null) {
            return Optional.empty();
        } else {
            return Optional.of(storedValue);
        }
    }

    public String getString(Lang key) {
        return get(key).toString();
    }

    public String getString(Lang key, Serializable... values) {
        return get(key).parse(values);
    }

    public String[] getArray(Lang key) {
        return get(key).toArray();
    }

    public String[] getArray(Lang key, Serializable... values) {
        return get(key).toArray(values);
    }

    public String replaceLanguageInHtml(String from) {
        if (isEmpty()) {
            return from;
        }

        Pattern scripts = Pattern.compile("(<script>[\\s\\S]*?</script>|<script src=[\"|'].*[\"|']></script>|<link [\\s\\S]*?>)");

        Matcher scriptMatcher = scripts.matcher(from);
        List<String> foundScripts = new ArrayList<>();
        while (scriptMatcher.find()) {
            foundScripts.add(scriptMatcher.toMatchResult().group(0));
        }

        TranslatedString translated = new TranslatedString(from);
        Arrays.stream(HtmlLang.values())
                // Longest first so that entries that contain each other don't partially replace.
                .sorted((one, two) -> Integer.compare(
                        two.getIdentifier().length(),
                        one.getIdentifier().length()
                ))
                .forEach(lang -> getNonDefault(lang).ifPresent(replacement ->
                        translated.translate(lang.getDefault(), replacement.toString()))
                );

        StringBuilder complete = new StringBuilder(translated.length());

        String[] parts = scripts.split(translated.toString());
        for (int i = 0; i < parts.length; i++) {
            complete.append(parts[i]);
            if (i < parts.length - 1) {
                complete.append(replaceLanguageInJavascript(foundScripts.get(i)));
            }
        }

        return complete.toString();
    }

    public String replaceLanguageInJavascript(String from) {
        if (isEmpty()) {
            return from;
        }

        TranslatedString translated = new TranslatedString(from);
        Arrays.stream(JSLang.values())
                // Longest first so that entries that contain each other don't partially replace.
                .sorted((one, two) -> Integer.compare(
                        two.getIdentifier().length(),
                        one.getIdentifier().length()
                ))
                .forEach(lang -> getNonDefault(lang).ifPresent(replacement ->
                        translated.translate(lang.getDefault(), replacement.toString()))
                );

        for (Lang extra : new Lang[]{
                HtmlLang.UNIT_NO_DATA,
                HtmlLang.TITLE_WORLD_PLAYTIME,
                HtmlLang.LABEL_OPERATOR,
                HtmlLang.LABEL_BANNED,
                HtmlLang.SIDE_SESSIONS,
                HtmlLang.LABEL_PLAYTIME,
                HtmlLang.LABEL_AFK_TIME,
                HtmlLang.LABEL_LONGEST_SESSION,
                HtmlLang.LABEL_SESSION_MEDIAN,
                HtmlLang.LABEL_PLAYER_KILLS,
                HtmlLang.LABEL_MOB_KILLS,
                HtmlLang.LABEL_DEATHS,
                HtmlLang.LABEL_PLAYERS_ONLINE,
                HtmlLang.LABEL_REGISTERED,
                HtmlLang.TITLE_SERVER,
                HtmlLang.TITLE_LENGTH,
                HtmlLang.TITLE_AVG_PING,
                HtmlLang.TITLE_BEST_PING,
                HtmlLang.TITLE_WORST_PING,
                HtmlLang.LABEL_FREE_DISK_SPACE,
                HtmlLang.LABEL_NEW_PLAYERS,
                HtmlLang.LABEL_UNIQUE_PLAYERS,

        }) {
            getNonDefault(extra).ifPresent(replacement ->
                    translated.translate(extra.getDefault(), replacement.toString()));
        }

        return translated.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Locale)) return false;
        if (!super.equals(o)) return false;
        Locale locale = (Locale) o;
        return langCode == locale.langCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), langCode);
    }
}