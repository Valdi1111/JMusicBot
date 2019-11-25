package org.valdi.jmusicbot.settings.locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.valdi.SuperApiX.common.plugin.StoreLoader;
import org.valdi.jmusicbot.JMusicBot;
import org.valdi.jmusicbot.SubSystem;
import org.valdi.jmusicbot.delivery.webserver.auth.FailReason;
import org.valdi.jmusicbot.exceptions.EnableException;
import org.valdi.jmusicbot.settings.locale.lang.*;
import org.valdi.jmusicbot.utils.FileLister;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * System in charge of {@link Locale}.
 *
 * @author Rsl1122
 */
public class LocaleSystem implements SubSystem {
    public static final String LOCALES_FOLDER = "locales";
    public static final String LOCALES_EXTENSION = ".txt";

    private final Logger logger = LogManager.getLogger("Locale");
    private final JMusicBot main;
    private Locale locale;

    public LocaleSystem(JMusicBot main) {
        this.main = main;
    }

    public static Map<String, Lang> getIdentifiers() {
        Lang[][] lang = new Lang[][] {
                CommandLang.values(),
                CmdHelpLang.values(),
                DeepHelpLang.values(),
                PluginLang.values(),
                ManageLang.values(),
                GenericLang.values(),
                HtmlLang.values(),
                ErrorPageLang.values(),
                FailReason.values(),
                JSLang.values(),
        };

        return Arrays.stream(lang)
                .flatMap(Arrays::stream)
                .collect(Collectors.toMap(Lang::getIdentifier, Function.identity()));
    }

    public void enable() throws EnableException {
        this.copyDefaultLocales(main);

        LangCode lang = main.getConfig().getLocale();
        File localeFile = new File(getLocalesFolder(false), "locale_" + lang.name() + LOCALES_EXTENSION);
        if(!localeFile.exists()) {
            throw new EnableException("Locale file not found! Path: " + localeFile);
        }

        this.locale = new Locale(main, lang);
        try {
            locale.load(localeFile);
            logger.info("Using locale '{}' by {}", lang.getName(), lang.getAuthors());
        } catch (IOException e) {
            throw new EnableException("Error loading locale file! Path: " + localeFile, e);
        }
    }

    public File getLocalesFolder(boolean create) {
        File localesFolder = new File(main.getDataFolder(), LOCALES_FOLDER);

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
     * Copies all the locale files from the plugin jar to the filesystem.
     * Only done if the locale folder does not already exist.
     */
    public void copyDefaultLocales(StoreLoader loader) {
        try {
            List<String> locales = FileLister.listJar(loader, LOCALES_FOLDER, LOCALES_EXTENSION);
            if(locales.isEmpty()) {
                return;
            }

            // Get the folder
            File localeFolder = this.getLocalesFolder(true);

            // Run through the files and store the locales
            // Fill with the locale files from the jar
            // If it does exist, then new files will NOT be written!
            for (String name : locales) {
                // Get the last part of the name
                int lastIndex = name.lastIndexOf('/');
                File outFile = new File(localeFolder, name.substring(Math.max(lastIndex, 0), name.length()));
                InputStream initialStream = main.getResource(LOCALES_FOLDER + File.separator + name);
                if (!outFile.exists()) {
                    Files.copy(initialStream, outFile.toPath());
                }
            }
        } catch (IOException e) {
            logger.error("Could not copy locale files from jar ", e);
        }
    }

    public void disable() {
        // No action necessary on disable.
    }

    public Locale getLocale() {
        return locale;
    }
}