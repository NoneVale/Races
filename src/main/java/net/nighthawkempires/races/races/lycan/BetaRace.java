package net.nighthawkempires.races.races.lycan;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class BetaRace {

    public String getName() {
        return "Beta";
    }

    /*public RaceType getRaceType() {
        return RaceType.LYCAN;
    }*/

    public int getTier() {
        return 2;
    }

    public String[] getDescription() {
        return new String[] { "Beta Lycans, while still retaining some of their youthful inexperience," +
                        " have honed many of their abilities and developed many new ones. "
        };
    }
}
