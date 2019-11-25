package org.valdi.jmusicbot.settings.theme;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.valdi.SuperApiX.common.config.IFileStorage;
import org.valdi.SuperApiX.common.config.IFilesProvider;
import org.valdi.jmusicbot.JMusicBot;

import java.io.File;
import java.util.Optional;

/**
 * Config that keeps track of theme.yml.
 *
 * @author Rsl1122
 */
public class ThemeConfig {
    private final Logger logger = LogManager.getLogger("Theme");
    private final JMusicBot main;
    private final IFileStorage storage;

    public ThemeConfig(JMusicBot main, File themeFile) {
        this.main = main;
        Optional<IFilesProvider> provider = main.getFilesProvider();
        if(!provider.isPresent()) {
            throw new RuntimeException("Fatal Error! File provider not found!");
        }

        this.storage = provider.get().createYamlFile(main, themeFile.getParentFile(), themeFile.getName());
    }

    public void load() {
        storage.loadOnly();
    }

    public String getString(String path) {
        return storage.getString(path);
    }

}
