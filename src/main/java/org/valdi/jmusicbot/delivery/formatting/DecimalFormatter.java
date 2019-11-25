package org.valdi.jmusicbot.delivery.formatting;

import org.valdi.jmusicbot.JMusicBot;

import java.text.DecimalFormat;

/**
 * Formatter for decimal points that depends on settings.
 *
 * @author Rsl1122
 */
public class DecimalFormatter implements Formatter<Double> {
    private final JMusicBot main;

    public DecimalFormatter(JMusicBot main) {
        this.main = main;
    }

    @Override
    public String apply(Double value) {
        // DecimalFormat is initialized here because config is not fully enabled in the constructor
        return new DecimalFormat(main.getConfig().getFormattingDecimals()).format(value);
    }
}
