package net.nighthawkempires.races.races.lycan;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class BetaRace implements Race {

    public String getName() {
        return "Beta";
    }

    public RaceType getRaceType() {
        return RaceType.LYCAN;
    }

    public int getTier() {
        return 2;
    }

    public String[] getDescription() {
        return new String[] {
                GRAY + "Beta Description"
        };
    }
}
