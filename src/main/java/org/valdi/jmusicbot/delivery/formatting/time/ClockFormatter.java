package org.valdi.jmusicbot.delivery.formatting.time;

import org.valdi.jmusicbot.JMusicBot;

/**
 * Formatter for a timestamp that only includes a clock.
 *
 * @author Rsl1122
 */
public class ClockFormatter extends DateFormatter {

    public ClockFormatter(JMusicBot main) {
        super(main);
    }

    @Override
    public String apply(Long date) {
        return date > 0 ? format(date) : "-";
    }

    private String format(Long date) {
        String format = main.getConfig().getFormattingDatesJustClock();

        return format(date, format);
    }
}