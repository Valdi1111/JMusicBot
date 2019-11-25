/*
 *  This file is part of Player Analytics (Plan).
 *
 *  Plan is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License v3 as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Plan is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Plan. If not, see <https://www.gnu.org/licenses/>.
 */
package org.valdi.jmusicbot.delivery.webserver;

import com.dosse.upnp.UPnP;
import org.valdi.jmusicbot.JMusicBot;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.valdi.jmusicbot.SubSystem;
import org.valdi.jmusicbot.delivery.rendering.pages.PageFactory;
import org.valdi.jmusicbot.delivery.webserver.response.ResponseFactory;
import org.valdi.jmusicbot.exceptions.EnableException;
import org.valdi.jmusicbot.settings.locale.lang.PluginLang;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.concurrent.*;

public class WebServer implements SubSystem {
    private final Logger logger = LogManager.getLogger("WebServer");

    private final JMusicBot main;

    private int port;
    private boolean enabled = false;
    private HttpServer server;

    private boolean usingHttps = false;

    public WebServer(JMusicBot main) {
        this.main = main;
    }

    @Override
    public void enable() throws EnableException {
        this.port = main.getConfig().getWebServerPort();
        if(UPnP.isUPnPAvailable()) {
            UPnP.openPortTCP(port, "JMusicBot");
        }

        PageFactory pageFactory = new PageFactory(main);
        ResponseFactory responseFactory = new ResponseFactory(main, pageFactory);
        ResponseHandler responseHandler = new ResponseHandler(main, this, responseFactory);
        RequestHandler requestHandler = new RequestHandler(main, responseHandler, responseFactory);
        initServer(requestHandler);

        if (!isEnabled()) {
            if (main.getConfig().isWebServerActive()) {
                logger.warn(main.getLocale().getString(PluginLang.ENABLE_NOTIFY_WEB_SERVER_DISABLED));
            } else {
                logger.error(main.getLocale().getString(PluginLang.WEB_SERVER_FAIL_PORT_BIND, port));
            }
        }

        requestHandler.getResponseHandler().registerPages();
    }

