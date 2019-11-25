package org.valdi.jmusicbot.settings.locale;

/**
 * Language enum of supported languages, follows ISO 639-1 for language codes.
 *
 * @author Rsl1122
 */
public enum LangCode {

    CUSTOM("Custom", ""),
    EN("English", "Rsl1122"),
    CN("Simplified Chinese", "f0rb1d (佛壁灯) & qsefthuopq"),
    DE("Deutch", "Eyremba & fuzzlemann & Morsmorse"),
    FI("Finnish", "Rsl1122"),
    FR("French", "CyanTech & Aurelien"),
    JA("Japanese", "yukieji"),
    TR("Turkish", "TDJisvan"),
    PT_BR("Portuguese (Brazil)", "jvmuller");

    private final String name;
    private final String authors;

    LangCode(String name, String authors) {
        this.name = name;
        this.authors = authors;
    }

    public static LangCode fromString(String code) {
        try {
            return LangCode.valueOf(code.toUpperCase());
        } catch (IllegalArgumentException e) {
            return LangCode.EN;
        }
    }

    public String getName() {
        return name;
    }

    public String getAuthors() {
        return authors;
    }

    public String getFileName() {
        return "locale_" + name() + ".txt";
    }
}
