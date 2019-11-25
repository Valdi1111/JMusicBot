package org.valdi.jmusicbot.version;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.valdi.jmusicbot.JMusicBot;
import org.valdi.jmusicbot.SubSystem;
import org.valdi.jmusicbot.settings.locale.lang.PluginLang;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * System for checking if new Version is available when the System initializes.
 *
 * @author Rsl1122
 */
public class VersionCheckSystem implements SubSystem {
    private final Logger logger = LogManager.getLogger("Version");
    private final JMusicBot main;
    private final String currentVersion;

    private VersionInfo newVersionAvailable;

    public VersionCheckSystem(JMusicBot main, String currentVersion) {
        this.main = main;
        this.currentVersion = currentVersion;
    }

    public boolean isNewVersionAvailable() {
        return getNewVersionAvailable().isPresent();
    }

    @Override
    public void enable() {
        if (!main.getConfig().isUpdateCheck()) {
            return;
        }

        try {
            List<VersionInfo> versions = VersionInfoLoader.load();
            if (!main.getConfig().isUpdateDevVersions()) {
                versions = versions.stream().filter(VersionInfo::isRelease).collect(Collectors.toList());
            }
            VersionInfo newestVersion = versions.get(0);
            if (Version.isNewVersionAvailable(new Version(currentVersion), newestVersion.getVersion())) {
                newVersionAvailable = newestVersion;
                String notification = main.getLocale().getString(
                        PluginLang.VERSION_AVAILABLE,
                        newestVersion.getVersion().toString(),
                        newestVersion.getChangeLogUrl()
                ) + (newestVersion.isRelease() ? "" : main.getLocale().getString(PluginLang.VERSION_AVAILABLE_DEV));
            } else {
                logger.info(main.getLocale().getString(PluginLang.VERSION_NEWEST));
            }
        } catch (IOException e) {
            logger.error(main.getLocale().getString(PluginLang.VERSION_FAIL_READ_VERSIONS));
        }
    }

    @Override
    public void disable() {
        /* Does not need to be closed */
    }

    public Optional<VersionInfo> getNewVersionAvailable() {
        return Optional.ofNullable(newVersionAvailable);
    }

    public Optional<String> getUpdateButton() {
        return getNewVersionAvailable()
                .map(v -> "<button class=\"btn bg-white col-plan\" data-target=\"#updateModal\" data-toggle=\"modal\" type=\"button\">" +
                        "<i class=\"fa fa-fw fa-download\"></i> v." + v.getVersion().getVersionString() +
                        "</button>"
                );
    }

    public String getCurrentVersionButton() {
        return "<button class=\"btn bg-plan\" data-target=\"#updateModal\" data-toggle=\"modal\" type=\"button\">" +
                "v." + getCurrentVersion() +
                "</button>";
    }

    public String getUpdateModal() {
        return getNewVersionAvailable()
                .map(v -> "<div class=\"modal-header\">" +
                        "<h5 class=\"modal-title\" id=\"updateModalLabel\">" +
                        "<i class=\"fa fa-fw fa-download\"></i> Version " + v.getVersion().getVersionString() + " is Available!" +
                        "</h5><button aria-label=\"Close\" class=\"close\" data-dismiss=\"modal\" type=\"button\"><span aria-hidden=\"true\">&times;</span></button>" +
                        "</div>" + // Close modal-header
                        "<div class=\"modal-body\">" +
                        "<p>A new version has been released and is now available for download." +
                        (v.isRelease() ? "" : "<br>This version is a DEV release.") + "</p>" +
                        "<a class=\"btn col-plan\" href=\"" + v.getChangeLogUrl() + "\" rel=\"noopener noreferrer\" target=\"_blank\">" +
                        "<i class=\"fa fa-fw fa-list\"></i> View Changelog</a>" +
                        "<a class=\"btn col-plan\" href=\"" + v.getDownloadUrl() + "\" rel=\"noopener noreferrer\" target=\"_blank\">" +
                        "<i class=\"fa fa-fw fa-download\"></i> Download Plan-" + v.getVersion().getVersionString() + ".jar</a>" +
                        "</div>") // Close modal-body
                .orElse("<div class=\"modal-header\">" +
                        "<h5 class=\"modal-title\" id=\"updateModalLabel\">" +
                        "<i class=\"far fa-fw fa-check-circle\"></i> You have version " + getCurrentVersion() + "" +
                        "</h5><button aria-label=\"Close\" class=\"close\" data-dismiss=\"modal\" type=\"button\"><span aria-hidden=\"true\">&times;</span></button>" +
                        "</div>" + // Close modal-header
                        "<div class=\"modal-body\">" +
                        "<p>You are running the latest version.</p>" +
                        "</div>"); // Close modal-body
    }

    public String getCurrentVersion() {
        return currentVersion;
    }
}
