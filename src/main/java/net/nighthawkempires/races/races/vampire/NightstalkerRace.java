package net.nighthawkempires.races.races.vampire;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class NightstalkerRace {

    public String getName() {
        return "Nightstalker";
    }

    /*public RaceType getRaceType() {
        return RaceType.VAMPIRE;
    }*/

    public int getTier() {
        return 2;
    }

    public String[] getDescription() {
        return new String[] {
                GRAY + "Nightstalker Description"
        };
    }
}
