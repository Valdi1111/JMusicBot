package org.valdi.jmusicbot.delivery.formatting.time;

import org.valdi.jmusicbot.JMusicBot;

/**
 * Formatter for a timestamp which includes year, but not seconds.
 *
 * @author Rsl1122
 */
public class YearFormatter extends DateFormatter {

    public YearFormatter(JMusicBot main) {
        super(main);
    }

    @Override
    public String apply(Long date) {
        return date > 0 ? format(date) : "-";
    }

    private String format(Long date) {
        String format = main.getConfig().getFormattingDatesNoSeconds();

        if (main.getConfig().isFormattingDatesDayNames()) {
            format = replaceRecentDays(date, format);
        }
        return format(date, format);
    }
}