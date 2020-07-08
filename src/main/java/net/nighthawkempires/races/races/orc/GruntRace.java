package net.nighthawkempires.races.races.orc;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class GruntRace implements Race {

    public String getName() {
        return "Grunt";
    }

    public RaceType getRaceType() {
        return RaceType.ORC;
    }

    public int getTier() {
        return 1;
    }

    public String[] getDescription() {
        return new String[] {
                GRAY + "Grunt Description"
        };
    }
}
