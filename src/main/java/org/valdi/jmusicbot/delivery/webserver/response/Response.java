package org.valdi.jmusicbot.delivery.webserver.response;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.lang3.StringUtils;
import org.valdi.jmusicbot.settings.locale.Locale;
import org.valdi.jmusicbot.settings.theme.Theme;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.zip.GZIPOutputStream;

/**
 * @author Rsl1122
 */
public abstract class Response {
    private String type;
    private String header;
    private String content;

    protected Headers responseHeaders;

    public Response(ResponseType type) {
        this.type = type.get();
    }

    /**
     * Default Response constructor that defaults ResponseType to HTML.
     */
    public Response() {
        this(ResponseType.HTML);
    }

    protected String getHeader() {
        return header;
    }

    public Optional<String> getHeader(String called) {
        if (header != null) {
            for (String header : StringUtils.split(header, "\r\n")) {
                if (called == null) {
                    return Optional.of(header);
                }
                if (StringUtils.startsWith(header, called)) {
                    return Optional.of(StringUtils.split(header, ':')[1].trim());
                }
            }
        }
        return Optional.empty();
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getResponse() {
        return header + "\r\n"
                + "Content-Type: " + type + ";\r\n"
                + "Content-Length: " + content.length() + "\r\n"
                + "\r\n"
                + content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCode() {
        return getHeader(null).map(h -> Integer.parseInt(StringUtils.split(h, ' ')[1])).orElse(500);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Response response = (Response) o;
        return Objects.equals(header, response.header) &&
                Objects.equals(content, response.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header, content);
    }

    protected void setType(ResponseType type) {
        this.type = type.get();
    }

    public void setResponseHeaders(Headers responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    protected void translate(Function<String, String> translator) {
        content = translator.apply(content);
    }

    protected void fixThemeColors(Theme theme) {
        content = theme.replaceThemeColors(content);
    }

    public void send(HttpExchange exchange, Locale locale, Theme theme) throws IOException {
        responseHeaders.set("Content-Type", type);
        responseHeaders.set("Content-Encoding", "gzip");
        exchange.sendResponseHeaders(getCode(), 0);

        try (GZIPOutputStream out = new GZIPOutputStream(exchange.getResponseBody());
             ByteArrayInputStream bis = new ByteArrayInputStream((content != null ? content : "").getBytes(StandardCharsets.UTF_8))) {
            byte[] buffer = new byte[2048];
            int count;
            while ((count = bis.read(buffer)) != -1) {
                out.write(buffer, 0, count);
            }
        }
    }

    @Override
    public String toString() {
        return header + " | " + getResponse();
    }
}
