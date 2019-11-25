package org.valdi.jmusicbot.delivery.webserver.response;

import net.dv8tion.jda.core.entities.Guild;
import org.valdi.jmusicbot.JMusicBot;
import org.valdi.jmusicbot.delivery.rendering.pages.PageFactory;
import org.valdi.jmusicbot.delivery.webserver.auth.Authentication;
import org.valdi.jmusicbot.delivery.webserver.response.errors.ErrorResponse;
import org.valdi.jmusicbot.delivery.webserver.response.errors.ForbiddenResponse;
import org.valdi.jmusicbot.delivery.webserver.response.errors.InternalErrorResponse;
import org.valdi.jmusicbot.delivery.webserver.response.errors.NotFoundResponse;
import org.valdi.jmusicbot.delivery.webserver.response.pages.DebugPageResponse;
import org.valdi.jmusicbot.delivery.webserver.response.pages.PageResponse;
import org.valdi.jmusicbot.exceptions.ParseException;
import org.valdi.jmusicbot.exceptions.WebUserAuthException;
import org.valdi.jmusicbot.settings.locale.lang.ErrorPageLang;

import java.io.IOException;

/**
 * Factory for creating different {@link Response} objects.
 *
 * @author Rsl1122
 */
public class ResponseFactory {
    private final JMusicBot main;
    private final PageFactory pageFactory;

    public ResponseFactory(JMusicBot main, PageFactory pageFactory) {
        this.main = main;
        this.pageFactory = pageFactory;
    }

    public Response playFilesPageResponse(Authentication auth, Guild guild) {
        try {
            return new PageResponse(pageFactory.playFilesPage(auth, guild));
        } catch (ParseException e) {
            return internalErrorResponse(e, "Failed to parse play files page");
        }
    }

    public Response playListPageResponse(Authentication auth, Guild guild, String id) {
        try {
            return new PageResponse(pageFactory.playListPage(auth, guild, id));
        } catch (ParseException e) {
            return internalErrorResponse(e, "Failed to parse play list page");
        }
    }

    public Response debugPageResponse() {
        try {
            return new DebugPageResponse(main, pageFactory.debugPage());
        } catch (IOException e) {
            return internalErrorResponse(e, "Failed to parse debug page");
        }
    }

    public ErrorResponse internalErrorResponse(Throwable e, String s) {
        try {
            return new InternalErrorResponse(main, s, e);
        } catch (IOException improperRestartException) {
            return new ErrorResponse(main, "Error occurred: " + e.toString() + ", additional error " +
                    "occurred when attempting to send error page to user: " + improperRestartException.toString());
        }
    }

    public Response javaScriptResponse(String fileName) {
        try {
            return new JavaScriptResponse(main, fileName);
        } catch (IOException e) {
            return notFound404("JS File not found from jar: " + fileName + ", " + e.toString());
        }
    }

    public Response cssResponse(String fileName) {
        try {
            return new CSSResponse(main, fileName);
        } catch (IOException e) {
            return notFound404("CSS File not found from jar: " + fileName + ", " + e.toString());
        }
    }

    public Response imageResponse(String fileName) {
        return new ByteResponse(main, ResponseType.IMAGE, FileResponse.format(fileName));
    }

    public Response loginRedirectResponse(String token) {
        return new LoginRedirectResponse("/play/files", token);
    }

    public Response logoutRedirectResponse() {
        return new LogoutRedirectResponse("/play/files");
    }

    /**
     * Redirect somewhere
     *
     * @param location Starts with '/'
     * @return Redirection response.
     */
    public Response redirectResponse(String location) {
        return new RedirectResponse(location);
    }

    public Response faviconResponse() {
        return new ByteResponse(main, ResponseType.X_ICON, "web/favicon.ico");
    }

    public ErrorResponse pageNotFound404() {
        return notFound404(main.getLocale().getString(ErrorPageLang.UNKNOWN_PAGE_404));
    }

    public ErrorResponse notFound404(String message) {
        try {
            return new NotFoundResponse(main, message);
        } catch (IOException e) {
            return internalErrorResponse(e, "Failed to parse 404 page");
        }
    }

    public ErrorResponse basicAuthFail(WebUserAuthException e) {
        try {
            return PromptAuthorizationResponse.getBasicAuthResponse(main, e);
        } catch (IOException jarReadFailed) {
            return internalErrorResponse(e, "Failed to parse PromptAuthorizationResponse");
        }
    }

    public ErrorResponse forbidden403() {
        return forbidden403("Your user is not authorized to view this page.<br>"
                + "If you believe this is an error contact staff to change your access level.");
    }

    public ErrorResponse forbidden403(String message) {
        try {
            return new ForbiddenResponse(main, message);
        } catch (IOException e) {
            return internalErrorResponse(e, "Failed to parse ForbiddenResponse");
        }
    }

    public Response discordAuth() {
        return this.redirectResponse("/callback");
    }

    public ErrorResponse basicAuth() {
        try {
            return PromptAuthorizationResponse.getBasicAuthResponse(main);
        } catch (IOException e) {
            return internalErrorResponse(e, "Failed to parse PromptAuthorizationResponse");
        }
    }
}