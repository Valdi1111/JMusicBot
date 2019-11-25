package org.valdi.jmusicbot.utils;

import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;

import java.net.URISyntaxException;
import java.util.Map;
import java.util.stream.Collectors;

public class YoutubeLinkUtils {

    public static String getThumbnailUrl(String videoId) {
        return "https://img.youtube.com/vi/" + videoId + "/mqdefault.jpg";
    }

    public static String getThumbnailUrl(String url, boolean retryValidPart) {
        return "https://img.youtube.com/vi/" + getVideoId(url, true) + "/mqdefault.jpg";
    }

    public static String getVideoId(String url, boolean retryValidPart) {
        UrlInfo urlInfo = getUrlInfo(url, true);
        if ("/watch".equals(urlInfo.path)) {
            String videoIds = urlInfo.parameters.get("v");
            if (videoIds != null) {
                return videoIds;
            }
        }
        return urlInfo.path.substring(1);
    }

    public static UrlInfo getUrlInfo(String url, boolean retryValidPart) {
        try {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://" + url;
            }

            URIBuilder builder = new URIBuilder(url);
            return new UrlInfo(builder.getPath(), builder.getQueryParams().stream()
                    .filter(it -> it.getValue() != null)
                    .collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue, (a, b) -> a)));
        } catch (URISyntaxException e) {
            if (retryValidPart) {
                return getUrlInfo(url.substring(0, e.getIndex() - 1), false);
            } else {
                throw new FriendlyException("Not a valid URL: " + url, FriendlyException.Severity.COMMON, e);
            }
        }
    }

    public static class UrlInfo {
        private final String path;
        private final Map<String, String> parameters;

        private UrlInfo(String path, Map<String, String> parameters) {
            this.path = path;
            this.parameters = parameters;
        }

        public String getPath() {
            return path;
        }

        public Map<String, String> getParameters() {
            return parameters;
        }
    }
}
