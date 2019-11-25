package org.valdi.jmusicbot.settings.theme;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.valdi.SuperApiX.common.plugin.StoreLoader;
import org.valdi.jmusicbot.JMusicBot;
import org.valdi.jmusicbot.SubSystem;
import org.valdi.jmusicbot.exceptions.EnableException;
import org.valdi.jmusicbot.utils.FileLister;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.valdi.jmusicbot.settings.theme.ThemeVal.*;

/**
 * Enum that contains available themes.
 * <p>
 * Change config setting Theme.Base to select one.
 *
 * @author Rsl1122
 */
public class Theme implements SubSystem {
    public static final String THEMES_FOLDER = "themes";
    public static final String THEMES_EXTENSION = ".yml";

    private final Logger logger = LogManager.getLogger("Theme");
    private final JMusicBot main;
    private ThemeConfig themeConfig;

    public Theme(JMusicBot main) {
        this.main = main;
    }

    public String getValue(ThemeVal variable) {
        try {
            return getThemeValue(variable);
        } catch (NullPointerException | IllegalStateException e) {
            return variable.getDefaultValue();
        }
    }

    public String[] getPieColors(ThemeVal variable) {
        return Arrays.stream(StringUtils.split(getValue(variable), ','))
                .map(color -> StringUtils.remove(StringUtils.trim(color), '"'))
                .toArray(String[]::new);
    }

    @Override
    public void enable() throws EnableException {
        this.copyDefaultLocales(main);

        String fileName = this.getFileName(main.getConfig().getTheme());
        File themeFile = new File(getThemesFolder(false), fileName + THEMES_EXTENSION);
        if(!themeFile.exists()) {
            throw new EnableException("Theme file not found! Path: " + themeFile);
        }

        this.themeConfig = new ThemeConfig(main, themeFile);
        themeConfig.load();
        logger.info("Using theme '{}'", themeFile.getName());
    }

    public File getThemesFolder(boolean create) {
        File localesFolder = new File(main.getDataFolder(), THEMES_FOLDER);

        if(localesFolder.exists() && !localesFolder.isDirectory()) {
            throw new RuntimeException("Locales folder already exists, but it isn't a file!!");
        }

        // If there is no locale folder, create it
        if (!localesFolder.exists() && create) {
            localesFolder.mkdirs();
        }

        return localesFolder;
    }

    /**
     * Copies all the theme files from the plugin jar to the filesystem.
     * Only done if the theme folder does not already exist.
     */
    public void copyDefaultLocales(StoreLoader loader) {
        try {
            List<String> locales = FileLister.listJar(loader, THEMES_FOLDER, THEMES_EXTENSION);
            if(locales.isEmpty()) {
                return;
            }

            // Get the folder
            File themesFolder = this.getThemesFolder(true);

            // Run through the files and store the themes
            // Fill with the theme files from the jar
            // If it does exist, then new files will NOT be written!
            for (String name : locales) {
                // Get the last part of the name
                int lastIndex = name.lastIndexOf('/');
                File outFile = new File(themesFolder, name.substring(Math.max(lastIndex, 0), name.length()));
                InputStream initialStream = main.getResource(THEMES_FOLDER + File.separator + name);
                if (!outFile.exists()) {
                    Files.copy(initialStream, outFile.toPath());
                }
            }
        } catch (IOException e) {
            logger.error("Could not copy themes files from jar ", e);
        }
    }

    @Override
    public void disable() {
        // No need to save theme on disable
    }

    private String getColor(ThemeVal variable) {
        String path = variable.getThemePath();
        try {
            return themeConfig.getString(path);
        } catch (Exception | NoSuchFieldError e) {
            logger.error("Something went wrong with getting variable " + variable.name() + " for: " + path, e);
        }
        return variable.getDefaultValue();
    }

    public String replaceThemeColors(String resourceString) {
        return replaceVariables(resourceString,
                RED, PINK, PURPLE,
                DEEP_PURPLE, INDIGO, BLUE, LIGHT_BLUE, CYAN, TEAL, GREEN, LIGHT_GREEN, LIME,
                YELLOW, AMBER, ORANGE, DEEP_ORANGE, BROWN, GREY, BLUE_GREY, BLACK, WHITE,
                GRAPH_PUNCHCARD, GRAPH_PLAYERS_ONLINE, GRAPH_TPS_HIGH, GRAPH_TPS_MED, GRAPH_TPS_LOW,
                GRAPH_CPU, GRAPH_RAM, GRAPH_CHUNKS, GRAPH_ENTITIES, GRAPH_WORLD_PIE, FONT_STYLESHEET, FONT_FAMILY
        );
    }

    private String replaceVariables(String resourceString, ThemeVal... themeVariables) {
        List<String> replace = new ArrayList<>();
        List<String> with = new ArrayList<>();
        for (ThemeVal variable : themeVariables) {
            String value = getColor(variable);
            String defaultValue = variable.getDefaultValue();
            if (defaultValue.equals(value)) {
                continue;
            }
            replace.add(defaultValue);
            with.add(value);
        }
        replace.add("${defaultTheme}");
        with.add(getValue(ThemeVal.THEME_DEFAULT));

        return StringUtils.replaceEach(resourceString, replace.toArray(new String[0]), with.toArray(new String[0]));
    }

    private String getThemeValue(ThemeVal color) {
        return themeConfig.getString(color.getThemePath());
    }

    private String getFileName(String theme) {
        switch (theme.toLowerCase()) {
            case "soft":
            case "soften":
                return "soft";
            case "mute":
            case "lowsaturation":
                return "mute";
            case "pastel":
            case "bright":
            case "harsh":
            case "saturated":
            case "high":
                return "pastel";
            case "sepia":
            case "brown":
                return "sepia";
            case "grey":
            case "gray":
            case "greyscale":
            case "grayscale":
                return "greyscale";
            default:
                return "theme";
        }
    }
}