    /**
     * Starts up the WebServer in a new Thread Pool.
     */
    private void initServer(RequestHandler requestHandler) {
        if (!main.getConfig().isWebServerActive()) {
            // WebServer has been disabled.
            return;
        }

        if (enabled) {
            // Server is already enabled stop code
            return;
        }

        try {
            usingHttps = startHttpsServer();

            logger.debug(usingHttps ? "Https Start Successful." : "Https Start Failed.");

            if (!usingHttps) {
                logger.info("§e" + main.getLocale().getString(PluginLang.WEB_SERVER_NOTIFY_HTTP_USER_AUTH));
                server = HttpServer.create(new InetSocketAddress(main.getConfig().getWebServerInternalIp(), port), 10);
            } else if (server == null) {
                logger.info("§eWebServer: Proxy HTTPS Override enabled. HTTP Server in use, make sure that your Proxy webserver is routing with HTTPS and AlternativeIP.Link points to the Proxy");
                server = HttpServer.create(new InetSocketAddress(main.getConfig().getWebServerInternalIp(), port), 10);
            }
            server.createContext("/", requestHandler);

            ExecutorService executor = new ThreadPoolExecutor(4, 8,
                    30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100),
                    new BasicThreadFactory.Builder()
                            .namingPattern("WebServer Thread - %d")
                            .uncaughtExceptionHandler((t, e) -> {
                                logger.warn("Exception in WebServer Thread", e);
                            }).build()
            );
            server.setExecutor(executor);
            server.start();

            enabled = true;

            logger.info(main.getLocale().getString(PluginLang.ENABLED_WEB_SERVER, server.getAddress().getPort(), getAccessAddress()));
            String alternativeIP = main.getConfig().getWebServerExternalIp();
            if (alternativeIP.isEmpty()) {
                logger.info("§e" + main.getLocale().getString(PluginLang.ENABLE_NOTIFY_EMPTY_IP));
            }
        } catch (IllegalArgumentException | IllegalStateException | IOException e) {
            logger.error("Error", e);
            enabled = false;
        }
    }

    private boolean startHttpsServer() {
        String keyStorePath = main.getConfig().getWebServerSslPath();

        if ("proxy".equalsIgnoreCase(keyStorePath)) {
            return true;
        }

        try {
            if (!Paths.get(keyStorePath).isAbsolute()) {
                keyStorePath = main.getDataFolder() + File.separator + keyStorePath;
            }
        } catch (InvalidPathException e) {
            logger.error("WebServer: Could not find Keystore: " + e);
        }

        char[] storepass = main.getConfig().getWebServerSslStorePass().toCharArray();
        char[] keypass = main.getConfig().getWebServerSslKeyPass().toCharArray();
        String alias = main.getConfig().getWebServerSslAlias();

        boolean startSuccessful = false;
        String keyStoreKind = keyStorePath.endsWith(".p12") ? "PKCS12" : "JKS";
        try (FileInputStream fIn = new FileInputStream(keyStorePath)) {
            KeyStore keystore = KeyStore.getInstance(keyStoreKind);

            keystore.load(fIn, storepass);
            Certificate cert = keystore.getCertificate(alias);

            if (cert == null) {
                throw new IllegalStateException("Certificate with Alias: " + alias + " was not found in the Keystore.");
            }

            logger.info("Certificate: " + cert.getType());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keystore, keypass);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(keystore);

            server = HttpsServer.create(new InetSocketAddress(main.getConfig().getWebServerInternalIp(), port), 10);
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(keyManagerFactory.getKeyManagers(), null/*trustManagerFactory.getTrustManagers()*/, null);

            ((HttpsServer) server).setHttpsConfigurator(new HttpsConfigurator(sslContext) {
                @Override
                public void configure(HttpsParameters params) {
                    SSLEngine engine = sslContext.createSSLEngine();

                    params.setNeedClientAuth(false);
                    params.setCipherSuites(engine.getEnabledCipherSuites());
                    params.setProtocols(engine.getEnabledProtocols());

                    SSLParameters defaultSSLParameters = sslContext.getDefaultSSLParameters();
                    params.setSSLParameters(defaultSSLParameters);
                }
            });
            startSuccessful = true;
        } catch (IllegalStateException e) {
            logger.error("Error", e);
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            logger.error(main.getLocale().getString(PluginLang.WEB_SERVER_FAIL_SSL_CONTEXT));
        } catch (FileNotFoundException e) {
            logger.info("§e" + main.getLocale().getString(PluginLang.WEB_SERVER_NOTIFY_NO_CERT_FILE, keyStorePath));
            logger.info(main.getLocale().getString(PluginLang.WEB_SERVER_NOTIFY_HTTP));
        } catch (IOException e) {
            logger.error("WebServer: " + e);
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException e) {
            logger.error(main.getLocale().getString(PluginLang.WEB_SERVER_FAIL_STORE_LOAD));
        }
        return startSuccessful;
    }

    /**
     * @return if the WebServer is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Shuts down the server - Async thread is closed with shutdown boolean.
     */
    @Override
    public void disable() {
        UPnP.closePortTCP(port);
        if (server != null) {
            shutdown();
            logger.info(main.getLocale().getString(PluginLang.DISABLED_WEB_SERVER));
        }
        enabled = false;
    }

    private void shutdown() {
        server.stop(0);
        Executor executor = server.getExecutor();
        if (executor instanceof ExecutorService) {
            ExecutorService service = (ExecutorService) executor;
            service.shutdown();
            try {
                if (!service.awaitTermination(5, TimeUnit.SECONDS)) {
                    service.shutdownNow();
                }
            } catch (InterruptedException e) {
                logger.error("WebServer ExecutorService shutdown thread interrupted on disable: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    public String getProtocol() {
        return usingHttps ? "https" : "http";
    }

    public boolean isUsingHTTPS() {
        return usingHttps;
    }

    public boolean isAuthRequired() {
        //return isUsingHTTPS();
        return true;
    }

    public String getAccessAddress() {
        return isEnabled() ? getProtocol() + "://" + getIP() : main.getConfig().getWebServerExternalAddress();
    }

    private String getIP() {
        return main.getConfig().getWebServerExternalIp().replace("%port%", String.valueOf(port));
    }
}
