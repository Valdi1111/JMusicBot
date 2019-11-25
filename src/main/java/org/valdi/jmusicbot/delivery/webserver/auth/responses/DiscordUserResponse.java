package org.valdi.jmusicbot.delivery.webserver.auth.responses;

import com.google.gson.annotations.SerializedName;

public class DiscordUserResponse {
    private String id;
    private String username;
    private String discriminator;
    private String avatar;
    private boolean bot;
    @SerializedName("mfa_enabled")
    private boolean twoFactorAuthenticationEnabled;
    private boolean verified;
    private String email;

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public String getAvatar() {
        return avatar;
    }

    public boolean isBot() {
        return bot;
    }

    public boolean isTwoFactorAuthenticationEnabled() {
        return twoFactorAuthenticationEnabled;
    }

    public boolean isVerified() {
        return verified;
    }

    public String getEmail() {
        return email;
    }
}
