package org.valdi.jmusicbot.delivery.webserver.response;

import com.sun.net.httpserver.HttpExchange;
import org.valdi.jmusicbot.settings.locale.Locale;
import org.valdi.jmusicbot.settings.theme.Theme;

import java.io.IOException;

/**
 * @author Rsl1122
 */
public class RedirectResponse extends Response {

    private String direct;

    public RedirectResponse(String direct) {
        super.setHeader("HTTP/1.1 302 Found");
        this.direct = direct;
    }

    @Override
    public void send(HttpExchange exchange, Locale locale, Theme theme) throws IOException {
        responseHeaders.set("Location", direct);
        super.send(exchange, locale, theme);
    }
}
