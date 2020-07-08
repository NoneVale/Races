package net.nighthawkempires.races.races.aurora;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class AbyssalAuroraRace implements Race {

    public String getName() {
        return "Abyssal Aurora";
    }

    public RaceType getRaceType() {
        return RaceType.AURORA;
    }

    public int getTier() {
        return 3;
    }

    public String[] getDescription() {
        return new String[] {
                GRAY + "Abyssal Aurora Description"
        };
    }
}
