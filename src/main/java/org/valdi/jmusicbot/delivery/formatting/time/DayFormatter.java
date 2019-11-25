package org.valdi.jmusicbot.delivery.formatting.time;

import org.valdi.jmusicbot.JMusicBot;

/**
 * Formatter for a timestamp which includes days as the smallest entry.
 *
 * @author Rsl1122
 */
public class DayFormatter extends DateFormatter {

    public DayFormatter(JMusicBot main) {
        super(main);
    }

    @Override
    public String apply(Long date) {
        return date > 0 ? format(date) : "-";
    }

    private String format(Long date) {
        String format = "MMMMM d";

        if (main.getConfig().isFormattingDatesDayNames()) {
            format = replaceRecentDays(date, format, "MMMMM");
        }

        return format(date, format);
    }
}