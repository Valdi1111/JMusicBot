package org.valdi.jmusicbot.delivery.webserver.auth;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.valdi.jmusicbot.delivery.webserver.auth.exceptions.DiscordAuthenticationException;
import org.valdi.jmusicbot.delivery.webserver.auth.exceptions.RateLimitedException;
import org.valdi.jmusicbot.delivery.webserver.auth.responses.DiscordUserResponse;
import org.valdi.jmusicbot.delivery.webserver.auth.responses.DiscordErrorResponse;
import org.valdi.jmusicbot.delivery.webserver.auth.responses.DiscordOAuthTokenResponse;
import org.valdi.jmusicbot.delivery.webserver.auth.responses.DiscordRateLimitedResponse;
import org.valdi.jmusicbot.delivery.webserver.auth.responses.DiscordGuildResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscordAuth {
    private final static String API_BASE_URL = "https://discordapp.com/api";
    private final static String BOT_AUTHORIZE_URL = API_BASE_URL + "/oauth2/authorize";
    private final static String USER_IDENTIFICATION_URL = API_BASE_URL + "/users/@me";
    private final static String USER_GUILDS_URL = USER_IDENTIFICATION_URL + "/guilds";
    private final static String TOKEN_BASE_URL = API_BASE_URL + "/oauth2/token";
    private final static String TOKEN_BASE_REVOKE_URL = TOKEN_BASE_URL + "/revoke";

    private final static Gson gson = new Gson();
    private boolean waitOnRateLimit = true;

    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String accessToken;
    private String refreshToken;
    private long generatedIn;
    private long expiresIn;

    /**
     * Initializes a DiscordAuth instance
     *
     * @param redirectUri  your URL callback
     * @param clientId     your application client ID, get it on your Discord application config
     * @param clientSecret your application secret, get it on your Discord application config
     */
    public DiscordAuth(String redirectUri, String clientId, String clientSecret) {
        this(redirectUri, clientId, clientSecret, true);
    }

    /**
     * Initializes a DiscordAuth instance
     *
     * @param redirectUri     your URL callback
     * @param clientId        your application client ID, get it on your Discord application config
     * @param clientSecret    your application secret, get it on your Discord application config
     * @param waitOnRateLimit if the client is rate limited, we block the current thread to try again, if false, the method throws an exception
     */
    public DiscordAuth(String redirectUri, String clientId, String clientSecret, boolean waitOnRateLimit) {
        this.redirectUri = redirectUri;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.waitOnRateLimit = waitOnRateLimit;
    }

    public String getAuthorizationUrl(String[] scopes, String state) {
        StringBuilder builder = new StringBuilder();
        builder.append(BOT_AUTHORIZE_URL);
        builder.append("?response_type=code");
        builder.append("&client_id=").append(clientId);
        builder.append("&scope=").append(getScopes(scopes));
        if (state != null && !state.isEmpty()) {
            builder.append("&state=").append(state);
        }

        try {
            builder.append("&redirect_uri=").append(URLEncoder.encode(redirectUri, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    private String getScopes(String[] scopes) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for(String scope : scopes) {
            if(!first) {
                builder.append("%20");
            }

            builder.append(scope);
            first = false;
        }

        return builder.toString();
    }

    /**
     * Gets the OAuth2 access token
     *
     * @param authCode authentication Code received on the URL callback, configure your URL callback on your Discord application config
     * @return access token
     */
    public String getAccessTokenString(String authCode) {
        return doTokenExchange(authCode).getAccessToken();
    }

    /**
     * Regenerates the access token using the stored refresh token
     *
     * @return access token
     */
    public DiscordOAuthTokenResponse doTokenExchangeUsingRefreshToken() {
        return doTokenExchangeUsingRefreshToken(refreshToken);
    }

    /**
     * Starts a OAuth2 token exchange process
     *
     * @param authCode authentication Code received on the URL callback, configure your URL callback on your Discord application config
     * @return the authentication response
     */
    public DiscordOAuthTokenResponse doTokenExchange(String authCode) {
        Map<String, Object> payload = getAccessTokenPayload();
        payload.put("code", authCode);
        return doTokenExchange(payload);
    }

    /**
     * Regenerates the access token using a refresh token
     *
     * @return access token
     */
    public DiscordOAuthTokenResponse doTokenExchangeUsingRefreshToken(String refreshToken) {
        Map<String, Object> payload = getAccessTokenPayload();
        payload.put("refresh_token", refreshToken);
        return doTokenExchange(payload);
    }

    private Map<String, Object> getAccessTokenPayload() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("grant_type", "authorization_code");
        payload.put("redirect_uri", redirectUri);
        payload.put("client_id", clientId);
        payload.put("client_secret", clientSecret);
        return payload;
    }

    private DiscordOAuthTokenResponse doTokenExchange(Map<String, Object> payload) {
        HttpRequest req = HttpRequest
                .post(TOKEN_BASE_URL)
                .header("User-Agent", "Mozilla/5.0 (X11; U; Linux i686) Gecko/20071127 Firefox/2.0.0.11")
                .header("Content-Type", "application/x-www-form-urlencoded")
                // PAYLOAD IS NOT JSON
                .send(buildQuery(payload));

        String body = req.body();
        System.out.println(body);
        hasErrors(body);

        DiscordRateLimitedResponse rate = isRateLimited(body);
        if (rate != null) {
            if (waitOnRateLimit) {
                try {
                    Thread.sleep(rate.getRetryAfter());
                } catch (InterruptedException ignored) {
                }
                return doTokenExchangeUsingRefreshToken(refreshToken);
            } else {
                throw new RateLimitedException();
            }
        }

        DiscordOAuthTokenResponse s = gson.fromJson(body, DiscordOAuthTokenResponse.class);
        this.accessToken = s.getAccessToken(); // Store Access Token for later use
        this.refreshToken = s.getRefreshToken(); // Store Refresh Token for later use
        this.expiresIn = s.getExpiresIn();
        this.generatedIn = System.currentTimeMillis();
        return s;
    }

    /**
     * Gets if the current access token is valid
     *
     * @return
     */
    public boolean isValid() {
        return System.currentTimeMillis() > this.generatedIn + (this.expiresIn * 1000);
    }

    /**
     * Get the current user info
     *
     * @return current user response
     */
    public DiscordUserResponse getCurrentUserIdentification() {
        HttpRequest req = HttpRequest
                .get(USER_IDENTIFICATION_URL)
                .header("User-Agent", "Mozilla/5.0 (X11; U; Linux i686) Gecko/20071127 Firefox/2.0.0.11")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Bearer " + accessToken);

        String body = req.body();
        System.out.println(body);
        hasErrors(body);

        DiscordRateLimitedResponse rate = isRateLimited(body);
        if (rate != null) {
            if (waitOnRateLimit) {
                try {
                    Thread.sleep(rate.getRetryAfter());
                } catch (InterruptedException e) {
                }
                return getCurrentUserIdentification();
            } else {
                throw new RateLimitedException();
            }
        }

        return gson.fromJson(body, DiscordUserResponse.class);
    }

    /**
     * Get the current user guilds
     *
     * @return a list with the user guilds
     */
    public List<DiscordGuildResponse> getUserGuilds() {
        HttpRequest req = HttpRequest
                .get(USER_GUILDS_URL)
                .header("User-Agent", "Mozilla/5.0 (X11; U; Linux i686) Gecko/20071127 Firefox/2.0.0.11")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Bearer " + accessToken);

        String body = req.body();
        System.out.println(body);
        hasErrors(body);

        DiscordRateLimitedResponse rate = isRateLimited(body);
        if (rate != null) {
            if (waitOnRateLimit) {
                try {
                    Thread.sleep(rate.getRetryAfter());
                } catch (InterruptedException ignored) {
                }
                return getUserGuilds();
            } else {
                throw new RateLimitedException();
            }
        }

        return gson.fromJson(body, new TypeToken<List<DiscordGuildResponse>>() {
        }.getType());
    }

    private void hasErrors(String body) {
        try {
            DiscordErrorResponse err = gson.fromJson(body, DiscordErrorResponse.class);
            if (err.getError() != null || err.getMessage() != null) {
                throw new DiscordAuthenticationException(err.getError());
            }
        } catch (Exception ignored) {
        } // dirty workaround ;)
    }

    private DiscordRateLimitedResponse isRateLimited(String body) {
        try {
            DiscordRateLimitedResponse err = gson.fromJson(body, DiscordRateLimitedResponse.class);
            if (err.getMessage().equals("You are being rate limited.") || err.getRetryAfter() != 0) {
                return err;
            }
        } catch (Exception ignored) {
        } // dirty workaround ;)
        return null;
    }

    private String buildQuery(Map<String, Object> params) {
        String[] query = new String[params.size()];
        int index = 0;
        for (String key : params.keySet()) {
            String val = String.valueOf(params.get(key) != null ? params.get(key) : "");
            try {
                val = URLEncoder.encode(val, "UTF-8");
            } catch (UnsupportedEncodingException ignored) {
            }
            query[index++] = key + "=" + val;
        }

        return StringUtils.join(query, "&");
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getGeneratedIn() {
        return generatedIn;
    }

    public void setGeneratedIn(long generatedIn) {
        this.generatedIn = generatedIn;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
