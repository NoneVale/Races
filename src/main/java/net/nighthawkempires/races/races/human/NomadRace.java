package net.nighthawkempires.races.races.human;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

import static org.bukkit.ChatColor.GRAY;

public class NomadRace implements Race {

    public String getName() {
        return "Nomad";
    }

    public RaceType getRaceType() {
        return RaceType.HUMAN;
    }

    public int getTier() {
        return 1;
    }

    public String[] getDescription() {
        return new String[] { "Nomads are humans who favor adventuring, creating, and nomadic roaming over the more sedentary" +
                        " life of a village. Nomads have a certain magic to them, and although they are not yet" +
                        " as powerful as they will become, it grants them certain abilities."
        };
    }
}
