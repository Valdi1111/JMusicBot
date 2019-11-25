package org.valdi.jmusicbot.delivery.formatting;

import org.valdi.jmusicbot.JMusicBot;
import org.valdi.jmusicbot.delivery.domain.DateHolder;
import org.valdi.jmusicbot.delivery.formatting.time.*;

/**
 * Factory for new instances of different {@link Formatter}s.
 *
 * @author Rsl1122
 */
public class Formatters {
    private final JMusicBot main;

    private final DateHolderFormatter yearFormatter;
    private final DateHolderFormatter dayFormatter;
    private final DateHolderFormatter secondFormatter;
    private final DateHolderFormatter clockFormatter;
    private final DateHolderFormatter iso8601NoClockFormatter;

    private final YearFormatter yearLongFormatter;
    private final DayFormatter dayLongFormatter;
    private final SecondFormatter secondLongFormatter;
    private final ClockFormatter clockLongFormatter;
    private final ISO8601NoClockFormatter iso8601NoClockLongFormatter;

    private final TimeAmountFormatter timeAmountFormatter;
    private final TrackTimeFormatter trackTimeFormatter;

    private final DecimalFormatter decimalFormatter;
    private final PercentageFormatter percentageFormatter;

    public Formatters(JMusicBot main) {
        this.main = main;

        yearLongFormatter = new YearFormatter(main);
        dayLongFormatter = new DayFormatter(main);
        clockLongFormatter = new ClockFormatter(main);
        secondLongFormatter = new SecondFormatter(main);
        iso8601NoClockLongFormatter = new ISO8601NoClockFormatter(main);

        yearFormatter = new DateHolderFormatter(yearLongFormatter);
        dayFormatter = new DateHolderFormatter(dayLongFormatter);
        secondFormatter = new DateHolderFormatter(secondLongFormatter);
        clockFormatter = new DateHolderFormatter(clockLongFormatter);
        iso8601NoClockFormatter = new DateHolderFormatter(iso8601NoClockLongFormatter);

        timeAmountFormatter = new TimeAmountFormatter(main);
        trackTimeFormatter = new TrackTimeFormatter(main);

        decimalFormatter = new DecimalFormatter(main);
        percentageFormatter = new PercentageFormatter(decimalFormatter);
    }

    public Formatter<DateHolder> year() {
        return this.yearFormatter;
    }

    public Formatter<Long> yearLong() {
        return yearLongFormatter;
    }

    public Formatter<DateHolder> day() {
        return dayFormatter;
    }

    public Formatter<Long> dayLong() {
        return dayLongFormatter;
    }

    public Formatter<DateHolder> second() {
        return secondFormatter;
    }

    public Formatter<Long> secondLong() {
        return secondLongFormatter;
    }

    public Formatter<DateHolder> clock() {
        return clockFormatter;
    }

    public Formatter<Long> clockLong() {
        return clockLongFormatter;
    }

    public Formatter<DateHolder> iso8601NoClock() {
        return iso8601NoClockFormatter;
    }

    public Formatter<Long> iso8601NoClockLong() {
        return iso8601NoClockLongFormatter;
    }

    public Formatter<Long> timeAmount() {
        return timeAmountFormatter;
    }

    public TrackTimeFormatter trackTime() {
        return trackTimeFormatter;
    }

    public Formatter<Double> percentage() {
        return percentageFormatter;
    }

    public Formatter<Double> decimals() {
        return decimalFormatter;
    }
}