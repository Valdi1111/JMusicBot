package org.valdi.jmusicbot.settings.locale;

import org.apache.commons.lang3.StringUtils;

/**
 * Utility for translating String.
 * <p>
 * Improves performance by avoiding a double for-each loop since this class can be considered final in the lambda
 * expression in {@link Locale#replaceLanguageInHtml(String)}.
 *
 * @author Rsl1122
 */
public class TranslatedString {

    private String translating;

    public TranslatedString(String translating) {
        this.translating = translating;
    }

    public void translate(String replace, String with) {
        translating = StringUtils.replace(translating, replace, with);
    }

    @Override
    public String toString() {
        return translating;
    }

    public int length() {
        return translating.length();
    }
}