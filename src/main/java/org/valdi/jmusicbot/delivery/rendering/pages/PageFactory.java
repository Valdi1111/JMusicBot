package org.valdi.jmusicbot.delivery.rendering.pages;

import net.dv8tion.jda.core.entities.Guild;
import org.valdi.jmusicbot.JMusicBot;
import org.valdi.jmusicbot.delivery.webserver.auth.Authentication;

/**
 * Factory for creating different {@link Page} objects.
 *
 * @author Rsl1122
 */
public class PageFactory {
    private final JMusicBot main;

    public PageFactory(JMusicBot main) {
        this.main = main;
    }

    public DebugPage debugPage() {
        return new DebugPage(main);
    }

    public PlayFilesPage playFilesPage(Authentication auth, Guild guild) {
        return new PlayFilesPage(main, guild, auth);
    }

    public PlayListPage playListPage(Authentication auth, Guild guild, String id) {
        return new PlayListPage(main, guild, auth, id);
    }
}