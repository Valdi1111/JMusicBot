package org.valdi.jmusicbot.delivery.webserver.response;

import org.apache.commons.lang3.StringUtils;
import org.valdi.jmusicbot.JMusicBot;

import java.io.IOException;

/**
 * Response class for returning file contents.
 * <p>
 * Created to remove copy-paste.
 *
 * @author Rsl1122
 */
public class FileResponse extends Response {

    public FileResponse(JMusicBot main, String fileName) throws IOException {
        super.setHeader("HTTP/1.1 200 OK");
        super.setContent(main.getCustomizableResourceOrDefault(fileName).asString());
    }

    public static String format(String fileName) {
        String[] split = StringUtils.split(fileName, '/');
        int i;
        for (i = 0; i < split.length; i++) {
            String s = split[i];
            if (equalsOne(s, "css", "js", "images", "plugins", "scss", "pages", "data")) {
                break;
            }
        }
        StringBuilder b = new StringBuilder("web");
        for (int j = i; j < split.length; j++) {
            String s = split[j];
            b.append('/').append(s);
        }
        return b.toString();
    }

    /**
     * Check if a String equals one option.
     *
     * @param toCheck String to check against
     * @param isEqual Options that are considered good.
     * @return If one of the given strings matches the first one.
     */
    public static boolean equalsOne(String toCheck, String... isEqual) {
        if (isEqual == null) {
            return false;
        }
        if (toCheck == null && containsNull((Object[]) isEqual)) {
            return true;
        }
        for (String s : isEqual) {
            if (s.equals(toCheck)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if an array contains null objects.
     *
     * @param objects Objects to check.
     * @return true if a null is found.
     */
    public static boolean containsNull(Object... objects) {
        for (Object t : objects) {
            if (t == null) {
                return true;
            }
        }
        return false;
    }
}
