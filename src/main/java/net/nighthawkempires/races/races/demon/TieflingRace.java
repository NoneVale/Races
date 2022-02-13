package net.nighthawkempires.races.races.demon;

import net.nighthawkempires.races.races.Race;
import net.nighthawkempires.races.races.RaceType;

public class TieflingRace implements Race {

    public String getName() {
        return "Tiefling";
    }

    public RaceType getRaceType() {
        return RaceType.DEMON;
    }

    public int getTier() {
        return 1;
    }

    public String[] getDescription() {
        return new String[] { "Tiefling are the least powerful caste of the Demons, and often share many human qualities" +
                " -- most can even pass as human." +
                "  But they have distinctive features that give them away as Demons."
        };
    }
}
