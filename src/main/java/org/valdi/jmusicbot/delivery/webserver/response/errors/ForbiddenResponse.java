package org.valdi.jmusicbot.delivery.webserver.response.errors;

import org.valdi.jmusicbot.JMusicBot;
import org.valdi.jmusicbot.delivery.rendering.html.icon.Family;
import org.valdi.jmusicbot.delivery.rendering.html.icon.Icon;

import java.io.IOException;

/**
 * @author Rsl1122
 */
public class ForbiddenResponse extends ErrorResponse {

    public ForbiddenResponse(JMusicBot main, String msg) throws IOException {
        super(main);
        super.setHeader("HTTP/1.1 403 Forbidden");
        super.setTitle(Icon.called("hand-paper").of(Family.REGULAR) + " 403 Forbidden - Access Denied");
        super.setParagraph(msg);
        super.replacePlaceholders();
    }
}
