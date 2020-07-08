package net.nighthawkempires.races.races.orc;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class WarbossRace implements Race {

    public String getName() {
        return "Warboss";
    }

    public RaceType getRaceType() {
        return RaceType.ORC;
    }

    public int getTier() {
        return 3;
    }

    public String[] getDescription() {
        return new String[] {
                GRAY + "Warboss Description"
        };
    }
}
