package org.valdi.jmusicbot.delivery.rendering.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.valdi.jmusicbot.JMusicBot;
import org.valdi.jmusicbot.benchmarking.Benchmark;
import org.valdi.jmusicbot.delivery.rendering.html.Html;
import org.valdi.jmusicbot.delivery.rendering.html.icon.Icon;
import org.valdi.jmusicbot.delivery.rendering.html.structure.TabsElement;
import org.valdi.jmusicbot.delivery.webserver.cache.JSONCache;
import org.valdi.jmusicbot.file.ResourceCache;

import java.util.*;

/**
 * Html parsing for the Debug page.
 *
 * @author Rsl1122
 */
public class DebugPage implements Page {
    private final Logger logger = LogManager.getLogger("WebServer DebugPage");
    private final JMusicBot main;

    DebugPage(JMusicBot main) {
        this.main = main;
    }

    @Override
    public String toHtml() {
        StringBuilder preContent = new StringBuilder();

        String issueLink = Html.LINK_EXTERNAL.parse("https://github.com/Rsl1122/Plan-PlayerAnalytics/issues/new", "Create new issue on Github");
        String hastebinLink = Html.LINK_EXTERNAL.parse("https://hastebin.com/", "Create a new hastebin paste");

        preContent.append("<p>")
                .append(Html.separateWithDots(issueLink, hastebinLink)).append("<br><br>")
                .append("This page contains debug information for an issue ticket. You can copy it directly into the issue, the info is pre-formatted.")
                .append("</p>");

        TabsElement.Tab info = new TabsElement.Tab(Icon.called("server") + " Server Information", createServerInfoContent());
        //TabsElement.Tab errors = new TabsElement.Tab(Icon.called("exclamation-circle") + " Errors", createErrorContent());
        //TabsElement.Tab debugLog = new TabsElement.Tab(Icon.called("bug") + " Debug Log", createDebugLogContent());
        TabsElement.Tab caches = new TabsElement.Tab(Icon.called("archive") + " JMusicBot Caches", createCacheContent());

        TabsElement tabs = new TabsElement(info, /*errors, debugLog,*/ caches);

        return preContent + tabs.toHtmlFull();
    }

    private String createCacheContent() {
        StringBuilder content = new StringBuilder();
        appendResourceCache(content);
        appendJSONCache(content);
        //appendSessionCache(content);
        return content.toString();
    }

    private void appendResourceCache(StringBuilder content) {
        try {
            content.append("<pre>### Cached Resources (from File or Jar):<br><br>");
            List<String> cacheKeys = ResourceCache.getCachedResourceNames();
            if (cacheKeys.isEmpty()) {
                content.append("Empty");
            }
            for (String cacheKey : cacheKeys) {
                content.append("- ").append(cacheKey).append("<br>");
            }
            content.append("</pre>");
        } catch (Exception e) {
            logger.warn("Error", e);
        }
    }

    private void appendJSONCache(StringBuilder content) {
        try {
            content.append("<pre>### Cached JSON:<br><br>");
            List<String> cacheKeys = JSONCache.getCachedIDs();
            if (cacheKeys.isEmpty()) {
                content.append("Empty");
            }
            for (String cacheKey : cacheKeys) {
                content.append("- ").append(cacheKey).append("<br>");
            }
            content.append("</pre>");
        } catch (Exception e) {
            logger.warn("Error", e);
        }
    }

    /*private void appendSessionCache(StringBuilder content) {
        try {
            content.append("<pre>### Session Cache:<br><br>");
            content.append("Name | Session Started <br>")
                    .append("-- | -- <br>");
            Set<Map.Entry<UUID, Session>> sessions = SessionCache.getActiveSessions().entrySet();
            if (sessions.isEmpty()) {
                content.append("Empty");
            }
            for (Map.Entry<UUID, Session> entry : sessions) {
                Session session = entry.getValue();
                String name = session.getValue(SessionKeys.NAME).orElse(entry.getKey().toString());
                String start = session.getValue(SessionKeys.START).map(yearFormatter).orElse("Unknown");
                content.append(name).append(" | ").append(start).append("<br>");
            }
            content.append("</pre>");
        } catch (Exception e) {
            logger.warn("Error", e);
        }
    }

    private String createDebugLogContent() {
        StringBuilder content = new StringBuilder();
        appendDebugLog(content);
        return content.toString();
    }

    private String createErrorContent() {
        StringBuilder content = new StringBuilder();
        appendLoggedErrors(content);
        return content.toString();
    }*/

    private String createServerInfoContent() {
        StringBuilder content = new StringBuilder();

        appendServerInformation(content);
        appendBenchmarks(content);

        return content.toString();
    }

