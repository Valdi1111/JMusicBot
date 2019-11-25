package org.valdi.jmusicbot.delivery.webserver.response;

import com.sun.net.httpserver.HttpExchange;
import org.valdi.jmusicbot.JMusicBot;
import org.valdi.jmusicbot.settings.locale.Locale;
import org.valdi.jmusicbot.settings.theme.Theme;

import java.io.IOException;

/**
 * @author Rsl1122
 */
public class CSSResponse extends FileResponse {

    public CSSResponse(JMusicBot main, String fileName) throws IOException {
        super(main, format(fileName));
        super.setType(ResponseType.CSS);
        setContent(getContent());
    }

    @Override
    public void send(HttpExchange exchange, Locale locale, Theme theme) throws IOException {
        fixThemeColors(theme);
        super.send(exchange, locale, theme);
    }
}
