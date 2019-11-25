package org.valdi.jmusicbot.webserver;

import org.valdi.jmusicbot.JMusicBot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Date;

public class HttpServer extends Thread {
    private final Logger logger = LogManager.getLogger("WebServer");
    private final boolean verbose = true;
    private final JMusicBot main;

    public HttpServer(JMusicBot main) {
        super("WebServer Thread");

        this.main = main;
    }

    @Override
    public void run() {
        try {
            int port = main.getConfig().getWebServerPort();
            ServerSocket server = new ServerSocket(port);
            logger.info("Server started! Listening for connections on port : {} ...", port);

            while(true) {
                HttpServerHandler httpServer = new HttpServerHandler(main, server.accept());

                if(verbose) {
                    logger.info("Connection open. ({})", new Date());
                }

                httpServer.start();
            }
        } catch (IOException e) {
            logger.error("Error on server socket", e);
        }
    }
}
