package org.valdi.jmusicbot.delivery.webserver.auth;

import org.valdi.jmusicbot.delivery.domain.WebUser;
import org.valdi.jmusicbot.delivery.webserver.auth.responses.DiscordGuildResponse;
import org.valdi.jmusicbot.delivery.webserver.auth.responses.DiscordUserResponse;
import org.valdi.jmusicbot.exceptions.WebUserAuthException;

import java.util.List;

/**
 * Interface for different WebUser authentication methods used by Requests.
 *
 * @author Rsl1122
 */
public interface Authentication {

    WebUser getWebUser() throws WebUserAuthException;

    DiscordUserResponse getUser();

    List<DiscordGuildResponse> getGuilds();

}
