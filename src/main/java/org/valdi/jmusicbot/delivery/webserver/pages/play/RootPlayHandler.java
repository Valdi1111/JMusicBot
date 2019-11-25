package org.valdi.jmusicbot.delivery.webserver.pages.play;

import org.valdi.jmusicbot.JMusicBot;
import org.valdi.jmusicbot.delivery.domain.WebUser;
import org.valdi.jmusicbot.delivery.webserver.RequestTarget;
import org.valdi.jmusicbot.delivery.webserver.auth.Authentication;
import org.valdi.jmusicbot.delivery.webserver.pages.TreePageHandler;
import org.valdi.jmusicbot.delivery.webserver.response.ResponseFactory;
import org.valdi.jmusicbot.exceptions.WebUserAuthException;

public class RootPlayHandler extends TreePageHandler {

    public RootPlayHandler(JMusicBot main, ResponseFactory responseFactory) {
        super(responseFactory);

        registerPage("files", new PlayFilesHandler(main, responseFactory));
        registerPage("list", new PlayListHandler(main, responseFactory));
        registerPage("", responseFactory.redirectResponse("/play/files"), 0);
    }

    @Override
    public boolean isAuthorized(Authentication auth, RequestTarget target) throws WebUserAuthException {
        WebUser webUser = auth.getWebUser();
        return webUser.getPermLevel() >= 0;
    }

}
