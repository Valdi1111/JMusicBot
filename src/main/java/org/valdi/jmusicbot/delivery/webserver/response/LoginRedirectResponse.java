package org.valdi.jmusicbot.delivery.webserver.response;

import com.sun.net.httpserver.HttpExchange;
import org.valdi.jmusicbot.settings.locale.Locale;
import org.valdi.jmusicbot.settings.theme.Theme;

import java.io.IOException;

/**
 * @author Rsl1122
 */
public class LoginRedirectResponse extends Response {
    private String direct;
    private String token;

    public LoginRedirectResponse(String direct, String token) {
        super.setHeader("HTTP/1.1 302 Found");
        this.direct = direct;
        this.token = token;
    }

    @Override
    public void send(HttpExchange exchange, Locale locale, Theme theme) throws IOException {
        responseHeaders.set("Location", direct);
        responseHeaders.set("Set-Cookie", "token=" + token + "; Path=/");
        super.send(exchange, locale, theme);
    }
}
