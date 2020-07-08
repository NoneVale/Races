package net.nighthawkempires.races.races.aurora;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class GlacialAuroraRace implements Race {

    public String getName() {
        return "Glacial Aurora";
    }

    public RaceType getRaceType() {
        return RaceType.AURORA;
    }

    public int getTier() {
        return 2;
    }

    public String[] getDescription() {
        return new String[] {
                GRAY + "Glacial Aurora Description"
        };
    }
}
