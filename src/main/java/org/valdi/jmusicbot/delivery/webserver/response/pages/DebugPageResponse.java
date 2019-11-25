package org.valdi.jmusicbot.delivery.webserver.response.pages;

import org.valdi.jmusicbot.JMusicBot;
import org.valdi.jmusicbot.delivery.rendering.html.icon.Icon;
import org.valdi.jmusicbot.delivery.rendering.pages.DebugPage;
import org.valdi.jmusicbot.delivery.webserver.response.errors.ErrorResponse;

import java.io.IOException;

/**
 * WebServer response for /debug-page used for easing issue reporting.
 *
 * @author Rsl1122
 */
public class DebugPageResponse extends ErrorResponse {

    public DebugPageResponse(JMusicBot main, DebugPage debugPage) throws IOException {
        super(main);
        super.setHeader("HTTP/1.1 200 OK");
        super.setTitle(Icon.called("bug") + " Debug Information");
        super.setParagraph(debugPage.toHtml());
        replacePlaceholders();
    }

}
