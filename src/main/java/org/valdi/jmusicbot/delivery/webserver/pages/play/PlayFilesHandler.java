package org.valdi.jmusicbot.delivery.webserver.pages.play;

import net.dv8tion.jda.core.entities.Guild;
import org.valdi.jmusicbot.JMusicBot;
import org.valdi.jmusicbot.delivery.domain.WebUser;
import org.valdi.jmusicbot.delivery.webserver.Request;
import org.valdi.jmusicbot.delivery.webserver.RequestTarget;
import org.valdi.jmusicbot.delivery.webserver.auth.Authentication;
import org.valdi.jmusicbot.delivery.webserver.auth.responses.DiscordGuildResponse;
import org.valdi.jmusicbot.delivery.webserver.pages.PageHandler;
import org.valdi.jmusicbot.delivery.webserver.response.Response;
import org.valdi.jmusicbot.delivery.webserver.response.ResponseFactory;
import org.valdi.jmusicbot.exceptions.WebUserAuthException;
import org.valdi.jmusicbot.exceptions.connection.WebException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlayFilesHandler implements PageHandler {
    private final JMusicBot main;
    private final ResponseFactory responseFactory;

    public PlayFilesHandler(JMusicBot main, ResponseFactory responseFactory) {
        this.main = main;
        this.responseFactory = responseFactory;
    }

    @Override
    public Response getResponse(Request request, RequestTarget target) throws WebException {
        Optional<Authentication> auth = request.getAuth();
        if(!auth.isPresent()) {
            return responseFactory.discordAuth();
        }

        List<DiscordGuildResponse> userGuilds = auth.get().getGuilds();
        List<String> userGuildsIds = new ArrayList<>();
        if(userGuilds != null && !userGuilds.isEmpty()) {
            userGuilds.forEach(g -> userGuildsIds.add(g.getId()));
        }

        Optional<String> code = target.getParameter("guild");
        if(code.isPresent()) {
            Guild guild = main.getBot().getJDA().getGuildById(code.get());
            if(guild == null || !userGuildsIds.contains(code.get())) {
                return responseFactory.pageNotFound404();
            }
            return responseFactory.playFilesPageResponse(auth.get(), guild);
        }

        List<Guild> botGuilds = main.getBot().getJDA().getGuilds();
        List<Guild> guilds = new ArrayList<>();
        for(Guild guild : botGuilds) {
            if(userGuildsIds.contains(guild.getId())) {
                guilds.add(guild);
            }
        }

        if(guilds.size() == 1) {
            return responseFactory.redirectResponse("/play/files?guild=" + guilds.get(0).getId());
        }

        // TODO Ask which guild manage
        return responseFactory.redirectResponse("/play/files?guild=" + guilds.get(0).getId());
    }

    @Override
    public boolean isAuthorized(Authentication auth, RequestTarget target) throws WebUserAuthException {
        WebUser webUser = auth.getWebUser();
        return webUser.getPermLevel() >= 0;
    }
}
