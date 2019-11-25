package org.valdi.jmusicbot.exceptions;

import org.valdi.jmusicbot.delivery.webserver.auth.FailReason;
import org.valdi.jmusicbot.exceptions.connection.WebException;

/**
 * Thrown when WebUser can not be authorized (WebServer).
 *
 * @author Rsl1122
 */
public class WebUserAuthException extends WebException {
    private final FailReason failReason;

    public WebUserAuthException(FailReason failReason) {
        super(failReason.getReason());
        this.failReason = failReason;
    }

    public WebUserAuthException(FailReason failReason, String additionalInfo) {
        super(failReason.getReason() + ": " + additionalInfo);
        this.failReason = failReason;
    }

    public WebUserAuthException(Throwable cause) {
        super(FailReason.ERROR.getReason(), cause);
        this.failReason = FailReason.ERROR;
    }

    public FailReason getFailReason() {
        return failReason;
    }
}
