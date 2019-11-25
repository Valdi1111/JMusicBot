package org.valdi.jmusicbot.delivery.webserver.response.pages;

import com.googlecode.htmlcompressor.compressor.HtmlCompressor;
import com.sun.net.httpserver.HttpExchange;
import org.valdi.jmusicbot.delivery.rendering.pages.Page;
import org.valdi.jmusicbot.delivery.webserver.response.Response;
import org.valdi.jmusicbot.delivery.webserver.response.ResponseType;
import org.valdi.jmusicbot.exceptions.ParseException;
import org.valdi.jmusicbot.settings.locale.Locale;
import org.valdi.jmusicbot.settings.theme.Theme;

import java.io.IOException;

/**
 * Response for all HTML Page responses.
 *
 * @author Rsl1122
 */
public class PageResponse extends Response {
    private static final HtmlCompressor HTML_COMPRESSOR = new HtmlCompressor();

    static {
        HTML_COMPRESSOR.setRemoveIntertagSpaces(true);
    }

    public PageResponse(ResponseType type) {
        super(type);
    }

    public PageResponse(Page page) throws ParseException {
        this(ResponseType.HTML);
        super.setHeader("HTTP/1.1 200 OK");
        setContent(page.toHtml());
    }

    public PageResponse() {
    }

    @Override
    public void send(HttpExchange exchange, Locale locale, Theme theme) throws IOException {
        translate(locale::replaceLanguageInHtml);
        fixThemeColors(theme);
        super.send(exchange, locale, theme);
    }

    @Override
    public void setContent(String content) {
        super.setContent(HTML_COMPRESSOR.compress(content));
    }
}