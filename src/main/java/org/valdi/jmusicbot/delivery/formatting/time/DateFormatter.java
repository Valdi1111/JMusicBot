package org.valdi.jmusicbot.delivery.formatting.time;

import org.valdi.jmusicbot.JMusicBot;
import org.valdi.jmusicbot.delivery.formatting.Formatter;
import org.valdi.jmusicbot.settings.locale.lang.GenericLang;

import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Abstract formatter for a timestamp.
 *
 * @author Rsl1122
 */
public abstract class DateFormatter implements Formatter<Long> {
    protected final JMusicBot main;

    public DateFormatter(JMusicBot main) {
        this.main = main;
    }

    @Override
    public abstract String apply(Long value);

    protected String format(long epochMs, String format) {
        boolean useServerTime = main.getConfig().isUseServerTimezone();
        String localeSetting = main.getConfig().getLocale().name();
        java.util.Locale usedLocale = "default".equalsIgnoreCase(localeSetting)
                ? java.util.Locale.ENGLISH
                : java.util.Locale.forLanguageTag(localeSetting);
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, usedLocale);
        TimeZone timeZone = useServerTime ? TimeZone.getDefault() : TimeZone.getTimeZone("GMT");
        dateFormat.setTimeZone(timeZone);
        return dateFormat.format(epochMs);
    }

    protected String replaceRecentDays(long epochMs, String format) {
        return replaceRecentDays(epochMs, format, main.getConfig().getFormattingDatesDayNamesPattern());
    }

    protected String replaceRecentDays(long epochMs, String format, String pattern) {
        long now = System.currentTimeMillis();

        boolean useServerTime = main.getConfig().isUseServerTimezone();
        TimeZone timeZone = useServerTime ? TimeZone.getDefault() : TimeZone.getTimeZone("GMT");
        int offset = timeZone.getOffset(epochMs);

        // Time since Start of day: UTC + Timezone % 24 hours
        long fromStartOfDay = (now + offset) % TimeUnit.DAYS.toMillis(1L);
        if (epochMs > now - fromStartOfDay) {
            format = format.replace(pattern, main.getLocale().getString(GenericLang.TODAY));
        } else if (epochMs > now - TimeUnit.DAYS.toMillis(1L) - fromStartOfDay) {
            format = format.replace(pattern, main.getLocale().getString(GenericLang.YESTERDAY));
        } else if (epochMs > now - TimeUnit.DAYS.toMillis(5L)) {
            format = format.replace(pattern, "EEEE");
        }
        return format;
    }

}