package org.valdi.jmusicbot.delivery.webserver.response.errors;

import org.valdi.jmusicbot.JMusicBot;
import org.valdi.jmusicbot.delivery.rendering.html.icon.Icon;

import java.io.IOException;

/**
 * Generic 404 response.
 *
 * @author Rsl1122
 */
public class NotFoundResponse extends ErrorResponse {

    public NotFoundResponse(JMusicBot main, String msg) throws IOException {
        super(main);
        super.setHeader("HTTP/1.1 404 Not Found");
        super.setTitle(Icon.called("map-signs") + " 404 Not Found");
        super.setParagraph(msg);
        super.replacePlaceholders();
    }

}
