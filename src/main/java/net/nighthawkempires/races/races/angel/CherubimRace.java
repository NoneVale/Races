package net.nighthawkempires.races.races.angel;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.*;

public class CherubimRace implements Race {

    public String getName() {
        return "Cherubim";
    }

    public RaceType getRaceType() {
        return RaceType.ANGEL;
    }

    public int getTier() {
        return 2;
    }

    public String[] getDescription() {
        return new String[] {
                GRAY + "Cherubim Description"
        };
    }
}
