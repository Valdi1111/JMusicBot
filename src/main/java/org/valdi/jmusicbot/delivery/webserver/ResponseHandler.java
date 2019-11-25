package org.valdi.jmusicbot.delivery.webserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.valdi.jmusicbot.JMusicBot;
import org.valdi.jmusicbot.delivery.webserver.auth.Authentication;
import org.valdi.jmusicbot.delivery.webserver.pages.*;
import org.valdi.jmusicbot.delivery.webserver.pages.play.RootPlayHandler;
import org.valdi.jmusicbot.delivery.webserver.response.Response;
import org.valdi.jmusicbot.delivery.webserver.response.ResponseFactory;
import org.valdi.jmusicbot.delivery.webserver.response.errors.BadRequestResponse;
import org.valdi.jmusicbot.exceptions.WebUserAuthException;
import org.valdi.jmusicbot.exceptions.connection.*;

import java.util.Optional;

import static org.valdi.jmusicbot.delivery.webserver.pages.CallbackHandler.CALLBACK_FILE;

/**
 * Handles choosing of the correct response to a request.
 *
 * @author Rsl1122
 */
public class ResponseHandler extends TreePageHandler {
    private final Logger logger = LogManager.getLogger("WebServer ResponseHandler");

    private final JMusicBot main;
    private WebServer webServer;

    private final DebugPageHandler debugPageHandler;
    private final RootPlayHandler rootPlayHandler;
    private final CallbackHandler callbackHandler;
    private final LogoutHandler logoutHandler;

    public ResponseHandler(JMusicBot main, WebServer webServer, ResponseFactory responseFactory) {
        super(responseFactory);
        this.main = main;

        this.webServer = webServer;
        this.debugPageHandler = new DebugPageHandler(responseFactory);
        this.rootPlayHandler = new RootPlayHandler(main, responseFactory);
        this.callbackHandler = new CallbackHandler(main, responseFactory);
        this.logoutHandler = new LogoutHandler(responseFactory);
    }

    public void registerPages() {
        registerPage("debug", debugPageHandler);
        registerPage("play", rootPlayHandler);
        //registerPage("callback", callbackHandler);
        registerPage("logout", logoutHandler);

        registerPage("", new RootPageHandler(responseFactory, webServer));
    }

    public Response getResponse(Request request) {
        try {
            return tryToGetResponse(request);
        } catch (NotFoundException e) {
            return responseFactory.notFound404(e.getMessage());
        } catch (WebUserAuthException e) {
            return responseFactory.basicAuthFail(e);
        } catch (ForbiddenException e) {
            return responseFactory.forbidden403(e.getMessage());
        } catch (BadRequestException e) {
            return new BadRequestResponse(e.getMessage() + " (when requesting '" + request.getTargetString() + "')");
        } catch (InternalErrorException e) {
            if (e.getCause() != null) {
                return responseFactory.internalErrorResponse(e.getCause(), request.getTargetString());
            } else {
                return responseFactory.internalErrorResponse(e, request.getTargetString());
            }
        } catch (Exception e) {
            logger.error("Error thrown while getting response", e);
            return responseFactory.internalErrorResponse(e, request.getTargetString());
        }
    }

    private Response tryToGetResponse(Request request) throws WebException {
        Optional<Authentication> authentication = request.getAuth();
        RequestTarget target = request.getTarget();
        String resource = target.getResourceString();

        if (target.endsWith(".css")) {
            return responseFactory.cssResponse(resource);
        }
        if (target.endsWith(".js")) {
            return responseFactory.javaScriptResponse(resource);
        }
        if (target.endsWith(".png") || target.endsWith(".jpg") || target.endsWith(".jpeg")) {
            return responseFactory.imageResponse(resource);
        }
        if (target.endsWith("favicon.ico")) {
            return responseFactory.faviconResponse();
        }
        if (target.endsWith(CALLBACK_FILE)) {
            return callbackHandler.getResponse(request, target);
        }

        boolean isAuthRequired = webServer.isAuthRequired();
        if (isAuthRequired && !authentication.isPresent()) {
            return responseFactory.discordAuth();
        }
        PageHandler pageHandler = getPageHandler(target);
        if (pageHandler == null) {
            return responseFactory.pageNotFound404();
        } else {
            boolean isAuthorized = authentication.isPresent() && pageHandler.isAuthorized(authentication.get(), target);
            if (!isAuthRequired || isAuthorized) {
                return pageHandler.getResponse(request, target);
            }
            return responseFactory.forbidden403();
        }
    }
}
