package org.valdi.jmusicbot.delivery.rendering.pages;

import net.dv8tion.jda.core.entities.Guild;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.valdi.jmusicbot.JMusicBot;
import org.valdi.jmusicbot.data.music.Playlist;
import org.valdi.jmusicbot.data.music.PlaylistTrack;
import org.valdi.jmusicbot.data.music.Track;
import org.valdi.jmusicbot.data.music.TrackType;
import org.valdi.jmusicbot.delivery.formatting.PlaceholderReplacer;
import org.valdi.jmusicbot.delivery.webserver.auth.Authentication;
import org.valdi.jmusicbot.delivery.webserver.auth.responses.DiscordUserResponse;
import org.valdi.jmusicbot.exceptions.ParseException;
import org.valdi.jmusicbot.utils.YoutubeLinkUtils;

import java.util.List;
import java.util.Map;

public class PlayListPage implements Page {
    private final Logger logger = LogManager.getLogger("WebServer PlayFilesPage");
    private final JMusicBot main;
    private final Guild guild;
    private final Authentication auth;
    private final String id;

    PlayListPage(JMusicBot main, Guild guild, Authentication auth, String id) {
        this.main = main;
        this.guild = guild;
        this.auth = auth;
        this.id = id;
    }

    @Override
    public String toHtml() throws ParseException {
        try {
            PlaceholderReplacer placeholders = new PlaceholderReplacer();

            Map<String, Playlist> playlists = main.getDataManager().getDatabase(guild).getPlaylists();
            List<PlaylistTrack> tracks = playlists.get(id).getOrderedTracks();
            StringBuilder music = new StringBuilder();
            if(tracks.isEmpty()) {
                music.append("<div class=\"alert alert-info\">La playlist è vuota.</div>");
            } else {
                music.append("<table class=\"table table-striped table-hover\">");
                music.append("<thead>");
                music.append("<tr>");
                music.append("<th style=\"width: 30px;\"></th>");
                music.append("<th style=\"width: 44px; text-align: right;\">#</th>");
                music.append("<th style=\"cursor: pointer;\">TITOLO</th>");
                music.append("<th style=\"width: 32px;\"></th>");
                music.append("<th style=\"width: 50px;\"></th>");
                music.append("<th style=\"cursor: pointer;\">ARTISTA</th>");
                music.append("<th style=\"cursor: pointer;\">ALBUM</th>");
                music.append("<th style=\"width: 32px;\"></th>");
                music.append("</tr>");
                music.append("</thead>");
                music.append("<tbody>");
                music.append("<tr>");
                for(PlaylistTrack pTrack : tracks) {
                    Track track = pTrack.getTrack();
                    if(track.getThumbnail().isEmpty()) {
                        if(track.getType() == TrackType.YOUTUBE) {
                            String img = YoutubeLinkUtils.getThumbnailUrl(track.getFile(), true);
                            appendImg(music, img);
                        } else {
                            music.append("<td style=\"width: 30px; height: 30px;\"></td>");
                        }
                    } else {
                        String img = "../data/cache/" + guild.getId() + "/" + track.getThumbnail();
                        appendImg(music, img);
                    }
                    music.append("<th scope=\"row\" style=\"min-width: 44px; width: 44px; text-align: right;\">").append(pTrack.getPos()).append("</th>");
                    music.append("<td>").append(track.getTitle()).append("</td>");
                    music.append("<td style=\"min-width: 32px; width: 32px;\"></td>");
                    music.append("<td style=\"min-width: 50px; width: 50px; text-align: right;\">").append(main.getFormatters().trackTime().apply(track.getDuration())).append("</td>");
                    music.append("<td>").append(track.getArtist()).append("</td>");
                    music.append("<td>").append(track.getAlbum()).append("</td>");
                    music.append("<td style=\"min-width: 32px; width: 32px; text-align: right\"></td>");
                    music.append("</tr>");
                }
                music.append("</tbody>");
                music.append("</table>");
            }
            placeholders.put("musiclist", music.toString());

            // Set playlists on sidebar
            StringBuilder lists = new StringBuilder();
            for(Playlist playlist : playlists.values()) {
                if(playlist.getId().equals(id)) {
                    placeholders.put("playlist", playlist.getName()); // Also set page title

                    lists.append("<li class=\"active\">");
                    lists.append("<a>");
                } else {
                    lists.append("<li>");
                    lists.append("<a href=\"/play/list?guild=").append(guild.getIdLong()).append("&id=").append(playlist.getId()).append("\">");
                }
                lists.append("<i class=\"material-icons\">playlist_play</i>");
                lists.append("<span>").append(playlist.getName()).append("</span>");
                lists.append("</a>");
                lists.append("</li>");
            }
            placeholders.put("playlists", lists.toString());


            DiscordUserResponse user = auth.getUser();
            String avatar = "https://cdn.discordapp.com/avatars/" + user.getId() + "/" + user.getAvatar() + ".png";
            if(user.getAvatar() == null) {
                avatar = "https://cdn.discordapp.com/embed/avatars/2.png?size=48";
            }

            placeholders.put("username", user.getUsername() + "#" + user.getDiscriminator());
            placeholders.put("email", user.getEmail());
            placeholders.put("avatar", avatar);
            placeholders.put("guild", guild.getName());

            return placeholders.apply(main.getCustomizableResourceOrDefault("web/play/list.html").asString());
        } catch (Exception e) {
            throw new ParseException(e);
        }
    }

    private void appendImg(StringBuilder builder, String url) {
        builder.append("<td style=\"padding: 0px; vertical-align: middle; width: 30px;\">");
        builder.append("<img style=\"width: 30px; height: 30px;\" src=").append(url).append(">");
        builder.append("</td>");
    }
}
