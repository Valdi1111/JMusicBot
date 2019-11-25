package org.valdi.jmusicbot.version;

import java.util.Objects;

/**
 * Data class for reading version.txt in https://github.com/Rsl1122/Plan-PlayerAnalytics.
 *
 * @author Rsl1122
 */
public class VersionInfo implements Comparable<VersionInfo> {

    private final boolean release;
    private final Version version;
    private final String downloadUrl;
    private final String changeLogUrl;

    public VersionInfo(boolean release, Version version, String downloadUrl, String changeLogUrl) {
        this.release = release;
        this.version = version;
        this.downloadUrl = downloadUrl;
        this.changeLogUrl = changeLogUrl;
    }

    public boolean isRelease() {
        return release;
    }

    public Version getVersion() {
        return version;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getChangeLogUrl() {
        return changeLogUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VersionInfo that = (VersionInfo) o;
        return release == that.release &&
                Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(release, version);
    }

    @Override
    public int compareTo(VersionInfo o) {
        return o.version.compareTo(this.version);
    }
}