package org.valdi.jmusicbot.delivery.formatting.time;

import org.valdi.jmusicbot.JMusicBot;
import org.valdi.jmusicbot.delivery.formatting.Formatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Formatter for time amount in milliseconds.
 *
 * @author Rsl1122
 */
public class TrackTimeFormatter implements Formatter<Long> {
    private final JMusicBot main;

    public TrackTimeFormatter(JMusicBot main) {
        this.main = main;
    }

    @Override
    public String apply(Long ms) {
        if (ms == null || ms < 0) {
            return "-";
        }
        long x = ms / 1000;
        x /= 3600;
        long hours = x % 24;

        Date date = new Date(ms);
        DateFormat formatter;
        if(hours > 0) {
            formatter = new SimpleDateFormat("HH:mm:ss");
        } else {
            formatter = new SimpleDateFormat("mm:ss");
        }
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(date);
    }
}