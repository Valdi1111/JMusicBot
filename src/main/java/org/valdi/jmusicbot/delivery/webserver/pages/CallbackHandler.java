package org.valdi.jmusicbot.delivery.webserver.pages;

import org.valdi.jmusicbot.JMusicBot;
import org.valdi.jmusicbot.delivery.webserver.Request;
import org.valdi.jmusicbot.delivery.webserver.RequestTarget;
import org.valdi.jmusicbot.delivery.webserver.auth.Authentication;
import org.valdi.jmusicbot.delivery.webserver.auth.DiscordAuth;
import org.valdi.jmusicbot.delivery.webserver.response.Response;
import org.valdi.jmusicbot.delivery.webserver.response.ResponseFactory;
import org.valdi.jmusicbot.exceptions.WebUserAuthException;
import org.valdi.jmusicbot.utils.Base64Util;

import java.util.Optional;

public class CallbackHandler implements PageHandler {
    public static final String[] SCOPES = new String[]{"identify", "email", "guilds"};
    public static final String CALLBACK_FILE = "/callback";

    private final JMusicBot main;
    private final ResponseFactory responseFactory;

    public CallbackHandler(JMusicBot main, ResponseFactory responseFactory) {
        this.main = main;
        this.responseFactory = responseFactory;
    }

    @Override
    public Response getResponse(Request request, RequestTarget target) {
        DiscordAuth auth = new DiscordAuth(main.getConfig().getWebServerExternalAddress() + CALLBACK_FILE,
                String.valueOf(main.getConfig().getClientId()),
                main.getConfig().getClientSecret());

        Optional<String> code = target.getParameter("code");
        if (!code.isPresent()) {
            return responseFactory.redirectResponse(auth.getAuthorizationUrl(SCOPES, null));
        }

        try {
            auth.doTokenExchange(code.get());
            String token = auth.getAccessToken() + ":" + auth.getRefreshToken();
            return responseFactory.loginRedirectResponse(Base64Util.encode(token));
        } catch(Exception e) {
            System.out.println("Error on discord auth > " + e);
            return responseFactory.pageNotFound404();
        }
    }

    @Override
    public boolean isAuthorized(Authentication auth, RequestTarget target) throws WebUserAuthException {
        return true;
    }
}
