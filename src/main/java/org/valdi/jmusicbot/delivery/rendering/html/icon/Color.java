package org.valdi.jmusicbot.delivery.rendering.html.icon;

import java.util.Optional;

public enum Color {
    RED("col-red"),
    PINK("col-pink"),
    PURPLE("col-purple"),
    DEEP_PURPLE("col-deep-purple"),
    INDIGO("col-indigo"),
    BLUE("col-blue"),
    LIGHT_BLUE("col-light-blue"),
    CYAN("col-cyan"),
    TEAL("col-teal"),
    GREEN("col-green"),
    LIGHT_GREEN("col-light-green"),
    LIME("col-lime"),
    YELLOW("col-yellow"),
    AMBER("col-amber"),
    ORANGE("col-orange"),
    DEEP_ORANGE("col-deep-orange"),
    BROWN("col-brown"),
    GREY("col-grey"),
    BLUE_GREY("col-blue-grey"),
    BLACK("col-black"),
    NONE("");

    private final String htmlClass;

    Color(String htmlClass) {
        this.htmlClass = htmlClass;
    }

    public static Color matchString(String name) {
        String lowerCaseName = name.toLowerCase();
        for (Color color : values()) {
            if (color.htmlClass.contains(lowerCaseName)) {
                return color;
            }
        }
        return Color.BLACK;
    }

    public static Optional<Color> getByName(String name) {
        if (name == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(valueOf(name));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public String getHtmlClass() {
        return htmlClass;
    }
}
