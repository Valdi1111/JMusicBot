package org.valdi.jmusicbot.delivery.domain;

import java.util.Objects;

/**
 * Object containing webserver security user information.
 *
 * @author Rsl1122
 */
public class WebUser {
    private final String user;
    private final String saltedPassHash;
    private final int permLevel;

    public WebUser(String user, String saltedPassHash, int permLevel) {
        this.user = user;
        this.saltedPassHash = saltedPassHash;
        this.permLevel = permLevel;
    }

    public String getName() {
        return user;
    }

    public String getSaltedPassHash() {
        return saltedPassHash;
    }

    public int getPermLevel() {
        return permLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebUser webUser = (WebUser) o;
        return permLevel == webUser.permLevel &&
                Objects.equals(user, webUser.user) &&
                Objects.equals(saltedPassHash, webUser.saltedPassHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, saltedPassHash, permLevel);
    }

    @Override
    public String toString() {
        return "WebUser{" +
                "user='" + user + '\'' +
                ", saltedPassHash='" + saltedPassHash + '\'' +
                ", permLevel=" + permLevel +
                '}';
    }
}
