package net.nighthawkempires.races.races.triton;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class AbyssalRace {

    public String getName() {
        return "Abyssal";
    }

    /*public RaceType getRaceType() {
        return RaceType.TRITON;
    }*/

    public int getTier() {
        return 3;
    }

    public String[] getDescription() {
        return new String[] { "Abyssal Tritons have fully embraced both the land-walking and aquatic sides of their ancestry." +
                " They can survive in the deepest of oceans or atop the tallest of snowy peaks."
        };
    }
}