package net.nighthawkempires.races.races.demon;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class KnightOfHellRace implements Race {

    public String getName() {
        return "Knight of Hell";
    }

    public RaceType getRaceType() {
        return RaceType.DEMON;
    }

    public int getTier() {
        return 1;
    }

    public String[] getDescription() {
        return new String[] {
                GRAY + "Knight of Hell Description"
        };
    }
}
