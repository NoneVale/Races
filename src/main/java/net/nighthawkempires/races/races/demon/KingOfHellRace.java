package net.nighthawkempires.races.races.demon;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class KingOfHellRace implements Race {

    public String getName() {
        return "King of Hell";
    }

    public RaceType getRaceType() {
        return RaceType.DEMON;
    }

    public int getTier() {
        return 3;
    }

    public String[] getDescription() {
        return new String[] {
                GRAY + "King of Hell Description"
        };
    }
}
