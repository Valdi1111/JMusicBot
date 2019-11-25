package org.valdi.jmusicbot.delivery.formatting.time;

import org.valdi.jmusicbot.JMusicBot;

/**
 * Formatter for a timestamp in ISO-8601 format without the clock.
 *
 * @author Rsl1122
 */
public class ISO8601NoClockFormatter extends DateFormatter {

    public ISO8601NoClockFormatter(JMusicBot main) {
        super(main);
    }

    @Override
    public String apply(Long date) {
        return date > 0 ? format(date) : "-";
    }

    private String format(Long date) {
        String format = "yyyy-MM-dd";

        return format(date, format);
    }
}