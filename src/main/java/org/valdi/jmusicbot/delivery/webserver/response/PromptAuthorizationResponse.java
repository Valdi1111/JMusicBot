package org.valdi.jmusicbot.delivery.webserver.response;

import org.valdi.jmusicbot.JMusicBot;
import org.valdi.jmusicbot.delivery.rendering.html.icon.Icon;
import org.valdi.jmusicbot.delivery.webserver.auth.FailReason;
import org.valdi.jmusicbot.delivery.webserver.response.errors.ErrorResponse;
import org.valdi.jmusicbot.exceptions.WebUserAuthException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rsl1122
 */
public class PromptAuthorizationResponse extends ErrorResponse {

    private static final String TIPS = "<br>- Ensure you have registered a user with <b>/plan register</b><br>"
            + "- Check that the username and password are correct<br>"
            + "- Username and password are case-sensitive<br>"
            + "<br>If you have forgotten your password, ask a staff member to delete your old user and re-register.";

    private PromptAuthorizationResponse(JMusicBot main) throws IOException {
        super(main);
        super.setTitle(Icon.called("lock").build() + " 401 Unauthorized");
    }

    public static PromptAuthorizationResponse getBasicAuthResponse(JMusicBot main) throws IOException {
        PromptAuthorizationResponse response = new PromptAuthorizationResponse(main);
        response.setHeader("HTTP/1.1 401 Access Denied\r\n"
                + "WWW-Authenticate: Basic realm=\"Plan WebUser (/plan register)\"");

        response.setParagraph("Authentication Failed." + TIPS);
        response.replacePlaceholders();
        return response;
    }

    public static PromptAuthorizationResponse getBasicAuthResponse(JMusicBot main, WebUserAuthException e) throws IOException {
        PromptAuthorizationResponse response = new PromptAuthorizationResponse(main);

        FailReason failReason = e.getFailReason();
        String reason = failReason.getReason();

        if (failReason == FailReason.ERROR) {
            StringBuilder errorBuilder = new StringBuilder("</p><pre>");
            for (String line : getStackTrace(e.getCause())) {
                errorBuilder.append(line);
            }
            errorBuilder.append("</pre>");

            reason += errorBuilder.toString();
        }

        response.setHeader("HTTP/1.1 401 Access Denied\r\n"
                + "WWW-Authenticate: Basic realm=\"" + failReason.getReason() + "\"");
        response.setParagraph("Authentication Failed.</p><p><b>Reason: " + reason + "</b></p><p>" + TIPS);
        response.replacePlaceholders();
        return response;
    }

    /**
     * Gets lines for stack trace recursively.
     *
     * @param throwable Throwable element
     * @return lines of stack trace.
     */
    private static List<String> getStackTrace(Throwable throwable) {
        List<String> stackTrace = new ArrayList<>();
        stackTrace.add(throwable.toString());
        for (StackTraceElement element : throwable.getStackTrace()) {
            stackTrace.add("    " + element.toString());
        }

        Throwable cause = throwable.getCause();
        if (cause != null) {
            List<String> causeTrace = getStackTrace(cause);
            if (!causeTrace.isEmpty()) {
                causeTrace.set(0, "Caused by: " + causeTrace.get(0));
                stackTrace.addAll(causeTrace);
            }
        }

        return stackTrace;
    }
}