    private void appendServerInformation(StringBuilder content) {
        //ServerProperties serverProperties = serverInfo.getServerProperties();

        content.append("<pre>### Server Information<br>")
                .append("**JMusicBot Version:** ")
                .append(main.getVersionCheckSystem().getCurrentVersion())
                .append("<br>");

        /*content.append("**Server:** ");
        content.append(serverProperties.getName())
                .append(" ").append(serverProperties.getImplVersion())
                .append(" (").append(serverProperties.getVersion());
        content.append(")<br>");

        content.append("**Database:** ").append(database.getType().getName());*/
        content.append("<br><br>");

        Properties properties = System.getProperties();

        String osName = properties.getProperty("os.name");
        String osVersion = properties.getProperty("os.version");
        String osArch = properties.getProperty("os.arch");

        String javaVendor = properties.getProperty("java.vendor");
        String javaVersion = properties.getProperty("java.version");

        String javaVMVendor = properties.getProperty("java.vm.vendor");
        String javaVMName = properties.getProperty("java.vm.name");
        String javaVMVersion = properties.getProperty("java.vm.version");

        content.append("**Operating SubSystem:** ").append(osName).append(" (").append(osArch)
                .append(") version ").append(osVersion).append("<br>");

        content.append("**Java Version:** ").append(javaVersion).append(", ").append(javaVendor).append("<br>");
        content.append("**Java VM Version:** ").append(javaVMName).append(" version ").append(javaVMVersion)
                .append(", ").append(javaVMVendor).append("<br>");

        content.append("</pre>");
    }

    private void appendBenchmarks(StringBuilder content) {
        content.append("<pre>### Benchmarks<br>&#96;&#96;&#96;<br>");
        try {
            for (Benchmark result : main.getTimings().getAverageResults()) {
                content.append(result.toString()).append("<br>");
            }
        } catch (Exception e) {
            content.append("Exception on Timings#getAverageResults");
        }
        content.append("&#96;&#96;&#96;</pre>");
    }

    /*private void appendLoggedErrors(StringBuilder content) {
        content.append("<pre>### Logged Errors<br>");

        if (errorHandler instanceof DefaultErrorHandler) {
            appendErrorLines(content, (DefaultErrorHandler) errorHandler);
        } else {
            content.append("Using incompatible ErrorHandler");
        }

        content.append("</pre>");
    }

    private void appendErrorLines(StringBuilder content, DefaultErrorHandler errorHandler) {
        List<String> lines = errorHandler.getErrorHandler(FolderTimeStampErrorFileLogger.class)
                .flatMap(FolderTimeStampFileLogger::getCurrentFile)
                .map(file -> {
                    try {
                        return FileResource.lines(file);
                    } catch (IOException e) {
                        errorHandler.log(L.WARN, this.getClass(), e);
                        return new ArrayList<String>();
                    }
                }).orElse(new ArrayList<>());
        SortedMap<String, List<String>> errors = FolderTimeStampErrorFileLogger.splitByError(lines);

        if (!errors.isEmpty()) {
            List<String> errorLines = new ArrayList<>();
            for (Map.Entry<String, List<String>> entry : errors.entrySet()) {
                StringBuilder errorLineBuilder = new StringBuilder();
                for (String line : entry.getValue()) {
                    errorLineBuilder.append(line).append("<br>");
                }
                String error = errorLineBuilder.toString();
                if (!errorLines.contains(error)) {
                    errorLines.add(error);
                }
            }
            for (String error : errorLines) {
                content.append("</pre><pre>&#96;&#96;&#96;<br>")
                        .append(error)
                        .append("&#96;&#96;&#96;");
            }
        } else {
            content.append("**No Errors logged.**<br>");
        }
    }

    private void appendDebugLog(StringBuilder content) {
        Optional<MemoryDebugLogger> memoryDebugLogger = this.debugLogger.getDebugLogger(MemoryDebugLogger.class);
        Map<String, List<String>> channels = memoryDebugLogger.map(MemoryDebugLogger::getChannels).orElse(new HashMap<>());

        if (channels.isEmpty()) {
            content.append("Incompatible Debug Logger in use (No MemoryDebugLogger)");
            return;
        }

        TabsElement.Tab[] tabs = channels.entrySet().stream()
                .sorted((one, two) -> String.CASE_INSENSITIVE_ORDER.compare(one.getKey(), two.getKey()))
                .map(channel -> {
                    String name = channel.getKey().isEmpty() ? "Default" : channel.getKey();
                    return new TabsElement.Tab(name, debugChannelContent(name, channel.getValue()));
                })
                .toArray(TabsElement.Tab[]::new);

        content.append(new TabsElement(tabs).toHtmlFull());
    }

    private String debugChannelContent(String channelName, List<String> lines) {
        StringBuilder content = new StringBuilder();
        content.append("<pre>### Debug (").append(channelName).append(")<br>&#96;&#96;&#96;<br>");
        for (String line : lines) {
            content.append(line).append("<br>");
        }
        content.append("&#96;&#96;&#96;</pre>");
        return content.toString();
    }*/
}