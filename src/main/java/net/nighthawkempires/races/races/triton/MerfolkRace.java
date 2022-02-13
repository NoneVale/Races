package net.nighthawkempires.races.races.triton;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class MerfolkRace {

    public String getName() {
        return "Merfolk";
    }

    /*public RaceType getRaceType() {
        return RaceType.TRITON;
    }*/

    public int getTier() {
        return 1;
    }

    public String[] getDescription() {
        return new String[] { "These Tritons are the closest to their Merfolk ancestors. While they can breathe air," +
                " they are sensitive to arid climates and too much sun exposure."
        };
    }
}
