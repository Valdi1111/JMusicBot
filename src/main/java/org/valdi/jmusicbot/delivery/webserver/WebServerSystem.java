package org.valdi.jmusicbot.delivery.webserver;

import org.valdi.jmusicbot.JMusicBot;
import org.valdi.jmusicbot.SubSystem;
import org.valdi.jmusicbot.delivery.webserver.cache.JSONCache;
import org.valdi.jmusicbot.exceptions.EnableException;

/**
 * WebServer subsystem for managing WebServer initialization.
 *
 * @author Rsl1122
 */
public class WebServerSystem implements SubSystem {
    private final WebServer webServer;

    public WebServerSystem(JMusicBot main) {
        this.webServer = new WebServer(main);
    }

    @Override
    public void enable() throws EnableException {
        webServer.enable();
    }

    @Override
    public void disable() {
        webServer.disable();
        JSONCache.invalidateAll();
        JSONCache.cleanUp();
    }

    public WebServer getWebServer() {
        return webServer;
    }

}
