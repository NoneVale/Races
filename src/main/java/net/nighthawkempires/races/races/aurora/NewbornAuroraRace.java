package net.nighthawkempires.races.races.aurora;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class NewbornAuroraRace implements Race {

    public String getName() {
        return "Newborn Aurora";
    }

    public RaceType getRaceType() {
        return RaceType.AURORA;
    }

    public int getTier() {
        return 1;
    }

    public String[] getDescription() {
        return new String[] {
                GRAY + "Newborn Aurora Description"
        };
    }
}
