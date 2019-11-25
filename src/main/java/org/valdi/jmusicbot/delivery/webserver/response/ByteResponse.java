package org.valdi.jmusicbot.delivery.webserver.response;

import com.sun.net.httpserver.HttpExchange;
import org.valdi.jmusicbot.JMusicBot;
import org.valdi.jmusicbot.settings.locale.Locale;
import org.valdi.jmusicbot.settings.theme.Theme;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * {@link Response} for raw bytes.
 *
 * @author Rsl1122
 */
public class ByteResponse extends Response {
    private final JMusicBot main;
    private final String fileName;

    public ByteResponse(JMusicBot main, ResponseType type, String fileName) {
        super(type);

        this.main = main;
        this.fileName = fileName;

        setHeader("HTTP/1.1 200 OK");
    }

    @Override
    public void send(HttpExchange exchange, Locale locale, Theme theme) throws IOException {
        responseHeaders.set("Accept-Ranges", "bytes");
        exchange.sendResponseHeaders(getCode(), 0);

        try (OutputStream out = exchange.getResponseBody();
             InputStream bis = main.getCustomizableResourceOrDefault(fileName).asInputStream()) {
            byte[] buffer = new byte[2048];
            int count;
            while ((count = bis.read(buffer)) != -1) {
                out.write(buffer, 0, count);
            }
        }
    }
}
