/*
 * Copyright 2019 John Grosh <john.a.grosh@gmail.com>.
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

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.commands.DJCommand;
import com.jagrosh.jmusicbot.commands.dj.addtrack.AddTrackDownload;
import com.jagrosh.jmusicbot.commands.dj.addtrack.AddTrackFile;
import com.jagrosh.jmusicbot.commands.dj.addtrack.AddTrackStream;

/**
 * @author Michaili K.
 */
public class AddTrackCmd extends DJCommand {

    public AddTrackCmd(Bot bot, String loadingEmoji) {
        super(bot);
        this.name = "addtrack";
        this.help = "add a track to the database";
        this.arguments = "<stream|download|file>";
        this.beListening = false;
        this.bePlaying = false;
        this.children = new Command[]{new AddTrackStream(bot, loadingEmoji), new AddTrackDownload(bot, loadingEmoji), new AddTrackFile(bot, loadingEmoji)};
    }

    @Override
    public void doCommand(CommandEvent event) {
        event.replyError("You need specify a subcommand!");
    }
}
