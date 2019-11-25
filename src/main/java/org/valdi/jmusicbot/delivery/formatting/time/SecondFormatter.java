package org.valdi.jmusicbot.delivery.formatting.time;

import org.valdi.jmusicbot.JMusicBot;

/**
 * Formatter for timestamp which includes seconds as the smallest entry.
 *
 * @author Rsl1122
 */
public class SecondFormatter extends DateFormatter {

    public SecondFormatter(JMusicBot main) {
        super(main);
    }

    @Override
    public String apply(Long date) {
        return date > 0 ? format(date) : "-";
    }

    private String format(Long date) {
        String format = main.getConfig().getFormattingDatesFull();

        if (main.getConfig().isFormattingDatesDayNames()) {
            format = replaceRecentDays(date, format);
        }

        return format(date, format);
    }
}