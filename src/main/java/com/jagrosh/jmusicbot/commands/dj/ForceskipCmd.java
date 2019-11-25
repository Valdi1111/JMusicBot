/*
 * Copyright 2016 John Grosh <john.a.grosh@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jagrosh.jmusicbot.commands.dj;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.audio.AudioHandler;
import com.jagrosh.jmusicbot.commands.DJCommand;
import net.dv8tion.jda.core.entities.User;
import org.valdi.jmusicbot.data.music.Track;
import org.valdi.jmusicbot.utils.TrackUtils;

import java.util.Optional;

/**
 * @author John Grosh <john.a.grosh@gmail.com>
 */
public class ForceskipCmd extends DJCommand {
    public ForceskipCmd(Bot bot) {
        super(bot);
        this.name = "forceskip";
        this.help = "skips the current song";
        this.aliases = new String[]{"modskip"};
        this.bePlaying = true;
    }

    @Override
    public void doCommand(CommandEvent event) {
        AudioHandler handler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();
        User u = event.getJDA().getUserById(handler.getRequester());
        String title = handler.getPlayer().getPlayingTrack().getInfo().title;
        Optional<Track> t = TrackUtils.getTrack(bot.getDataManager().getDatabase(event.getGuild()), handler.getPlayer().getPlayingTrack());
        if(t.isPresent()) {
            title = t.get().getTitle();
        }
        event.reply(event.getClient().getSuccess() + " Skipped **" + title
                + "** (requested by " + (u == null ? "someone" : "**" + u.getName() + "**") + ")");
        handler.getPlayer().stopTrack();
    }
}
