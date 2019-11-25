package org.valdi.jmusicbot;

import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.RichPresence;
import org.valdi.SuperApiX.common.config.advanced.adapters.AdapterInterface;
import org.valdi.SuperApiX.common.config.types.nodes.IConfigNode;

public class GameConfigAdapter implements AdapterInterface<Game> {

    @Override
    public Game deserialize(IConfigNode node) {
        String name = node.getString("name");
        String url = node.getString("url");
        int typeId = node.getInt("game-type");
        Game.GameType type = Game.GameType.fromKey(typeId);

        long start = node.getLong("start");
        long end = node.getLong("end");

        if(name == null || name.isEmpty()) {
            return null;
        }

        RichPresence.Timestamps time = null;
        if(start != 0L) {
            time = new RichPresence.Timestamps(start, end);
        }

        return new BetterGame(name, url, type, time);
    }

    @Override
    public void serialize(IConfigNode node, Game instance) {
        if(instance == null) {
            node.set("name", "default");
            node.set("game-type", 0);
            node.set("url", "");
            node.set("start", 0L);
            node.set("end", 0L);
            return;
        }

        String name = instance.getName();
        String url = instance.getUrl();
        Game.GameType type = instance.getType();
        RichPresence.Timestamps time = instance.getTimestamps();

        node.set("name", name);
        node.set("url", url);
        node.set("game-type", type.getKey());

        if(time != null) {
            node.set("start", time.getStart());
            node.set("end", time.getEnd());
        }
    }

    public class BetterGame extends Game {
        public BetterGame(String name, String url, GameType type, RichPresence.Timestamps timestamps) {
            super(name, url, type, timestamps);
        }
    }

}
