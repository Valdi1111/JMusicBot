package org.valdi.jmusicbot.delivery.webserver.auth.exceptions;

public class DiscordAuthenticationException extends RuntimeException {
    public DiscordAuthenticationException(String reason) {
        super(reason);
    }
}
