package org.valdi.jmusicbot.delivery.webserver.auth.responses;

import com.google.gson.annotations.SerializedName;

public class DiscordOAuthTokenResponse {
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("token_type")
    private String tokenType = null;
    @SerializedName("expires_in")
    private long expiresIn = 0;
    @SerializedName("refresh_token")
    private String refreshToken = null;
    private String scope = null;

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getScope() {
        return scope;
    }
}
