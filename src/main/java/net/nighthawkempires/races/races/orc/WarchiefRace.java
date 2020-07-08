package net.nighthawkempires.races.races.orc;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class WarchiefRace implements Race {

    public String getName() {
        return "Warchief";
    }

    public RaceType getRaceType() {
        return RaceType.ORC;
    }

    public int getTier() {
        return 2;
    }

    public String[] getDescription() {
        return new String[] {
                GRAY + "Warchief Description"
        };
    }
}
