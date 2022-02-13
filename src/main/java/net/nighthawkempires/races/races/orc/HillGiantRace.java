package net.nighthawkempires.races.races.orc;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class HillGiantRace {

    public String getName() {
        return "Hill Giant";
    }

    /*public RaceType getRaceType() {
        return RaceType.ORC;
    }*/

    public int getTier() {
        return 2;
    }

    public String[] getDescription() {
        return new String[] { "Hill Giant Orcs are characterized by their impressive magical skills. Unlike other races," +
                        " Orcs often train magic first and combat second, making Hill Giants the epitome of purely" +
                        " magical ability in Orcish society."
        };
    }
}
