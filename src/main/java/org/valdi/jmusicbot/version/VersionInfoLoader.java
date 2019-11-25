package org.valdi.jmusicbot.version;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Utility for loading version information from github.
 *
 * @author Rsl1122
 */
public class VersionInfoLoader {

    private static final String VERSION_TXT_URL = "https://raw.githubusercontent.com/Valdi1111/JMusicBot/master/versions.txt";

    private VersionInfoLoader() {
        /* Static method class */
    }

    /**
     * Loads version information from github.
     *
     * @return List of VersionInfo, newest version first.
     * @throws IOException                    If site can not be accessed.
     * @throws java.net.MalformedURLException If VERSION_TXT_URL is not valid.
     */
    public static List<VersionInfo> load() throws IOException {
        URL url = new URL(VERSION_TXT_URL);

        List<VersionInfo> versionInfo = new ArrayList<>();

        try (Scanner websiteScanner = new Scanner(url.openStream())) {
            while (websiteScanner.hasNextLine()) {
                checkLine(websiteScanner).ifPresent(lineParts -> {
                    boolean release = lineParts[0].equals("REL");
                    Version version = new Version(lineParts[1]);
                    String downloadUrl = lineParts[2];
                    String changeLogUrl = lineParts[3];

                    versionInfo.add(new VersionInfo(release, version, downloadUrl, changeLogUrl));
                });
            }
        }

        Collections.sort(versionInfo);
        return versionInfo;
    }

    private static Optional<String[]> checkLine(Scanner websiteScanner) {
        String line = websiteScanner.nextLine();
        if (!line.startsWith("REL") && !line.startsWith("DEV")) {
            return Optional.empty();
        }
        String[] parts = StringUtils.split(line, '|');
        if (parts.length < 4) {
            return Optional.empty();
        }
        return Optional.of(parts);
    }

}