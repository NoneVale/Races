package net.nighthawkempires.races.races.orc;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class UnderdarkRace {

    public String getName() {
        return "Underdark";
    }

    /*public RaceType getRaceType() {
        return RaceType.ORC;
    }*/

    public int getTier() {
        return 1;
    }

    public String[] getDescription() {
        return new String[] { "Underdark Orcs are generally thought of as the least powerful because they typically live in" +
                        " caves and are the least connected to their magic of all the Orcish subraces." +
                        " They are young, inexperienced, and oftentimes sensitive to the sun of the Overworld."
        };
    }
}
