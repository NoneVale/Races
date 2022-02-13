package net.nighthawkempires.races.races.dwarf;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class DuergarRace implements Race {

    public String getName() {
        return "Duergar";
    }

    public RaceType getRaceType() {
        return RaceType.DWARF;
    }

    public int getTier() {
        return 3;
    }

    public String[] getDescription() {
        return new String[] { "The oldest and most experienced of Dwarvenkind, Duergar are expert stonemasons and metalworkers," +
                        " and the impact of their hammers and pickaxes mark out the tempo of entire Dwarven cities." +
                        " They are finely attuned to the magic they graciously accept from the flesh of the earth."
        };
    }
}
