package net.nighthawkempires.races.races.human;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class HunterRace implements Race {

    public String getName() {
        return "Hunter";
    }

    public RaceType getRaceType() {
        return RaceType.HUMAN;
    }

    public int getTier() {
        return 1;
    }

    public String[] getDescription() {
        return new String[] {
                GRAY + "Hunter Description"
        };
    }
}
