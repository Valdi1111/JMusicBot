package org.valdi.jmusicbot.settings.locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a Message that can be modified by the caller.
 *
 * @author Rsl1122
 */
public class Message {

    private final String content;

    public Message(String content) {
        this.content = content;
    }

    public String parse(Serializable... p) {
        Map<String, Serializable> replaceMap = new HashMap<>();

        for (int i = 0; i < p.length; i++) {
            replaceMap.put(String.valueOf(i), p[i].toString());
        }

        StringSubstitutor sub = new StringSubstitutor(replaceMap);

        return sub.replace(content);
    }

    public String[] toArray() {
        return StringUtils.split(content, '\\');
    }

    public String[] toArray(Serializable... p) {
        return parse(p).split(content, '\\');
    }

    @Override
    public String toString() {
        return content;
    }
}
