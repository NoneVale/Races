package net.nighthawkempires.races.races.vampire;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class FledglingRace {

    public String getName() {
        return "Fledgling";
    }

    /*public RaceType getRaceType() {
        return RaceType.VAMPIRE;
    }*/

    public int getTier() {
        return 1;
    }

    public String[] getDescription() {
        return new String[] { "Vampire Fledglings are the youngest or most recently converted Vampires." +
                " While they are affected by the most basic and intrinsic racial traits, they have" +
                " little control over any of the more powerful Vampires' abilities and magic."
        };
    }
}