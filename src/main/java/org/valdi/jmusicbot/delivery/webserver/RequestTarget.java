package org.valdi.jmusicbot.delivery.webserver;

import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents URI of a requested resource.
 *
 * @author Rsl1122
 */
public class RequestTarget {

    private final String resourceString;
    private final List<String> resource;
    private final Map<String, String> parameters;

    public RequestTarget(URI targetURI) {
        resourceString = targetURI.getPath();
        resource = Arrays.stream(StringUtils.split(resourceString, '/'))
                .filter(part -> !part.isEmpty()).collect(Collectors.toList());

        parameters = new TreeMap<>();
        parseParameters(targetURI.getQuery());
    }

    private void parseParameters(String parameterString) {
        if (parameterString == null || parameterString.isEmpty()) {
            return;
        }

        String[] keysAndValues = StringUtils.split(parameterString, '&');
        for (String kv : keysAndValues) {
            if (kv.isEmpty()) {
                continue;
            }
            String[] keyAndValue = StringUtils.split(kv, "=", 2);
            if (keyAndValue.length >= 2) {
                parameters.put(keyAndValue[0], keyAndValue[1]);
            }
        }
    }

    public boolean isEmpty() {
        return resource.isEmpty();
    }

    public int size() {
        return resource.size();
    }

    public String get(int index) {
        return resource.get(index);
    }

    public void removeFirst() {
        if (!isEmpty()) {
            resource.remove(0);
        }
    }

    public boolean endsWith(String suffix) {
        return resourceString.endsWith(suffix);
    }

    public Optional<String> getParameter(String key) {
        return Optional.ofNullable(parameters.get(key));
    }

    public String getResourceString() {
        return resourceString;
    }
}