package org.valdi.jmusicbot.delivery.webserver.response;

import org.valdi.jmusicbot.JMusicBot;
import org.valdi.jmusicbot.settings.locale.Locale;

import java.io.IOException;

/**
 * @author Rsl1122
 */
public class JavaScriptResponse extends FileResponse {

    JavaScriptResponse(JMusicBot main, String fileName) throws IOException {
        super(main, format(fileName));
        super.translate(main.getLocale()::replaceLanguageInJavascript);
        super.setType(ResponseType.JAVASCRIPT);
    }
}
