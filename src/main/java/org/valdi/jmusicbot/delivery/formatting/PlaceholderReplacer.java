package org.valdi.jmusicbot.delivery.formatting;

import org.apache.commons.text.StringSubstitutor;
import org.valdi.jmusicbot.delivery.domain.container.DataContainer;
import org.valdi.jmusicbot.delivery.domain.keys.PlaceholderKey;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

/**
 * Formatter for replacing ${placeholder} values inside strings.
 *
 * @author Rsl1122
 */
public class PlaceholderReplacer extends HashMap<String, Serializable> implements Formatter<String> {

    public <T> void addPlaceholderFrom(DataContainer container, PlaceholderKey<T> key) {
        put(key.getPlaceholder(), container.getValue(key).map(Objects::toString).orElse("Missing value " + key.getPlaceholder()));
    }

    public void addAllPlaceholdersFrom(DataContainer container, PlaceholderKey... keys) {
        for (PlaceholderKey key : keys) {
            addPlaceholderFrom(container, key);
        }
    }

    public <T> void addPlaceholderFrom(DataContainer container, Formatter<T> formatter, PlaceholderKey<T> key) {
        if (!container.supports(key)) {
            return;
        }
        put(key.getPlaceholder(), container.getFormattedUnsafe(key, formatter));
    }

    public <T> void addAllPlaceholdersFrom(DataContainer container, Formatter<T> formatter, PlaceholderKey<T>... keys) {
        for (PlaceholderKey<T> key : keys) {
            addPlaceholderFrom(container, formatter, key);
        }
    }

    @Override
    public String apply(String string) {
        StringSubstitutor sub = new StringSubstitutor(this);
        sub.setEnableSubstitutionInVariables(true);
        return sub.replace(string);
    }
}