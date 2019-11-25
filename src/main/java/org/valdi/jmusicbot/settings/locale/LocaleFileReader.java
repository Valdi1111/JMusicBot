package org.valdi.jmusicbot.settings.locale;

import org.valdi.jmusicbot.file.Resource;
import org.valdi.jmusicbot.settings.locale.lang.Lang;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Utility for reading locale files.
 *
 * @author Rsl1122
 */
public class LocaleFileReader {
    private List<String> lines;

    public LocaleFileReader(Resource resource) throws IOException {
        lines = resource.asLines();
    }

    public void load(Locale locale) {
        Map<String, Lang> identifiers = LocaleSystem.getIdentifiers();
        lines.forEach(line -> {
            String[] split = line.split(" \\|\\| ");
            if (split.length == 2) {
                String identifier = split[0].trim();
                Lang msg = identifiers.get(identifier);
                if (msg != null) {
                    locale.put(msg, new Message(split[1]));
                }
            }
        });
    }

}