package org.valdi.jmusicbot.delivery.webserver.response.errors;

import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.valdi.jmusicbot.JMusicBot;
import org.valdi.jmusicbot.delivery.webserver.response.pages.PageResponse;
import org.valdi.jmusicbot.settings.locale.Locale;
import org.valdi.jmusicbot.settings.theme.Theme;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents generic HTTP Error response that has the page style in it.
 *
 * @author Rsl1122
 */
public class ErrorResponse extends PageResponse {
    private final JMusicBot main;
    private String title;
    private String paragraph;

    public ErrorResponse(JMusicBot main) throws IOException {
        this(main, main.getCustomizableResourceOrDefault("web/error.html").asString());
    }

    public ErrorResponse(JMusicBot main, String message) {
        this.main = main;
        setContent(message);
    }

    public void replacePlaceholders() {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("title", title);
        String[] split = StringUtils.split(title, ">", 3);
        placeholders.put("titleText", split.length == 3 ? split[2] : title);
        placeholders.put("paragraph", paragraph);
        placeholders.put("version", main.getVersionCheckSystem().getUpdateButton().orElse(main.getVersionCheckSystem().getCurrentVersionButton()));
        placeholders.put("updateModal", main.getVersionCheckSystem().getUpdateModal());

        setContent(StringSubstitutor.replace(getContent(), placeholders));
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }

    @Override
    public void send(HttpExchange exchange, Locale locale, Theme theme) throws IOException {
        translate(locale::replaceLanguageInHtml);
        fixThemeColors(theme);
        super.send(exchange, locale, theme);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrorResponse)) return false;
        if (!super.equals(o)) return false;
        ErrorResponse that = (ErrorResponse) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(paragraph, that.paragraph);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, paragraph);
    }
}
