package org.valdi.jmusicbot.delivery.rendering.html.icon;

import java.util.Optional;

public enum Family {
    SOLID(" fa fa-", "\"></i>"),
    REGULAR(" far fa-", "\"></i>"),
    BRAND(" fab fa-", "\"></i>"),
    @Deprecated
    LINE(" material-icons\">", "</i>");

    private final String middle;
    private final String suffix;

    Family(String middle, String suffix) {
        this.middle = middle;
        this.suffix = suffix;
    }

    public String appendAround(String color, String name) {
        return "<i class=\"" + color + middle + name + suffix;
    }

    public static Optional<Family> getByName(String name) {
        if (name == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(valueOf(name));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
