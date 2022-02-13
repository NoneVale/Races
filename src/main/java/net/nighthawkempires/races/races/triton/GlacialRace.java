package net.nighthawkempires.races.races.triton;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class GlacialRace {

    public String getName() {
        return "Glacial";
    }

    /*public RaceType getRaceType() {
        return RaceType.TRITON;
    }*/

    public int getTier() {
        return 2;
    }

    public String[] getDescription() {
        return new String[] { "Glacial Tritons are accustomed to the near-freezing temperatures of frozen oceans," +
                " iceberg fields, and snowy tundras."
        };
    }
}
