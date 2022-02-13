package net.nighthawkempires.races.races.lycan;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class AlphaRace {

    public String getName() {
        return "Alpha";
    }

    /*public RaceType getRaceType() {
        return RaceType.LYCAN;
    }*/

    public int getTier() {
        return 3;
    }

    public String[] getDescription() {
        return new String[] { "Alpha Lycans are the oldest and most advanced development stage. At this point," +
                        " they have developed all of their wolfish abilities, their wolfish form creates more physical changes," +
                        " and they can actually infect other players with Lycanthropy."
        };
    }
}
