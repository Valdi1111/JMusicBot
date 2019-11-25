package org.valdi.jmusicbot.delivery.webserver.auth;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.commons.lang3.StringUtils;
import org.valdi.jmusicbot.JMusicBot;
import org.valdi.jmusicbot.delivery.domain.WebUser;
import org.valdi.jmusicbot.delivery.webserver.auth.responses.DiscordGuildResponse;
import org.valdi.jmusicbot.delivery.webserver.auth.responses.DiscordUserResponse;
import org.valdi.jmusicbot.exceptions.WebUserAuthException;
import org.valdi.jmusicbot.utils.Base64Util;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.valdi.jmusicbot.delivery.webserver.pages.CallbackHandler.CALLBACK_FILE;

/**
 * Authentication handling for Basic Auth.
 * <p>
 * Basic access authentication (Wikipedia):
 * https://en.wikipedia.org/wiki/Basic_access_authentication
 *
 * @author Rsl1122
 */
public class DiscordAuthentication implements Authentication {
    private static final Cache<String, DiscordUserResponse> discordUsers = Caffeine.newBuilder()
            .expireAfterWrite(90, TimeUnit.SECONDS)
            .build();

    private static final Cache<String, List<DiscordGuildResponse>> discordGuilds = Caffeine.newBuilder()
            .expireAfterWrite(90, TimeUnit.SECONDS)
            .build();

    private final JMusicBot main;
    private final String authenticationString;
    private final DiscordAuth auth;

    public DiscordAuthentication(JMusicBot main, String authenticationString) {
        this.main = main;
        this.authenticationString = authenticationString;
        this.auth = new DiscordAuth(main.getConfig().getWebServerExternalAddress() + CALLBACK_FILE,
                String.valueOf(main.getConfig().getClientId()),
                main.getConfig().getClientSecret());
    }

    @Override
    public WebUser getWebUser() throws WebUserAuthException {
        String decoded = Base64Util.decode(authenticationString);
        String[] userInfo = StringUtils.split(decoded, ':');
        if (userInfo.length != 2) {
            throw new WebUserAuthException(FailReason.USER_AND_PASS_NOT_SPECIFIED);
        }

        this.auth.setAccessToken(userInfo[0]);
        this.auth.setRefreshToken(userInfo[1]);
        return new WebUser(auth.getAccessToken(), auth.getRefreshToken(), 2);
    }

    @Override
    public DiscordUserResponse getUser() {
        DiscordUserResponse res = discordUsers.getIfPresent(auth.getAccessToken());
        if(res == null) {
            res = auth.getCurrentUserIdentification();
            discordUsers.put(auth.getAccessToken(), res);
        }
        return res;
    }

    @Override
    public List<DiscordGuildResponse> getGuilds() {
        List<DiscordGuildResponse> res = discordGuilds.getIfPresent(auth.getAccessToken());
        if(res == null) {
            res = auth.getUserGuilds();
            discordGuilds.put(auth.getAccessToken(), res);
        }
        return res;
    }
}
