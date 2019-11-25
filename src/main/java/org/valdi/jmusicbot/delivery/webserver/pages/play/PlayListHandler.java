package org.valdi.jmusicbot.delivery.webserver.pages.play;

import net.dv8tion.jda.core.entities.Guild;
import org.valdi.jmusicbot.JMusicBot;
import org.valdi.jmusicbot.data.music.Playlist;
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
import java.util.Map;
import java.util.Optional;

public class PlayListHandler implements PageHandler {
    private final JMusicBot main;
    private final ResponseFactory responseFactory;

    public PlayListHandler(JMusicBot main, ResponseFactory responseFactory) {
        this.main = main;
        this.responseFactory = responseFactory;
    }

    @Override
    public Response getResponse(Request request, RequestTarget target) throws WebException {
        Optional<Authentication> auth = request.getAuth();
        if(!auth.isPresent()) {
            return responseFactory.discordAuth();
        }

        Optional<String> code = target.getParameter("guild");
        if(!code.isPresent()) {
            return responseFactory.pageNotFound404();
        }

        Optional<String> playlist = target.getParameter("id");
        if(!playlist.isPresent()) {
            return responseFactory.pageNotFound404();
        }

        List<DiscordGuildResponse> userGuilds = auth.get().getGuilds();
        List<String> userGuildsIds = new ArrayList<>();
        if(userGuilds != null && !userGuilds.isEmpty()) {
            userGuilds.forEach(g -> userGuildsIds.add(g.getId()));
        }

        Guild guild = main.getBot().getJDA().getGuildById(code.get());
        if(guild == null || !userGuildsIds.contains(code.get())) {
            return responseFactory.pageNotFound404();
        }

        Map<String, Playlist> playlists = main.getDataManager().getDatabase(guild).getPlaylists();
        if(!playlists.containsKey(playlist.get())) {
            return responseFactory.pageNotFound404();
        }

        return responseFactory.playListPageResponse(auth.get(), guild, playlist.get());
    }

    @Override
    public boolean isAuthorized(Authentication auth, RequestTarget target) throws WebUserAuthException {
        WebUser webUser = auth.getWebUser();
        return webUser.getPermLevel() >= 0;
    }
}
