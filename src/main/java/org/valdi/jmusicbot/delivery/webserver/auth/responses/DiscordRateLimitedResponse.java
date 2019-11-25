package org.valdi.jmusicbot.delivery.webserver.auth.responses;

import com.google.gson.annotations.SerializedName;

public class DiscordRateLimitedResponse {
    private boolean global;
    private String message;
    @SerializedName("retry_after")
    private int retryAfter;

    public boolean isGlobal() {
        return global;
    }

    public String getMessage() {
        return message;
    }

    public int getRetryAfter() {
        return retryAfter;
    }
}
